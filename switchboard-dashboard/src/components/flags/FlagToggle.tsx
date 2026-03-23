import { useToggleFlag } from "../../api/flags";
import { Switch } from "../ui/switch";

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
    <Switch
      checked={enabled}
      onCheckedChange={() => toggle.mutate({ flagKey, envKey })}
      disabled={toggle.isPending}
    />
  );
}
