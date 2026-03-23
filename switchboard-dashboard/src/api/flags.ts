import { queryOptions, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "./client";
import type { FlagResponse, SdkFlagResponse, EvaluationResponse } from "./types";

export const flagKeys = {
  all: (projectKey: string) => ["flags", projectKey] as const,
  detail: (projectKey: string, flagKey: string) =>
    ["flags", projectKey, flagKey] as const,
  configs: (projectKey: string, envKey: string) =>
    ["flagConfigs", projectKey, envKey] as const,
};

export const flagQueries = {
  list: (projectKey: string) =>
    queryOptions({
      queryKey: flagKeys.all(projectKey),
      queryFn: () => api.get<FlagResponse[]>(`/api/v1/projects/${projectKey}/flags`),
      staleTime: 30_000,
    }),

  configs: (projectKey: string, envKey: string) =>
    queryOptions({
      queryKey: flagKeys.configs(projectKey, envKey),
      queryFn: () =>
        api.get<SdkFlagResponse[]>(`/api/v1/client/${projectKey}/${envKey}/flags`),
      staleTime: 10_000,
    }),
};

export function useToggleFlag(projectKey: string) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ flagKey, envKey }: { flagKey: string; envKey: string }) =>
      api.patch(`/api/v1/projects/${projectKey}/flags/${flagKey}/environments/${envKey}/toggle`),
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ["flags"] });
      queryClient.invalidateQueries({ queryKey: ["flagConfigs"] });
    },
  });
}

export function useUpdateRollout(projectKey: string) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      flagKey,
      envKey,
      percentage,
    }: {
      flagKey: string;
      envKey: string;
      percentage: number;
    }) =>
      api.put(`/api/v1/projects/${projectKey}/flags/${flagKey}/environments/${envKey}/rollout`, {
        percentage,
      }),
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ["flags"] });
      queryClient.invalidateQueries({ queryKey: ["flagConfigs"] });
    },
  });
}

export function useEvaluateFlag(projectKey: string, envKey: string) {
  return useMutation({
    mutationFn: (body: { flagKey: string; userId: string; attributes: Record<string, string> }) =>
      api.post<EvaluationResponse>(`/api/v1/client/${projectKey}/${envKey}/evaluate`, body),
  });
}
