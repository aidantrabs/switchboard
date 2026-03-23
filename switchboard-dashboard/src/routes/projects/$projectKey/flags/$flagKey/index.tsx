import { createRoute } from "@tanstack/react-router";
import { useQuery } from "@tanstack/react-query";
import { projectsRoute } from "../../../../projects";
import { flagQueries } from "../../../../../api/flags";
import { projectQueries } from "../../../../../api/projects";
import { PageLayout } from "../../../../../components/layout/PageLayout";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../../../../../components/ui/card";
import { Badge } from "../../../../../components/ui/badge";
import { Separator } from "../../../../../components/ui/separator";
import { FlagToggle } from "../../../../../components/flags/FlagToggle";
import { RolloutSlider } from "../../../../../components/flags/RolloutSlider";
import { Code, Layers } from "lucide-react";

export const flagDetailRoute = createRoute({
  getParentRoute: () => projectsRoute,
  path: "$projectKey/flags/$flagKey",
  component: FlagDetailPage,
});

function FlagDetailPage() {
  const { projectKey, flagKey } = flagDetailRoute.useParams();
  const { data: flags } = useQuery(flagQueries.list(projectKey));
  const { data: environments } = useQuery(projectQueries.environments(projectKey));

  const flag = flags?.find((f) => f.key === flagKey);

  if (!flag) {
    return (
      <PageLayout title="Flag not found" description={flagKey}>
        <Card>
          <CardContent className="py-12 text-center text-muted-foreground">
            This flag doesn't exist or hasn't loaded yet.
          </CardContent>
        </Card>
      </PageLayout>
    );
  }

  return (
    <PageLayout
      title={flag.name}
      description={flag.description || flag.key}
      actions={
        <Badge variant={flag.flagType === "RELEASE" ? "default" : "secondary"}>
          {flag.flagType.toLowerCase()}
        </Badge>
      }
    >
      <div className="grid gap-6 lg:grid-cols-3">
        <div className="lg:col-span-2 space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="text-base">Environments</CardTitle>
              <CardDescription>Toggle and configure per environment</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              {environments?.map((env) => (
                <div key={env.key} className="flex items-center justify-between rounded-lg border p-4">
                  <div className="space-y-0.5">
                    <p className="text-sm font-medium">{env.name}</p>
                    <p className="text-xs text-muted-foreground font-mono">{env.key}</p>
                  </div>
                  <FlagToggle
                    projectKey={projectKey}
                    flagKey={flagKey}
                    envKey={env.key}
                    enabled={false}
                  />
                </div>
              )) ?? (
                <p className="text-sm text-muted-foreground">No environments configured.</p>
              )}
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle className="text-base">Rollout</CardTitle>
              <CardDescription>Percentage-based gradual rollout</CardDescription>
            </CardHeader>
            <CardContent>
              {environments?.[0] && (
                <RolloutSlider
                  projectKey={projectKey}
                  flagKey={flagKey}
                  envKey={environments[0].key}
                  initialPercentage={0}
                />
              )}
            </CardContent>
          </Card>
        </div>

        <div className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="text-base">Variants</CardTitle>
            </CardHeader>
            <CardContent className="space-y-2">
              {flag.variants.length === 0 ? (
                <p className="text-sm text-muted-foreground">No variants defined.</p>
              ) : (
                flag.variants.map((v) => (
                  <div key={v.key} className="flex items-center justify-between rounded-lg border px-3 py-2">
                    <div className="flex items-center gap-2">
                      <Code size={14} className="text-muted-foreground" />
                      <span className="font-mono text-sm">{v.key}</span>
                    </div>
                    <span className="text-xs text-muted-foreground font-mono">{v.value}</span>
                  </div>
                ))
              )}
              <Separator className="my-2" />
              <div className="flex items-center gap-2 text-xs text-muted-foreground">
                <Layers size={12} />
                Default: <code className="bg-muted px-1.5 py-0.5 rounded">{flag.defaultVariant}</code>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle className="text-base">Details</CardTitle>
            </CardHeader>
            <CardContent className="space-y-3 text-sm">
              <DetailRow label="Key" value={flag.key} mono />
              <DetailRow label="Type" value={flag.flagType.toLowerCase()} />
              <DetailRow
                label="Created"
                value={new Date(flag.createdAt).toLocaleDateString(undefined, {
                  month: "short", day: "numeric", year: "numeric",
                })}
              />
              <DetailRow
                label="Modified"
                value={new Date(flag.updatedAt).toLocaleDateString(undefined, {
                  month: "short", day: "numeric", year: "numeric",
                })}
              />
            </CardContent>
          </Card>
        </div>
      </div>
    </PageLayout>
  );
}

function DetailRow({ label, value, mono }: { label: string; value: string; mono?: boolean }) {
  return (
    <div className="flex items-center justify-between">
      <span className="text-muted-foreground">{label}</span>
      <span className={mono ? "font-mono text-xs" : ""}>{value}</span>
    </div>
  );
}
