"use client";

import type { TypedDocumentNode } from "@graphql-typed-document-node/core";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import Box from "@mui/material/Box";
import gql from "graphql-tag";
import { client } from "@repo/ui/lib/with-apollo";
import { useQuery } from "@apollo/client";
import React, { useState } from "react";
import { usePathname } from "next/navigation";
import type { CourseDto } from "lab-hub/app/__generated__/graphql";

const query: TypedDocumentNode<{ course: { fetch: CourseDto }, variables: { slug: string } }>
= gql`
    query fetchAllCourses($slug: String!) {
        course(id: $slug) {
            fetch {
                id
                name
                description
                semesterType
                semesterStartYear
                members {
                    id
                    username
                    email
                }
            }
        }
    }
`;

interface LinkTabProps {
    label?: string;
    href?: string;
    selected?: boolean;
}

function LinkTab(props: LinkTabProps) {
    console.log("LinkTab", props)
    return (
        <Tab
            component="a"
            onClick={(event: React.MouseEvent<HTMLAnchorElement>) => {
            }}
            {...props}
        />
    );
}

let courseData: CourseDto | undefined;

export default function CourseLayout(
    {
        children,
        params,
    }: {
        children: React.ReactNode
        params: { slug: string }
    }) {
    const { loading, data } = useQuery(
        query,
        {
            client, variables: { slug: params.slug },
        });
    courseData = data?.course.fetch;
    const links:LinkTabProps[] = [
        { href: `/courses/${params.slug}`, label: "Overview" },
        { href: `/courses/${params.slug}/members`, label: "Members" },
        { href: `/courses/${params.slug}/assignments`, label: "Assignments" },
    ];
    const path = usePathname();
    return (
        <div>
            <h1>{courseData?.name ?? "Loading course title..."}</h1>
            <Box sx={{ width: "100%" }}>
                <Tabs
                    aria-label="course tabs"
                    centered
                    value={links.findIndex((link) => link.href === path)}
                >
                    <LinkTab {...links[0]} />
                    <LinkTab {...links[1]} />
                    <LinkTab {...links[2]} />
                </Tabs>
            </Box>
            <div>{children}</div>
        </div>
    );
}
