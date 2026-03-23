import { createRoute } from "@tanstack/react-router";
import { useQuery } from "@tanstack/react-query";
import { flagQueries } from "../../../../api/flags";
import { FlagTable } from "../../../../components/flags/FlagTable";
import { projectsRoute } from "../../../projects";

export const flagListRoute = createRoute({
  getParentRoute: () => projectsRoute,
  path: "$projectKey/flags",
  component: FlagListPage,
});

function FlagListPage() {
  const { projectKey } = flagListRoute.useParams();
  const { data: flags, isLoading } = useQuery(flagQueries.list(projectKey));

  if (isLoading) return <p className="text-zinc-500">Loading...</p>;

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-4">
        Flags — <span className="text-zinc-500">{projectKey}</span>
      </h1>
      <FlagTable data={flags ?? []} projectKey={projectKey} />
    </div>
  );
}
