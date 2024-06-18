"use client";

import type { TypedDocumentNode } from "@graphql-typed-document-node/core";
import gql from "graphql-tag";
import { useQuery } from "@apollo/client";
import { client } from "@repo/ui/lib/with-apollo";
import type { CourseDto } from "lab-hub/app/__generated__/graphql";
import { SemesterType } from "lab-hub/app/__generated__/graphql";

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
export default function Page({ params }: { params: { slug: string } }) {
    const { loading, data } = useQuery(
        query,
        {
            client, variables: { slug: params.slug },
        });
    const courseData = data?.course.fetch;
    console.log("courses", courseData)
    return (
        <div>
            <h2>Semester</h2>
            <p>{
                courseData
                    ? `${courseData.semesterType === SemesterType.Ss
                        ? "Sommersemester"
                        : "Wintersemester"
                    } ${courseData.semesterType === SemesterType.Ws
                        ? `${courseData.semesterStartYear}/${courseData.semesterStartYear + 1}`
                        : courseData.semesterStartYear
                    }`
                    : "Loading semester..."
            }</p>
            <h2>Description</h2>
            <p>{courseData?.description ?? "Loading description..."}</p>
            Tabs for overview, members, and assignments
            <h2>Upcomming Assignments</h2>
            <p>TODO</p>
        </div>
    );
}
