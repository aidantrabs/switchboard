import { ToggleRight, ArrowRight, Zap, Shield, Terminal, Globe, Play } from "lucide-react";

const DASHBOARD_URL = "http://localhost:5173";
const GITHUB_URL = "https://github.com/aidantrabs/switchboard";

export function Landing() {
  return (
    <div className="min-h-screen">
      <nav className="fixed top-0 left-0 right-0 z-50 border-b bg-background/80 backdrop-blur-md">
        <div className="mx-auto flex h-14 max-w-6xl items-center justify-between px-6">
          <div className="flex items-center gap-2.5">
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-foreground text-background">
              <ToggleRight size={14} />
            </div>
            <span className="text-sm font-semibold tracking-tight">switchboard</span>
          </div>
          <div className="flex items-center gap-3">
            <a href={GITHUB_URL} target="_blank" rel="noopener noreferrer"
              className="text-xs text-muted-foreground hover:text-foreground transition-colors font-mono">
              github
            </a>
            <a href={DASHBOARD_URL}
              className="inline-flex items-center gap-2 rounded-md bg-foreground px-4 py-2 text-sm font-medium text-background hover:bg-foreground/90 transition-colors">
              dashboard
              <ArrowRight size={14} />
            </a>
          </div>
        </div>
      </nav>

      <section className="relative pt-32 pb-16 px-6">
        <div className="absolute inset-0 pattern-grid opacity-[0.03]" />
        <div className="relative mx-auto max-w-4xl text-center">
          <p className="text-xs font-mono text-muted-foreground tracking-wider uppercase mb-6">
            01 — feature flag control plane
          </p>
          <h1 className="text-5xl sm:text-7xl font-bold tracking-tighter leading-[0.9]">
            ship features
            <br />
            <span className="text-outline">without deploys</span>
          </h1>
          <p className="mt-8 text-lg text-muted-foreground max-w-2xl mx-auto leading-relaxed">
            an open-source feature flag platform built with hexagonal architecture.
            toggle flags, target users, roll out gradually — all without touching
            your deploy pipeline.
          </p>
          <div className="mt-10 flex items-center justify-center gap-4">
            <a href={DASHBOARD_URL}
              className="inline-flex items-center gap-2 rounded-md bg-foreground px-6 py-2.5 text-sm font-medium text-background hover:bg-foreground/90 transition-colors">
              open dashboard
              <ArrowRight size={16} />
            </a>
            <a href="/swagger-ui.html" target="_blank" rel="noopener noreferrer"
              className="inline-flex items-center gap-2 rounded-md border px-6 py-2.5 text-sm font-medium hover:bg-muted transition-colors">
              api docs
            </a>
          </div>
        </div>
      </section>

      <section className="pb-24 px-6">
        <div className="mx-auto max-w-5xl">
          <DemoPreview
            src="/demo/dashboard.gif"
            fallbackTitle="dashboard overview"
            fallbackDescription="manage flags across projects and environments"
          />
        </div>
      </section>

      <section className="py-24 px-6 border-t">
        <div className="mx-auto max-w-6xl">
          <p className="text-xs font-mono text-muted-foreground tracking-wider uppercase mb-12">
            02 — how it works
          </p>
          <div className="grid gap-px bg-border sm:grid-cols-2 lg:grid-cols-4 rounded-lg overflow-hidden border">
            <FeatureCard icon={Zap} title="instant toggles"
              description="flip a flag and every connected service updates in seconds. no redeploy needed." />
            <FeatureCard icon={Shield} title="targeted rollouts"
              description="release to 5% of users in canada on the premium plan. targeting rules, your way." />
            <FeatureCard icon={Terminal} title="sdk + cli + api"
              description="pure java sdk, spring boot starter, openfeature provider, picocli terminal tool." />
            <FeatureCard icon={Globe} title="multi-environment"
              description="separate configs per environment. enabled in dev, 50% in staging, off in prod." />
          </div>
        </div>
      </section>

      <section className="py-24 px-6 border-t">
        <div className="mx-auto max-w-6xl">
          <p className="text-xs font-mono text-muted-foreground tracking-wider uppercase mb-12">
            03 — see it in action
          </p>
          <div className="grid gap-6 lg:grid-cols-2">
            <DemoPreview
              src="/demo/toggle.gif"
              fallbackTitle="toggle flags"
              fallbackDescription="enable or disable features per environment instantly"
              compact
            />
            <DemoPreview
              src="/demo/cli.gif"
              fallbackTitle="cli management"
              fallbackDescription="manage flags from your terminal with the switchboard cli"
              compact
            />
            <DemoPreview
              src="/demo/swagger.gif"
              fallbackTitle="api explorer"
              fallbackDescription="interactive swagger ui for the full rest api"
              compact
            />
            <DemoPreview
              src="/demo/sdk.gif"
              fallbackTitle="sdk integration"
              fallbackDescription="services evaluate flags locally with no network call per check"
              compact
            />
          </div>
        </div>
      </section>

      <section className="py-24 px-6 border-t">
        <div className="mx-auto max-w-4xl">
          <p className="text-xs font-mono text-muted-foreground tracking-wider uppercase mb-12">
            04 — quick start
          </p>
          <div className="rounded-lg border bg-card p-6 sm:p-8">
            <div className="space-y-4 font-mono text-sm">
              <CodeLine comment="clone and start" />
              <CodeLine code="git clone https://github.com/aidantrabs/switchboard.git" />
              <CodeLine code="cd switchboard" />
              <CodeLine code="make dev" />
              <CodeLine />
              <CodeLine comment="seed sample data" />
              <CodeLine code="make seed" />
              <CodeLine />
              <CodeLine comment="open dashboard" />
              <CodeLine code="open http://localhost:5173" />
              <CodeLine />
              <CodeLine comment="toggle a flag" />
              <CodeLine code="curl -X PATCH localhost:8080/api/v1/projects/demo-project/flags/new-checkout/environments/dev/toggle" />
            </div>
          </div>
        </div>
      </section>

      <section className="py-24 px-6 border-t">
        <div className="mx-auto max-w-4xl">
          <p className="text-xs font-mono text-muted-foreground tracking-wider uppercase mb-12">
            05 — integrate in 30 seconds
          </p>
          <div className="grid gap-6 sm:grid-cols-2">
            <div className="rounded-lg border bg-card p-6">
              <p className="text-xs font-mono text-muted-foreground mb-4">build.gradle.kts</p>
              <pre className="text-sm font-mono text-muted-foreground leading-relaxed overflow-x-auto">
{`dependencies {
  implementation(
    "com.switchboard:switchboard-spring-boot-starter:0.1.0"
  )
}`}
              </pre>
            </div>
            <div className="rounded-lg border bg-card p-6">
              <p className="text-xs font-mono text-muted-foreground mb-4">application.yml</p>
              <pre className="text-sm font-mono text-muted-foreground leading-relaxed overflow-x-auto">
{`switchboard:
  api-url: https://switchboard.internal
  api-key: \${SWITCHBOARD_API_KEY}
  project: my-service
  environment: production`}
              </pre>
            </div>
          </div>
        </div>
      </section>

      <footer className="border-t py-8 px-6">
        <div className="mx-auto max-w-6xl flex items-center justify-between">
          <div className="flex items-center gap-2.5">
            <div className="flex h-6 w-6 items-center justify-center rounded-md bg-foreground text-background">
              <ToggleRight size={12} />
            </div>
            <span className="text-xs font-mono text-muted-foreground">switchboard v0.1.0</span>
          </div>
          <a href={GITHUB_URL} target="_blank" rel="noopener noreferrer"
            className="text-xs font-mono text-muted-foreground hover:text-foreground transition-colors">
            github ↗
          </a>
        </div>
      </footer>
    </div>
  );
}

