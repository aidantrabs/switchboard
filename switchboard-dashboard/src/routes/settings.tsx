import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";
import { PageLayout } from "../components/layout/PageLayout";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../components/ui/card";
import { Badge } from "../components/ui/badge";
import { Separator } from "../components/ui/separator";
import { Key, Globe, Shield } from "lucide-react";

export const settingsRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "settings",
  component: SettingsPage,
});

function SettingsPage() {
  return (
    <PageLayout title="Settings" description="Manage your switchboard configuration.">
      <div className="grid gap-6 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <div className="flex items-center gap-2">
              <Key size={16} className="text-muted-foreground" />
              <CardTitle className="text-base">API Keys</CardTitle>
            </div>
            <CardDescription>Manage authentication keys for the management API.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex items-center justify-between rounded-lg border p-3">
              <div>
                <p className="text-sm font-medium">Management Key</p>
                <p className="text-xs text-muted-foreground font-mono mt-0.5">sb_mgmt_••••••••</p>
              </div>
              <Badge variant="success">active</Badge>
            </div>
            <Separator />
            <p className="text-xs text-muted-foreground">
              Set via <code className="bg-muted px-1.5 py-0.5 rounded">switchboard.api-key</code> in application.yml
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <div className="flex items-center gap-2">
              <Shield size={16} className="text-muted-foreground" />
              <CardTitle className="text-base">SDK Keys</CardTitle>
            </div>
            <CardDescription>Keys used by SDKs to connect to the control plane.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex items-center justify-between rounded-lg border p-3">
              <div>
                <p className="text-sm font-medium">Demo SDK Key</p>
                <p className="text-xs text-muted-foreground font-mono mt-0.5">sb_demo_••••</p>
              </div>
              <Badge variant="secondary">demo</Badge>
            </div>
            <Separator />
            <p className="text-xs text-muted-foreground">
              SDK key management will be available in a future release.
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <div className="flex items-center gap-2">
              <Globe size={16} className="text-muted-foreground" />
              <CardTitle className="text-base">Connection</CardTitle>
            </div>
            <CardDescription>Server connection details.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-2 text-sm">
            <SettingRow label="API URL" value={window.location.origin} />
            <SettingRow label="Swagger" value="/swagger-ui.html" link />
            <SettingRow label="Health" value="/actuator/health" link />
          </CardContent>
        </Card>
      </div>
    </PageLayout>
  );
}

function SettingRow({ label, value, link }: { label: string; value: string; link?: boolean }) {
  return (
    <div className="flex items-center justify-between">
      <span className="text-muted-foreground">{label}</span>
      {link ? (
        <a href={value} target="_blank" rel="noopener noreferrer" className="font-mono text-xs hover:underline">
          {value}
        </a>
      ) : (
        <span className="font-mono text-xs">{value}</span>
      )}
    </div>
  );
}
