import { useLocation } from "@tanstack/react-router";
import { LayoutDashboard, FolderOpen, ScrollText, Settings, ToggleRight } from "lucide-react";
import { cn } from "../../lib/utils";
import { Separator } from "../ui/separator";

const navigation = [
  { to: "/", icon: LayoutDashboard, label: "Overview" },
  { to: "/projects", icon: FolderOpen, label: "Projects" },
  { to: "/audit", icon: ScrollText, label: "Audit Log" },
  { to: "/settings", icon: Settings, label: "Settings" },
];

export function Sidebar() {
  return (
    <aside className="hidden md:flex w-60 flex-col border-r bg-sidebar text-sidebar-foreground">
      <div className="flex items-center gap-2.5 px-5 py-4">
        <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-sidebar-primary text-sidebar-primary-foreground">
          <ToggleRight size={16} />
        </div>
        <span className="text-sm font-semibold tracking-tight">switchboard</span>
      </div>

      <Separator />

      <nav className="flex-1 px-3 py-3 space-y-0.5">
        {navigation.map((item) => (
          <NavLink key={item.to} {...item} />
        ))}
      </nav>

      <Separator />

      <div className="px-5 py-3">
        <p className="text-xs text-muted-foreground">v0.1.0</p>
      </div>
    </aside>
  );
}

function NavLink({ to, icon: Icon, label }: { to: string; icon: React.ElementType; label: string }) {
  const location = useLocation();
  const isActive = to === "/" ? location.pathname === "/" : location.pathname.startsWith(to);

  return (
    <a
      href={to}
      className={cn(
        "flex items-center gap-3 rounded-lg px-3 py-2 text-sm transition-colors",
        isActive
          ? "bg-sidebar-accent text-sidebar-accent-foreground font-medium"
          : "text-muted-foreground hover:bg-sidebar-accent/50 hover:text-sidebar-accent-foreground",
      )}
    >
      <Icon size={16} />
      {label}
    </a>
  );
}
