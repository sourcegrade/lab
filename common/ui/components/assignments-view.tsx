"use client";

import { MaterialReactTable, type MRT_ColumnDef } from "material-react-table";
import dayjs from "dayjs";
import Box from "@mui/material/Box";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs"
import type { Theme } from "@mui/material/styles";
import React from "react";


export interface AssignmentDto {
    id: string;
    title: string;
    dueDate: string;
    maxPoints: number;
    actualPoints: number;
    status: "not submitted" | "submitted" | "graded" | "plagiarized";
}

export function statusColors(status: string, theme: Theme) {
    switch (status) {
        case "submitted":
            return theme.palette.info.main;
        case "graded":
            return theme.palette.success.main;
        case "plagiarized":
            return theme.palette.error.dark;
        case "overdue":
            return theme.palette.warning.main;
        default:
            return "gray";
    }
};

export default function AssignmentsView(
    {
        assignments,
        demoDate,
    }: {
        assignments: AssignmentDto[];
        demoDate: dayjs.Dayjs | null;
    },
) {
    // material react table
    const columns: MRT_ColumnDef<AssignmentDto>[] = [
        {
            header: "Übungsblatt ID",
            accessorFn: (row) => row.id === "99" ? "Projekt" : row.id,
            enableSorting: false,
        },
        {
            accessorKey: "title",
            header: "Title",
        },
        // {
        //     accessorKey: "dueDate",
        //     header: "Due Date",
        // },
        {
            accessorFn: (row) => new Date(row.dueDate), //convert to Date for sorting and filtering
            id: "dueDate",
            header: "Due Date",
            filterVariant: "date",
            filterFn: "moreThan",
            sortingFn: "datetime",
            Cell: ({ cell }) => cell.getValue<Date>().toLocaleDateString("de"), //render Date as a string
            Header: ({ column }) => <em>{column.columnDef.header}</em>, //custom header markup
            muiFilterTextFieldProps: {
                sx: {
                    minWidth: "250px",
                },
            },
        },
        {
            header: "Grade",
            accessorFn: (row) => `${row.status === "submitted" ? "??" : row.actualPoints}/${row.maxPoints}`,
            Cell: ({ row, cell }) => (
                <Box
                    component="span"
                    sx={(theme) => ({
                        color: Number(row.original.actualPoints) / row.original.maxPoints === 1 && row.original.status === "graded" ? theme.palette.success.main
                            : theme.palette.text.primary,
                    })}
                >
                    {cell.getValue<string>()}
                </Box>
            ),
            enableSorting: false,
        },
        {
            header: "Status",
            accessorFn: (row) => row.status === "not submitted" && demoDate?.isAfter(dayjs(row.dueDate)) ? "overdue" : row.status,
            Cell: ({ cell }) => (
                <Box
                    component="span"
                    sx={(theme) => ({
                        backgroundColor: statusColors(cell.getValue() as AssignmentDto["status"], theme),
                        borderRadius: "0.35rem",
                        color: "white",
                        padding: "4px",
                    })}
                >
                    {cell.getValue<string>()}
                </Box>
            ),
        },
    ];
    // const table = useMaterialReactTable({
    //     columns,
    //     data: dummyAssignments,
    //     // initialState: {columnVisibility: {description: false}},
    //     // enableColumnResizing: true,
    //     // enableStickyHeader: true,
    //     // enableRowSelection: true,
    //     // enableColumnOrdering: true,
    //     // rowPinningDisplayMode: "select-sticky",
    //     // getRowId: (row) => row.id,
    //     // layoutMode: "grid",
    //     // columnResizeMode: "onEnd",
    // });
    return (
        <LocalizationProvider
            dateAdapter={AdapterDayjs}
        >
            <Box sx={{ width: "100%" }}>
                <MaterialReactTable
                    columnResizeMode="onEnd"
                    columns={columns}
                    data={assignments}
                    enableColumnOrdering
                    enableColumnResizing
                    enableRowSelection
                    enableStickyHeader
                    initialState={{ pagination: { pageSize: 15, pageIndex: 0 } }}
                    layoutMode="grid"
                    rowPinningDisplayMode="select-sticky"
                />
            </Box>
        </LocalizationProvider>
    );
}
