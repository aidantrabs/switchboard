import { Route as rootRoute } from "./routes/__root";
import { Route as indexRoute } from "./routes/index";
import { Route as projectsRoute } from "./routes/projects";
import { Route as flagListRoute } from "./routes/projects/$projectKey/flags/index";
import { Route as flagDetailRoute } from "./routes/projects/$projectKey/flags/$flagKey/index";
import { Route as auditRoute } from "./routes/audit";
import { Route as settingsRoute } from "./routes/settings";

const projectsTree = projectsRoute.addChildren([
  flagListRoute.addChildren([flagDetailRoute]),
]);

const routeTree = rootRoute.addChildren([
  indexRoute,
  projectsTree,
  auditRoute,
  settingsRoute,
]);

export { routeTree };
