import { createRoute, Outlet } from "@tanstack/react-router";
import { useQuery } from "@tanstack/react-query";
import { projectQueries } from "../api/projects";
import { rootRoute } from "./__root";
import { PageLayout } from "../components/layout/PageLayout";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../components/ui/card";
import { Badge } from "../components/ui/badge";
import { FolderOpen, ChevronRight } from "lucide-react";

export const projectsRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "projects",
  component: () => <Outlet />,
});

export const projectsIndexRoute = createRoute({
  getParentRoute: () => projectsRoute,
  path: "/",
  component: ProjectsPage,
});

function ProjectsPage() {
  const { data: projects, isLoading } = useQuery(projectQueries.list());

  if (isLoading) {
    return (
      <PageLayout title="Projects" description="Manage your feature flag projects.">
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {[1, 2, 3].map((i) => (
            <Card key={i} className="animate-pulse">
              <CardHeader><div className="h-5 w-32 rounded bg-muted" /></CardHeader>
              <CardContent><div className="h-4 w-20 rounded bg-muted" /></CardContent>
            </Card>
          ))}
        </div>
      </PageLayout>
    );
  }

  return (
    <PageLayout title="Projects" description="Manage your feature flag projects.">
      {!projects?.length ? (
        <Card>
          <CardContent className="flex flex-col items-center justify-center py-12">
            <FolderOpen size={40} className="text-muted-foreground mb-3" />
            <p className="text-sm text-muted-foreground">No projects yet.</p>
            <p className="text-xs text-muted-foreground mt-1">
              Run <code className="bg-muted px-1.5 py-0.5 rounded text-xs">make seed</code> to create sample data.
            </p>
          </CardContent>
        </Card>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {projects.map((p) => (
            <a key={p.key} href={`/projects/${p.key}/flags`} className="group">
              <Card className="transition-colors group-hover:border-foreground/20">
                <CardHeader className="flex flex-row items-start justify-between space-y-0">
                  <div className="space-y-1">
                    <CardTitle className="text-base">{p.name}</CardTitle>
                    <CardDescription className="font-mono text-xs">{p.key}</CardDescription>
                  </div>
                  <ChevronRight size={16} className="text-muted-foreground mt-1 transition-transform group-hover:translate-x-0.5" />
                </CardHeader>
                <CardContent>
                  <Badge variant="secondary">
                    {new Date(p.createdAt).toLocaleDateString()}
                  </Badge>
                </CardContent>
              </Card>
            </a>
          ))}
        </div>
      )}
    </PageLayout>
  );
}
