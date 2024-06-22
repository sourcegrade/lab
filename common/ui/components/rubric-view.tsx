"use client";

import type { MRT_ColumnDef } from "material-react-table";
import "bootstrap/dist/css/bootstrap.min.css"; // Import bootstrap CSS
import { MaterialReactTable, useMaterialReactTable } from "material-react-table";
import { useMemo } from "react";
import { useTheme } from "@mui/material";
import Box from "@mui/material/Box";
import LinearProgress from "@mui/material/LinearProgress";
import Paper from "@mui/material/Paper";
import {
    Chart, Legend,
    PieSeries,
    Title,
} from "@devexpress/dx-react-chart-material-ui";
import { Animation } from "@devexpress/dx-react-chart";
import type { Criterion, NumberRange, Rubric, ShowcaseString, TestRun } from "../models/rubric";

function renderShowcaseString(str?: ShowcaseString | null, errorMsg = "No value") {
    return str?.html ? <div dangerouslySetInnerHTML={{ __html: str.html }}/> : <p>{str?.text || errorMsg}</p>;
}

function renderNumberRange(range: NumberRange) {
    return range.min === range.max ? range.max : `[${range.min}, ${range.max}]`;
}

/**
 * Normalize the points to a value between 0 and 100
 * @param value the value to normalize
 * @param possiblePoints the range of possible points
 */
function normalizePoints(value: number, possiblePoints: NumberRange) {
    return ((value - possiblePoints.min)* 100) / (possiblePoints.max - possiblePoints.min);
}

export default function RubricView(params: { rubric: Rubric }) {
    const theme = useTheme();
    const rubric = params.rubric;
    const columns = useMemo<MRT_ColumnDef<Criterion>[]>(() => [
            {
                header: "Kriterium",
                accessorFn: (row) => row.name,
                Cell: ({ cell }) => {
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
                Cell: ({ cell }) => {
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
                grow: true,
                accessorFn: (row) => row.message,
                Cell: ({ cell }) => {
                    return renderShowcaseString(cell.getValue() as ShowcaseString, "");
                },
            },
            {
                header: "Duration",
                id: "duration",
                accessorFn: (row) => row.duration,
            },
            {
                header: "Stacktrace",
                id: "stacktrace",
                accessorFn: (row) => row.stacktrace,
                Cell: ({ cell }) => {
                    return <div dangerouslySetInnerHTML={{ __html: cell.getValue() as string }}/>;
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
        muiPaginationProps: ({ table }) => ({
            color: "secondary",
            shape: "rounded",
            variant: "outlined",
        }),
        renderDetailPanel: ({ row }) => {
            const testRunData = row.original.tests;
            const result = renderShowcaseString(row.original.description, "No Description.");

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
                // initialState: { columnVisibility: { "duration": false, "stacktrace": false } },
            });
            return <Box sx={{ width: "100%" }}>
                {result}
                {/*<MaterialReactTable table={testRunTable}/>*/}
            </Box>;
        },
        initialState: {}, //expand all rows by default
        paginateExpandedRows: false, //When rows are expanded, do not count sub-rows as number of rows on the page towards pagination
    });

    return <Box sx={{ width: "100%" }}>
        <h1>{renderShowcaseString(rubric.name)}</h1>
        <p>{renderShowcaseString(rubric.description)}</p>
        <p>Erreichte Punkte: {renderNumberRange(rubric.achievedPoints)}/{rubric.possiblePoints.max} ({normalizePoints(rubric.achievedPoints.min, rubric.possiblePoints)})%
            {/*<LinearProgress*/}
            {/*value={normalizePoints(rubric.achievedPoints.min, rubric.possiblePoints)} valueBuffer={normalizePoints(rubric.achievedPoints.max, rubric.possiblePoints)} variant="buffer"/>*/}
            <Paper>
                <Chart
                    data={[
                        ...(rubric.achievedPoints.min === rubric.achievedPoints.max)
                            ? [{ argument: "Erreicht", value: rubric.achievedPoints.min }]
                            : [{ argument: "Garantiert erreichte Punkte", value: rubric.achievedPoints.min }, { argument: "Zusätzlich mögliche Punkte", value: rubric.achievedPoints.max - rubric.achievedPoints.min }],
                        { argument: "Nicht erreichte Punkte", value: rubric.possiblePoints.max - rubric.achievedPoints.max },
                    ]}
                >
                    <PieSeries
                        argumentField="argument"
                        valueField="value"
                        innerRadius={0.6}
                    >
                        {/*<Label visible={true}>*/}
                        {/*    <Connector visible={true}/>*/}
                        {/*</Label>*/}
                    </PieSeries>
                    <Legend position="bottom"/>
                    <Title
                        text="Punkteverteilung"
                    />
                    <Animation />
                </Chart>
            </Paper>
        </p>
        <MaterialReactTable table={table}/>
    </Box>;
}
