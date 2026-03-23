import { rootRoute } from "./routes/__root";
import { indexRoute } from "./routes/index";
import { projectsRoute, projectsIndexRoute } from "./routes/projects";
import { flagListRoute } from "./routes/projects/$projectKey/flags/index";
import { flagDetailRoute } from "./routes/projects/$projectKey/flags/$flagKey/index";
import { auditRoute } from "./routes/audit";
import { settingsRoute } from "./routes/settings";

const routeTree = rootRoute.addChildren([
  indexRoute,
  projectsRoute.addChildren([
    projectsIndexRoute,
    flagListRoute,
    flagDetailRoute,
  ]),
  auditRoute,
  settingsRoute,
]);

export { routeTree };
