import { createRoute } from "@tanstack/react-router";
import { useQuery } from "@tanstack/react-query";
import { rootRoute } from "./__root";
import { projectQueries } from "../api/projects";
import { flagQueries } from "../api/flags";
import { PageLayout } from "../components/layout/PageLayout";
import { Card, CardContent, CardHeader, CardTitle } from "../components/ui/card";
import { Badge } from "../components/ui/badge";
import { FolderOpen, Flag, ToggleRight, Activity } from "lucide-react";

export const indexRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/",
  component: OverviewPage,
});

function OverviewPage() {
  const { data: projects } = useQuery(projectQueries.list());

  return (
    <PageLayout title="Overview" description="Your feature flag control plane at a glance.">
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard
          title="Projects"
          value={projects?.length ?? 0}
          icon={FolderOpen}
        />
        <StatCard
          title="Total Flags"
          value="--"
          icon={Flag}
          description="across all projects"
        />
        <StatCard
          title="Active Flags"
          value="--"
          icon={ToggleRight}
          description="currently enabled"
        />
        <StatCard
          title="System"
          value="Healthy"
          icon={Activity}
          badge={<Badge variant="success">ok</Badge>}
        />
      </div>

      <div className="grid gap-4 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="text-base">Recent Activity</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-sm text-muted-foreground">
              Flag changes will appear here once connected.
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-base">Quick Actions</CardTitle>
          </CardHeader>
          <CardContent className="space-y-2">
            <QuickLink href="/projects" label="View Projects" />
            <QuickLink href="/audit" label="View Audit Log" />
            <QuickLink href="/settings" label="Manage Settings" />
          </CardContent>
        </Card>
      </div>
    </PageLayout>
  );
}

function StatCard({
  title,
  value,
  icon: Icon,
  description,
  badge,
}: {
  title: string;
  value: string | number;
  icon: React.ElementType;
  description?: string;
  badge?: React.ReactNode;
}) {
  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between pb-2 space-y-0">
        <CardTitle className="text-sm font-medium text-muted-foreground">{title}</CardTitle>
        <Icon size={16} className="text-muted-foreground" />
      </CardHeader>
      <CardContent>
        <div className="flex items-center gap-2">
          <div className="text-2xl font-bold">{value}</div>
          {badge}
        </div>
        {description && (
          <p className="text-xs text-muted-foreground mt-1">{description}</p>
        )}
      </CardContent>
    </Card>
  );
}

function QuickLink({ href, label }: { href: string; label: string }) {
  return (
    <a
      href={href}
      className="flex items-center justify-between rounded-lg border px-4 py-3 text-sm transition-colors hover:bg-muted"
    >
      {label}
      <span className="text-muted-foreground">&rarr;</span>
    </a>
  );
}
