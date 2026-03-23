import { useState } from "react";
import { useNavigate } from "@tanstack/react-router";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { z } from "zod";
import { api } from "../../api/client";
import { flagKeys } from "../../api/flags";
import type { FlagResponse } from "../../api/types";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../ui/card";
import { Input } from "../ui/input";
import { Button } from "../ui/button";
import { Badge } from "../ui/badge";

const createFlagSchema = z.object({
  key: z.string()
    .min(1, "key is required")
    .max(255)
    .regex(/^[a-z0-9-]+$/, "lowercase letters, numbers, and hyphens only"),
  name: z.string().min(1, "name is required").max(255),
  description: z.string().max(1000).optional(),
  flagType: z.enum(["RELEASE", "EXPERIMENT", "OPERATIONAL", "PERMISSION"]),
});

type CreateFlagInput = z.infer<typeof createFlagSchema>;

const FLAG_TYPES = ["RELEASE", "EXPERIMENT", "OPERATIONAL", "PERMISSION"] as const;

export function CreateFlagForm({ projectKey, onClose }: { projectKey: string; onClose: () => void }) {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const [form, setForm] = useState<CreateFlagInput>({
    key: "",
    name: "",
    description: "",
    flagType: "RELEASE",
  });
  const [errors, setErrors] = useState<Partial<Record<keyof CreateFlagInput, string>>>({});

  const createFlag = useMutation({
    mutationFn: (input: CreateFlagInput) =>
      api.post<FlagResponse>(`/api/v1/projects/${projectKey}/flags`, {
        ...input,
        defaultVariant: "off",
        variants: [
          { key: "on", value: "true" },
          { key: "off", value: "false" },
        ],
      }),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: flagKeys.all(projectKey) });
      navigate({ to: `/projects/${projectKey}/flags/${data.key}` as string });
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const result = createFlagSchema.safeParse(form);
    if (!result.success) {
      const fieldErrors: typeof errors = {};
      result.error.errors.forEach((err) => {
        const field = err.path[0] as keyof CreateFlagInput;
        fieldErrors[field] = err.message;
      });
      setErrors(fieldErrors);
      return;
    }
    setErrors({});
    createFlag.mutate(result.data);
  };

  const updateField = (field: keyof CreateFlagInput, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }));
    if (errors[field]) setErrors((prev) => ({ ...prev, [field]: undefined }));
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-base">Create Flag</CardTitle>
        <CardDescription>Add a new feature flag to {projectKey}</CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <FieldGroup label="key" error={errors.key}>
            <Input
              placeholder="new-checkout-flow"
              value={form.key}
              onChange={(e) => updateField("key", e.target.value)}
              className="font-mono text-xs"
            />
          </FieldGroup>

          <FieldGroup label="name" error={errors.name}>
            <Input
              placeholder="New Checkout Flow"
              value={form.name}
              onChange={(e) => updateField("name", e.target.value)}
            />
          </FieldGroup>

          <FieldGroup label="description" error={errors.description}>
            <Input
              placeholder="optional description"
              value={form.description}
              onChange={(e) => updateField("description", e.target.value)}
            />
          </FieldGroup>

          <FieldGroup label="type" error={errors.flagType}>
            <div className="flex flex-wrap gap-2">
              {FLAG_TYPES.map((type) => (
                <button
                  key={type}
                  type="button"
                  onClick={() => updateField("flagType", type)}
                  className="focus:outline-none"
                >
                  <Badge
                    variant={form.flagType === type ? "default" : "outline"}
                    className="cursor-pointer"
                  >
                    {type.toLowerCase()}
                  </Badge>
                </button>
              ))}
            </div>
          </FieldGroup>

          {createFlag.error && (
            <p className="text-xs text-destructive-foreground">
              {(createFlag.error as Error).message}
            </p>
          )}

          <div className="flex gap-2 pt-2">
            <Button type="submit" size="sm" disabled={createFlag.isPending} className="flex-1">
              {createFlag.isPending ? "creating..." : "create flag"}
            </Button>
            <Button type="button" size="sm" variant="outline" onClick={onClose}>
              cancel
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  );
}

function FieldGroup({ label, error, children }: {
  label: string;
  error?: string;
  children: React.ReactNode;
}) {
  return (
    <div className="space-y-1.5">
      <label className="text-xs font-mono text-muted-foreground">{label}</label>
      {children}
      {error && <p className="text-xs text-destructive-foreground">{error}</p>}
    </div>
  );
}
