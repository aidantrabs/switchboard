import { useToggleFlag } from "../../api/flags";

export function FlagToggle({
  projectKey,
  flagKey,
  envKey,
  enabled,
}: {
  projectKey: string;
  flagKey: string;
  envKey: string;
  enabled: boolean;
}) {
  const toggle = useToggleFlag(projectKey);

  return (
    <button
      onClick={() => toggle.mutate({ flagKey, envKey })}
      disabled={toggle.isPending}
      className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors ${
        enabled ? "bg-green-500" : "bg-zinc-300 dark:bg-zinc-600"
      }`}
    >
      <span
        className={`inline-block h-4 w-4 rounded-full bg-white transition-transform ${
          enabled ? "translate-x-6" : "translate-x-1"
        }`}
      />
    </button>
  );
}
