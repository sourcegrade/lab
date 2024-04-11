"use client";

import type { TypedDocumentNode } from "@graphql-typed-document-node/core";
import {
    MaterialReactTable,
    useMaterialReactTable,
    type MRT_ColumnDef, // <--- import MRT_ColumnDef
} from "material-react-table";
import Box from "@mui/material/Box";
import gql from "graphql-tag";
import { useQuery } from "@apollo/client";
import { client } from "@repo/ui/lib/with-apollo";
import type { UserDto } from "lab-hub/app/__generated__/graphql";

const query: TypedDocumentNode<{ course: { fetch: { members: UserDto[]}}, variables: { slug: string } }>
= gql`
    query fetchAllCourses($slug: String!) {
        course(id: $slug) {
            fetch {
                members {
                    id
                    username
                    email
                }
            }
        }
    }
`;
export default function Page({ params }: { params: { slug: string } }) {
    const { loading, data } = useQuery(
        query,
        {
            client, variables: { slug: params.slug },
        });
    const courseData = data?.course.fetch;
    console.log("courses", courseData)

    // material react table
    const columns: MRT_ColumnDef<UserDto>[] = [
        {
            accessorKey: "id",
            header: "ID",
            enableSorting: false,
        },
        {
            accessorKey: "username",
            header: "Username",
        },
        {
            accessorKey: "email",
            header: "Email",
        },
    ];
    const table = useMaterialReactTable({
        columns,
        data: courseData?.members?? [],
        initialState: { columnVisibility: { id: false } },
        enableColumnResizing: true,
        enableStickyHeader: true,
        enableRowSelection: true,
        enableColumnOrdering: true,
        rowPinningDisplayMode: "select-sticky",
        getRowId: (row) => row.id,
        layoutMode: "grid",
        // columnResizeMode: "onEnd",
    });
    return (
        <Box sx={{ width: "100%" }}>
            <MaterialReactTable table={table} />
        </Box>
    );
}
