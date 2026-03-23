import { createRoute } from "@tanstack/react-router";
import { rootRoute } from "./__root";

export const settingsRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "settings",
  component: () => (
    <div>
      <h1 className="text-2xl font-semibold mb-4">Settings</h1>
      <p className="text-zinc-500">SDK keys and project configuration coming soon.</p>
    </div>
  ),
});
