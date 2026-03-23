import { useState } from "react";
import { useUpdateRollout } from "../../api/flags";

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
    <div className="flex items-center gap-3">
      <input
        type="range"
        min={0}
        max={100}
        value={percentage}
        onChange={(e) => setPercentage(Number(e.target.value))}
        onMouseUp={() => updateRollout.mutate({ flagKey, envKey, percentage })}
        onTouchEnd={() => updateRollout.mutate({ flagKey, envKey, percentage })}
        className="flex-1"
      />
      <span className="text-sm font-mono w-10 text-right">{percentage}%</span>
    </div>
  );
}
