import { useState } from "react";
import { useEvaluateFlag } from "../../api/flags";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../ui/card";
import { Input } from "../ui/input";
import { Button } from "../ui/button";
import { Badge } from "../ui/badge";
import { Separator } from "../ui/separator";
import { FlaskConical } from "lucide-react";

export function EvaluatePanel({
  projectKey,
  envKey,
  flagKey,
}: {
  projectKey: string;
  envKey: string;
  flagKey: string;
}) {
  const [userId, setUserId] = useState("");
  const [attributes, setAttributes] = useState("");
  const evaluate = useEvaluateFlag(projectKey, envKey);

  const handleEvaluate = () => {
    const attrs: Record<string, string> = {};
    if (attributes.trim()) {
      attributes.split(",").forEach((pair) => {
        const [k, v] = pair.split("=").map((s) => s.trim());
        if (k && v) attrs[k] = v;
      });
    }
    evaluate.mutate({ flagKey, userId: userId || "anonymous", attributes: attrs });
  };

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center gap-2">
          <FlaskConical size={16} className="text-muted-foreground" />
          <CardTitle className="text-base">Evaluate</CardTitle>
        </div>
        <CardDescription>Test flag evaluation for a user context</CardDescription>
      </CardHeader>
      <CardContent className="space-y-3">
        <Input
          placeholder="user id"
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
          className="font-mono text-xs"
        />
        <Input
          placeholder="attributes (key=val, key=val)"
          value={attributes}
          onChange={(e) => setAttributes(e.target.value)}
          className="font-mono text-xs"
        />
        <Button
          size="sm"
          className="w-full"
          onClick={handleEvaluate}
          disabled={evaluate.isPending}
        >
          {evaluate.isPending ? "evaluating..." : "evaluate"}
        </Button>

        {evaluate.data && (
          <>
            <Separator />
            <div className="space-y-2 text-sm">
              <div className="flex items-center justify-between">
                <span className="text-muted-foreground">variant</span>
                <span className="font-mono font-medium">{evaluate.data.variant}</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-muted-foreground">value</span>
                <span className="font-mono text-xs">{evaluate.data.value ?? "null"}</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-muted-foreground">reason</span>
                <Badge variant="outline" className="font-mono text-xs">
                  {evaluate.data.reason.toLowerCase().replace("_", " ")}
                </Badge>
              </div>
            </div>
          </>
        )}

        {evaluate.error && (
          <>
            <Separator />
            <p className="text-xs text-destructive-foreground">
              {(evaluate.error as Error).message}
            </p>
          </>
        )}
      </CardContent>
    </Card>
  );
}
