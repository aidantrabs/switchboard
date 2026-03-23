import { createRoute } from "@tanstack/react-router";
import { flagListRoute } from "../index";

export const flagDetailRoute = createRoute({
  getParentRoute: () => flagListRoute,
  path: "$flagKey",
  component: FlagDetailPage,
});

function FlagDetailPage() {
  const { projectKey, flagKey } = flagDetailRoute.useParams();

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-4">
        {flagKey} — <span className="text-zinc-500">{projectKey}</span>
      </h1>
      <p className="text-zinc-500">Flag detail view coming in next iteration.</p>
    </div>
  );
}
