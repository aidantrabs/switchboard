import { queryOptions } from "@tanstack/react-query";
import { api } from "./client";
import type { ProjectResponse, EnvironmentResponse } from "./types";

export const projectKeys = {
  all: ["projects"] as const,
  environments: (projectKey: string) => ["environments", projectKey] as const,
};

export const projectQueries = {
  list: () =>
    queryOptions({
      queryKey: projectKeys.all,
      queryFn: () => api.get<ProjectResponse[]>("/api/v1/projects"),
      staleTime: 60_000,
    }),

  environments: (projectKey: string) =>
    queryOptions({
      queryKey: projectKeys.environments(projectKey),
      queryFn: () =>
        api.get<EnvironmentResponse[]>(`/api/v1/projects/${projectKey}/environments`),
      staleTime: 60_000,
    }),
};
