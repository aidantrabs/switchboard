import {
  createColumnHelper,
  flexRender,
  getCoreRowModel,
  getSortedRowModel,
  useReactTable,
} from "@tanstack/react-table";
import type { AuditLogResponse } from "../../api/types";

const columnHelper = createColumnHelper<AuditLogResponse>();

const columns = [
  columnHelper.accessor("timestamp", {
    header: "Time",
    cell: (info) => new Date(info.getValue()).toLocaleString(),
  }),
  columnHelper.accessor("flagKey", { header: "Flag" }),
  columnHelper.accessor("action", {
    header: "Action",
    cell: (info) => (
      <span className="text-xs px-2 py-0.5 bg-zinc-100 rounded dark:bg-zinc-800">
        {info.getValue()}
      </span>
    ),
  }),
  columnHelper.accessor("environmentKey", { header: "Environment" }),
  columnHelper.accessor("changedBy", { header: "Changed By" }),
];

export function AuditTable({ data }: { data: AuditLogResponse[] }) {
  const table = useReactTable({
    data,
    columns,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
  });

  return (
    <table className="w-full text-sm">
      <thead>
        {table.getHeaderGroups().map((headerGroup) => (
          <tr key={headerGroup.id} className="border-b border-zinc-200 dark:border-zinc-800">
            {headerGroup.headers.map((header) => (
              <th key={header.id} className="text-left py-2 px-3 font-medium text-zinc-500">
                {flexRender(header.column.columnDef.header, header.getContext())}
              </th>
            ))}
          </tr>
        ))}
      </thead>
      <tbody>
        {table.getRowModel().rows.map((row) => (
          <tr
            key={row.id}
            className="border-b border-zinc-100 dark:border-zinc-800/50"
          >
            {row.getVisibleCells().map((cell) => (
              <td key={cell.id} className="py-2 px-3">
                {flexRender(cell.column.columnDef.cell, cell.getContext())}
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
}
