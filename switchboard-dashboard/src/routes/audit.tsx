import { createRoute } from "@tanstack/react-router";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { rootRoute } from "./__root";
import { projectQueries } from "../api/projects";
import { auditQueries } from "../api/audit";
import { PageLayout } from "../components/layout/PageLayout";
import { Card, CardContent } from "../components/ui/card";
import { Badge } from "../components/ui/badge";
import { Table, TableHeader, TableBody, TableHead, TableRow, TableCell } from "../components/ui/table";
import { ScrollText } from "lucide-react";

export const auditRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "audit",
  component: AuditPage,
});

const actionVariant = (action: string) => {
  if (action.includes("CREATED")) return "success" as const;
  if (action.includes("DELETED")) return "destructive" as const;
  if (action.includes("TOGGLED")) return "warning" as const;
  return "secondary" as const;
};

function AuditPage() {
  const { data: projects } = useQuery(projectQueries.list());
  const [selectedProject, setSelectedProject] = useState<string | null>(null);

  const projectKey = selectedProject ?? projects?.[0]?.key ?? null;
  const { data: logs, isLoading } = useQuery({
    ...auditQueries.byProject(projectKey ?? ""),
    enabled: !!projectKey,
  });

  return (
    <PageLayout title="Audit Log" description="Track all flag configuration changes.">
      {projects && projects.length > 1 && (
        <div className="flex gap-2">
          {projects.map((p) => (
            <button
              key={p.key}
              onClick={() => setSelectedProject(p.key)}
              className={`rounded-md border px-3 py-1.5 text-xs font-medium transition-colors ${
                projectKey === p.key
                  ? "border-primary bg-primary text-primary-foreground"
                  : "border-border hover:bg-muted"
              }`}
            >
              {p.name}
            </button>
          ))}
        </div>
      )}

      {!projectKey ? (
        <Card>
          <CardContent className="flex flex-col items-center justify-center py-12">
            <ScrollText size={40} className="text-muted-foreground mb-3" />
            <p className="text-sm text-muted-foreground">No projects available.</p>
          </CardContent>
        </Card>
      ) : isLoading ? (
        <Card className="animate-pulse">
          <CardContent className="py-12">
            <div className="h-4 w-48 rounded bg-muted mx-auto" />
          </CardContent>
        </Card>
      ) : !logs?.length ? (
        <Card>
          <CardContent className="flex flex-col items-center justify-center py-12">
            <ScrollText size={40} className="text-muted-foreground mb-3" />
            <p className="text-sm text-muted-foreground">No audit entries yet.</p>
          </CardContent>
        </Card>
      ) : (
        <div className="rounded-lg border">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Time</TableHead>
                <TableHead>Flag</TableHead>
                <TableHead>Action</TableHead>
                <TableHead>Environment</TableHead>
                <TableHead>Changed By</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {logs.map((log) => (
                <TableRow key={log.id}>
                  <TableCell className="text-xs text-muted-foreground whitespace-nowrap">
                    {new Date(log.timestamp).toLocaleString(undefined, {
                      month: "short", day: "numeric", hour: "2-digit", minute: "2-digit",
                    })}
                  </TableCell>
                  <TableCell>
                    <span className="font-mono text-sm">{log.flagKey ?? "—"}</span>
                  </TableCell>
                  <TableCell>
                    <Badge variant={actionVariant(log.action)}>
                      {log.action.toLowerCase().replace("_", " ")}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    {log.environmentKey ? (
                      <span className="font-mono text-xs">{log.environmentKey}</span>
                    ) : (
                      <span className="text-muted-foreground">—</span>
                    )}
                  </TableCell>
                  <TableCell className="text-muted-foreground text-sm">
                    {log.changedBy}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      )}
    </PageLayout>
  );
}
