import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";

export const auditRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "audit",
  component: () => (
    <div>
      <h1 className="text-2xl font-semibold mb-4">Audit Log</h1>
      <p className="text-zinc-500">Select a project to view audit logs.</p>
    </div>
  ),
});
