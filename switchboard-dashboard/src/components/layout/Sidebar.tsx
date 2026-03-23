import { Link } from "@tanstack/react-router";
import { Flag, FolderOpen, ScrollText, Settings } from "lucide-react";

export function Sidebar() {
  return (
    <aside className="w-56 border-r border-zinc-200 bg-zinc-50 p-4 flex flex-col gap-1 dark:bg-zinc-900 dark:border-zinc-800">
      <div className="text-lg font-semibold mb-4 px-2">switchboard</div>
      <NavLink to="/" icon={<Flag size={16} />} label="Dashboard" />
      <NavLink to="/projects" icon={<FolderOpen size={16} />} label="Projects" />
      <NavLink to="/audit" icon={<ScrollText size={16} />} label="Audit Log" />
      <NavLink to="/settings" icon={<Settings size={16} />} label="Settings" />
    </aside>
  );
}

function NavLink({ to, icon, label }: { to: string; icon: React.ReactNode; label: string }) {
  return (
    <a
      href={to}
      className="flex items-center gap-2 px-2 py-1.5 rounded text-sm text-zinc-600 hover:bg-zinc-200 dark:text-zinc-400 dark:hover:bg-zinc-800"
    >
      {icon}
      {label}
    </a>
  );
}
