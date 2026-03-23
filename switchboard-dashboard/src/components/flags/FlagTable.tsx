import {
  createColumnHelper,
  flexRender,
  getCoreRowModel,
  getSortedRowModel,
  getFilteredRowModel,
  useReactTable,
  type SortingState,
} from "@tanstack/react-table";
import { useState } from "react";
import type { FlagResponse } from "../../api/types";
import { Table, TableHeader, TableBody, TableHead, TableRow, TableCell } from "../ui/table";
import { Badge } from "../ui/badge";
import { Input } from "../ui/input";
import { ArrowUpDown, Flag } from "lucide-react";

const columnHelper = createColumnHelper<FlagResponse>();

const flagTypeVariant = (type: string) => {
  switch (type) {
    case "RELEASE": return "default" as const;
    case "EXPERIMENT": return "warning" as const;
    case "PERMISSION": return "secondary" as const;
    case "OPERATIONAL": return "outline" as const;
    default: return "secondary" as const;
  }
};

const columns = [
  columnHelper.accessor("key", {
    header: "Flag",
    cell: (info) => (
      <div className="flex items-center gap-2">
        <Flag size={14} className="text-muted-foreground shrink-0" />
        <span className="font-mono text-sm font-medium">{info.getValue()}</span>
      </div>
    ),
  }),
  columnHelper.accessor("name", {
    header: "Name",
    cell: (info) => (
      <span className="text-muted-foreground">{info.getValue()}</span>
    ),
  }),
  columnHelper.accessor("flagType", {
    header: "Type",
    cell: (info) => (
      <Badge variant={flagTypeVariant(info.getValue())}>
        {info.getValue().toLowerCase()}
      </Badge>
    ),
  }),
  columnHelper.accessor("updatedAt", {
    header: "Modified",
    cell: (info) => (
      <span className="text-muted-foreground text-xs">
        {new Date(info.getValue()).toLocaleDateString(undefined, {
          month: "short",
          day: "numeric",
          year: "numeric",
        })}
      </span>
    ),
  }),
];

export function FlagTable({
  data,
  projectKey,
}: {
  data: FlagResponse[];
  projectKey: string;
}) {
  const [sorting, setSorting] = useState<SortingState>([]);
  const [globalFilter, setGlobalFilter] = useState("");

  const table = useReactTable({
    data,
    columns,
    state: { sorting, globalFilter },
    onSortingChange: setSorting,
    onGlobalFilterChange: setGlobalFilter,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
  });

  return (
    <div className="space-y-4">
      <Input
        placeholder="Search flags..."
        value={globalFilter}
        onChange={(e) => setGlobalFilter(e.target.value)}
        className="max-w-sm"
      />

      <div className="rounded-lg border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => (
                  <TableHead
                    key={header.id}
                    onClick={header.column.getToggleSortingHandler()}
                    className="cursor-pointer select-none"
                  >
                    <div className="flex items-center gap-1.5">
                      {flexRender(header.column.columnDef.header, header.getContext())}
                      <ArrowUpDown size={12} className="text-muted-foreground" />
                    </div>
                  </TableHead>
                ))}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {table.getRowModel().rows.length === 0 ? (
              <TableRow>
                <TableCell colSpan={columns.length} className="h-24 text-center text-muted-foreground">
                  No flags found.
                </TableCell>
              </TableRow>
            ) : (
              table.getRowModel().rows.map((row) => (
                <TableRow key={row.id}>
                  {row.getVisibleCells().map((cell) => (
                    <TableCell key={cell.id}>
                      {cell.column.id === "key" ? (
                        <a
                          href={`/projects/${projectKey}/flags/${row.original.key}`}
                          className="hover:underline"
                        >
                          {flexRender(cell.column.columnDef.cell, cell.getContext())}
                        </a>
                      ) : (
                        flexRender(cell.column.columnDef.cell, cell.getContext())
                      )}
                    </TableCell>
                  ))}
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>
    </div>
  );
}
