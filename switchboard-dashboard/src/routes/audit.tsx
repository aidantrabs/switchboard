import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/audit")({
  component: () => (
    <div>
      <h1 className="text-2xl font-semibold mb-4">Audit Log</h1>
      <p className="text-zinc-500">Select a project to view audit logs.</p>
    </div>
  ),
});
