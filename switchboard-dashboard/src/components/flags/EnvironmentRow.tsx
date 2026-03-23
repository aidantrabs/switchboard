import { useQuery } from "@tanstack/react-query";
import { flagQueries } from "../../api/flags";
import { FlagToggle } from "./FlagToggle";
import { RolloutSlider } from "./RolloutSlider";
import { Badge } from "../ui/badge";

export function EnvironmentRow({
  projectKey,
  flagKey,
  envKey,
  envName,
}: {
  projectKey: string;
  flagKey: string;
  envKey: string;
  envName: string;
}) {
  const { data: configs } = useQuery(flagQueries.configs(projectKey, envKey));
  const config = configs?.find((c) => c.key === flagKey);
  const enabled = config?.enabled ?? false;
  const rollout = config?.rolloutPercentage ?? 0;

  return (
    <div className="rounded-lg border p-4 space-y-3">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="space-y-0.5">
            <p className="text-sm font-medium">{envName}</p>
            <p className="text-xs text-muted-foreground font-mono">{envKey}</p>
          </div>
          {enabled && <Badge variant="success">on</Badge>}
        </div>
        <FlagToggle
          projectKey={projectKey}
          flagKey={flagKey}
          envKey={envKey}
          enabled={enabled}
        />
      </div>
      {enabled && (
        <RolloutSlider
          projectKey={projectKey}
          flagKey={flagKey}
          envKey={envKey}
          initialPercentage={rollout}
        />
      )}
    </div>
  );
}