function DemoPreview({ src, fallbackTitle, fallbackDescription, compact }: {
  src: string;
  fallbackTitle: string;
  fallbackDescription: string;
  compact?: boolean;
}) {
  return (
    <div className={`rounded-lg border bg-card overflow-hidden ${compact ? "" : "shadow-2xl shadow-foreground/5"}`}>
      <div className="flex items-center gap-1.5 px-4 py-2.5 border-b bg-card">
        <div className="h-2.5 w-2.5 rounded-full bg-muted-foreground/20" />
        <div className="h-2.5 w-2.5 rounded-full bg-muted-foreground/20" />
        <div className="h-2.5 w-2.5 rounded-full bg-muted-foreground/20" />
        <span className="ml-2 text-xs font-mono text-muted-foreground/50">{fallbackTitle}</span>
      </div>
      <ImageOrFallback
        src={src}
        alt={fallbackTitle}
        fallbackTitle={fallbackTitle}
        fallbackDescription={fallbackDescription}
        compact={compact}
      />
    </div>
  );
}

function ImageOrFallback({ src, alt, fallbackTitle, fallbackDescription, compact }: {
  src: string;
  alt: string;
  fallbackTitle: string;
  fallbackDescription: string;
  compact?: boolean;
}) {
  return (
    <div className="relative">
      <img
        src={src}
        alt={alt}
        className="w-full"
        onError={(e) => {
          const target = e.currentTarget;
          target.style.display = "none";
          const fallback = target.nextElementSibling as HTMLElement;
          if (fallback) fallback.style.display = "flex";
        }}
      />
      <div
        className={`flex-col items-center justify-center text-center ${compact ? "py-16 px-6" : "py-24 px-8"}`}
        style={{ display: "none" }}
      >
        <Play size={compact ? 24 : 32} className="text-muted-foreground/30 mb-3" />
        <p className="text-sm font-medium text-muted-foreground">{fallbackTitle}</p>
        <p className="text-xs text-muted-foreground/60 mt-1 max-w-xs">{fallbackDescription}</p>
        <p className="text-xs font-mono text-muted-foreground/30 mt-4">
          add gif to /public/demo/
        </p>
      </div>
    </div>
  );
}

function FeatureCard({ icon: Icon, title, description }: {
  icon: React.ElementType;
  title: string;
  description: string;
}) {
  return (
    <div className="bg-background p-6 sm:p-8 space-y-3">
      <Icon size={20} className="text-muted-foreground" />
      <h3 className="text-sm font-semibold tracking-tight">{title}</h3>
      <p className="text-sm text-muted-foreground leading-relaxed">{description}</p>
    </div>
  );
}

function CodeLine({ code, comment }: { code?: string; comment?: string }) {
  if (!code && !comment) return <div className="h-2" />;
  if (comment) return <p className="text-muted-foreground/50"># {comment}</p>;
  return (
    <p>
      <span className="text-muted-foreground/50 select-none">$ </span>
      <span className="text-foreground/80">{code}</span>
    </p>
  );
}
