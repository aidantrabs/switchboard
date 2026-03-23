import { Menu } from "lucide-react";
import { Button } from "../ui/button";

export function Header({ onMenuClick }: { onMenuClick?: () => void }) {
  return (
    <header className="flex h-14 items-center gap-4 border-b bg-background px-4 md:px-6">
      <Button variant="ghost" size="icon" className="md:hidden" onClick={onMenuClick}>
        <Menu size={18} />
      </Button>
      <div className="flex-1" />
    </header>
  );
}
