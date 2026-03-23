import { createFileRoute, Link } from "@tanstack/react-router";
import { useQuery } from "@tanstack/react-query";
import { projectQueries } from "../api/projects";
import { FolderOpen } from "lucide-react";

export const Route = createFileRoute("/projects")({
  component: ProjectsPage,
});

function ProjectsPage() {
  const { data: projects, isLoading } = useQuery(projectQueries.list());

  if (isLoading) return <p className="text-zinc-500">Loading...</p>;

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-4">Projects</h1>
      {!projects?.length ? (
        <p className="text-zinc-500">No projects found.</p>
      ) : (
        <div className="grid gap-3">
          {projects.map((p) => (
            <Link
              key={p.key}
              to={`/projects/${p.key}/flags`}
              className="flex items-center gap-3 p-4 border border-zinc-200 rounded-lg hover:bg-zinc-50 dark:border-zinc-800 dark:hover:bg-zinc-800/50"
            >
              <FolderOpen size={18} className="text-zinc-400" />
              <div>
                <div className="font-medium">{p.name}</div>
                <div className="text-sm text-zinc-500">{p.key}</div>
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
