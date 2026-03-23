import { useState } from "react";
import { useUpdateRollout } from "../../api/flags";
import { Badge } from "../ui/badge";

export function RolloutSlider({
  projectKey,
  flagKey,
  envKey,
  initialPercentage,
}: {
  projectKey: string;
  flagKey: string;
  envKey: string;
  initialPercentage: number;
}) {
  const [percentage, setPercentage] = useState(initialPercentage);
  const updateRollout = useUpdateRollout(projectKey);

  return (
    <div className="space-y-2">
      <div className="flex items-center justify-between">
        <span className="text-sm text-muted-foreground">Rollout</span>
        <Badge variant="outline" className="font-mono">{percentage}%</Badge>
      </div>
      <input
        type="range"
        min={0}
        max={100}
        value={percentage}
        onChange={(e) => setPercentage(Number(e.target.value))}
        onMouseUp={() => updateRollout.mutate({ flagKey, envKey, percentage })}
        onTouchEnd={() => updateRollout.mutate({ flagKey, envKey, percentage })}
        className="w-full h-2 rounded-full appearance-none bg-muted accent-primary cursor-pointer"
      />
      <div className="flex justify-between text-xs text-muted-foreground">
        <span>0%</span>
        <span>100%</span>
      </div>
    </div>
  );
}
