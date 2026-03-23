import { queryOptions } from "@tanstack/react-query";
import { api } from "./client";
import type { AuditLogResponse } from "./types";

export const auditKeys = {
  project: (projectKey: string) => ["audit", projectKey] as const,
  flag: (projectKey: string, flagKey: string) => ["audit", projectKey, flagKey] as const,
};

export const auditQueries = {
  byProject: (projectKey: string) =>
    queryOptions({
      queryKey: auditKeys.project(projectKey),
      queryFn: () =>
        api.get<AuditLogResponse[]>(`/api/v1/projects/${projectKey}/audit-log`),
      staleTime: 15_000,
    }),

  byFlag: (projectKey: string, flagKey: string) =>
    queryOptions({
      queryKey: auditKeys.flag(projectKey, flagKey),
      queryFn: () =>
        api.get<AuditLogResponse[]>(
          `/api/v1/projects/${projectKey}/audit-log?flagKey=${flagKey}`
        ),
      staleTime: 15_000,
    }),
};
