"use client";

import type {MRT_ColumnDef} from "material-react-table";
import {MaterialReactTable, useMaterialReactTable} from "material-react-table";
import {useMemo} from "react";
import {useTheme} from "@mui/material";
import Box from "@mui/material/Box";
import type {Criterion, NumberRange, Rubric, ShowcaseString, TestRun} from "../models/rubric";

function renderShowcaseString(str?: ShowcaseString | null, errorMsg = "No value") {
    return str?.html ? <div dangerouslySetInnerHTML={{__html: str.html}}/> : <p>{str?.text || errorMsg}</p>;
}

function renderNumberRange(range: NumberRange) {
    return range.min === range.max ? range.max : `[${range.min}, ${range.max}]`;
}

export default function RubricView(params: { rubric: Rubric }) {
    const theme = useTheme();
    const columns = useMemo<MRT_ColumnDef<Criterion>[]>(() => [
            {
                header: "Kriterium",
                accessorFn: (row) => row.name,
                Cell: ({cell}) => {
                    return renderShowcaseString(cell.getValue() as ShowcaseString, "");
                },
            },
            {
                header: "Möglich",
                accessorFn: (row) => renderNumberRange(row.possiblePoints),
            },
            {
                header: "Erreicht",
                accessorFn: (row) => renderNumberRange(row.achievedPoints),
            },
            {
                header: "Kommentar",
                accessorFn: (row) => row.message,
                Cell: ({cell}) => {
                    return renderShowcaseString(cell.getValue() as ShowcaseString, "");
                },
            },
        ],
        []);
    const data = params.rubric.criteria;

    const testRunColumns = useMemo<MRT_ColumnDef<TestRun>[]>(() => [
            {
                header: "Test ID",
                accessorFn: (row) => row.id,
            },
            {
                header: "Name",
                accessorFn: (row) => row.name,
            },
            {
                header: "State",
                accessorFn: (row) => row.state,
            },
            {
                header: "Message",
                accessorFn: (row) => row.message,
                Cell: ({cell}) => {
                    return renderShowcaseString(cell.getValue() as ShowcaseString, "");
                },
            },
            {
                header: "Duration",
                accessorFn: (row) => row.duration,
            },
            {
                header: "Stacktrace",
                accessorFn: (row) => row.stacktrace,
                Cell: ({cell}) => {
                    return renderShowcaseString(cell.getValue() as ShowcaseString, "");
                },
            },
        ],
        []);

    const table = useMaterialReactTable({
        columns,
        data,
        enableExpandAll: true, //hide expand all double arrow in column header
        enableExpanding: true,
        enableColumnOrdering: true,
        enableColumnResizing: true,
        enableStickyHeader: true,
        layoutMode: "grid",
        filterFromLeafRows: true, //apply filtering to all rows instead of just parent rows
        getSubRows: (row) => row.children, //default
        muiDetailPanelProps: () => ({
            sx: (them) => ({
                backgroundColor:
                    theme.palette.mode === "dark"
                        ? "rgba(255,210,244,0.1)"
                        : "rgba(0,0,0,0.1)",
            }),
        }),
        muiPaginationProps: ({table}) => ({
            color: "secondary",
            shape: "rounded",
            variant: "outlined",
        }),
        renderDetailPanel: ({row}) => {
            const testRunData = row.original.tests;
            let result = renderShowcaseString(row.original.description, "No Description.");

            if (!testRunData) {
                return result;
            }
            // eslint-disable-next-line react-hooks/rules-of-hooks
            const testRunTable = useMaterialReactTable({
                columns: testRunColumns,
                data: testRunData,
                enableColumnOrdering: true,
                enableColumnResizing: true,
                enableStickyHeader: true,
                layoutMode: "grid",
            });
            return <div>
                {result}
                <MaterialReactTable table={testRunTable}/>
            </div>
        },
        initialState: {}, //expand all rows by default
        paginateExpandedRows: false, //When rows are expanded, do not count sub-rows as number of rows on the page towards pagination
    });

    return <Box sx={{width: "100%"}}>
        <MaterialReactTable table={table}/>
    </Box>;
}
