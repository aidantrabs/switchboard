export interface Variant {
  key: string;
  value: string;
}

export interface Condition {
  attribute: string;
  operator: string;
  value: string;
}

export interface TargetingRule {
  priority: number;
  conditions: Condition[];
  servedVariantKey: string;
}

export interface FlagResponse {
  id: string;
  key: string;
  name: string;
  description: string;
  flagType: string;
  defaultVariant: string;
  variants: Variant[];
  createdAt: string;
  updatedAt: string;
}

export interface FlagConfigResponse {
  flagId: string;
  enabled: boolean;
  rolloutPercentage: number;
  targetingRules: TargetingRule[];
}

export interface SdkFlagResponse {
  key: string;
  enabled: boolean;
  defaultVariant: string;
  variants: Variant[];
  rolloutPercentage: number;
  targetingRules: TargetingRule[];
}

export interface EvaluationResponse {
  flagKey: string;
  variant: string;
  value: string;
  reason: string;
}

export interface ProjectResponse {
  id: string;
  name: string;
  key: string;
  createdAt: string;
}

export interface EnvironmentResponse {
  id: string;
  name: string;
  key: string;
  sortOrder: number;
}

export interface AuditLogResponse {
  id: string;
  flagKey: string;
  environmentKey: string;
  action: string;
  changedBy: string;
  beforeState: string | null;
  afterState: string | null;
  timestamp: string;
}
