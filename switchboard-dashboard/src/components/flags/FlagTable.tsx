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
import { Link } from "@tanstack/react-router";
import type { FlagResponse } from "../../api/types";
import { ArrowUpDown } from "lucide-react";

const columnHelper = createColumnHelper<FlagResponse>();

const columns = [
  columnHelper.accessor("key", {
    header: "Key",
    cell: (info) => (
      <span className="font-mono text-sm">{info.getValue()}</span>
    ),
  }),
  columnHelper.accessor("name", { header: "Name" }),
  columnHelper.accessor("flagType", {
    header: "Type",
    cell: (info) => (
      <span className="text-xs px-2 py-0.5 bg-zinc-100 rounded dark:bg-zinc-800">
        {info.getValue().toLowerCase()}
      </span>
    ),
  }),
  columnHelper.accessor("updatedAt", {
    header: "Last Modified",
    cell: (info) => new Date(info.getValue()).toLocaleDateString(),
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
    <div>
      <input
        type="text"
        placeholder="Search flags..."
        value={globalFilter}
        onChange={(e) => setGlobalFilter(e.target.value)}
        className="mb-4 px-3 py-1.5 border border-zinc-200 rounded text-sm w-64 dark:bg-zinc-900 dark:border-zinc-700"
      />
      <table className="w-full text-sm">
        <thead>
          {table.getHeaderGroups().map((headerGroup) => (
            <tr key={headerGroup.id} className="border-b border-zinc-200 dark:border-zinc-800">
              {headerGroup.headers.map((header) => (
                <th
                  key={header.id}
                  onClick={header.column.getToggleSortingHandler()}
                  className="text-left py-2 px-3 font-medium text-zinc-500 cursor-pointer select-none"
                >
                  <div className="flex items-center gap-1">
                    {flexRender(header.column.columnDef.header, header.getContext())}
                    <ArrowUpDown size={12} />
                  </div>
                </th>
              ))}
            </tr>
          ))}
        </thead>
        <tbody>
          {table.getRowModel().rows.map((row) => (
            <tr
              key={row.id}
              className="border-b border-zinc-100 hover:bg-zinc-50 dark:border-zinc-800/50 dark:hover:bg-zinc-800/30"
            >
              {row.getVisibleCells().map((cell) => (
                <td key={cell.id} className="py-2 px-3">
                  {cell.column.id === "key" ? (
                    <Link
                      to={`/projects/${projectKey}/flags/${row.original.key}`}
                      className="text-blue-600 hover:underline dark:text-blue-400"
                    >
                      {flexRender(cell.column.columnDef.cell, cell.getContext())}
                    </Link>
                  ) : (
                    flexRender(cell.column.columnDef.cell, cell.getContext())
                  )}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      {table.getRowModel().rows.length === 0 && (
        <p className="text-zinc-500 text-center py-8">No flags found.</p>
      )}
    </div>
  );
}
