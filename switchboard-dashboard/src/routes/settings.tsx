import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/settings")({
  component: () => (
    <div>
      <h1 className="text-2xl font-semibold mb-4">Settings</h1>
      <p className="text-zinc-500">SDK keys and project configuration coming soon.</p>
    </div>
  ),
});
