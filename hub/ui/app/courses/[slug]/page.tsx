"use client";

import type { TypedDocumentNode } from "@graphql-typed-document-node/core";
import gql from "graphql-tag";
import { useQuery } from "@apollo/client";
import { client } from "@repo/ui/lib/with-apollo";
import { SemesterType } from "../../__generated__/graphql";
import type { CourseDto } from "../../__generated__/graphql";

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
    if (loading) {
        return <p>Loading...</p>;
    }
    if (!data) {
        return <p>No data</p>;
    }
    const courseData = data.course.fetch;
    console.log("courses", courseData)
    return (
        <div>
            <h1>{courseData.name}</h1>
            <h2>Semester</h2>
            <p>{
                courseData.semesterType === SemesterType.Ss ? "Sommersemester" : "Wintersemester"
            } {
                courseData.semesterType === SemesterType.Ws
                    ? `${courseData.semesterStartYear  }/${  courseData.semesterStartYear + 1}`
                    : courseData.semesterStartYear
            }</p>
            <h2>Description</h2>
            <p>{courseData.description}</p>
            <h2>Members</h2>
            <ul>
                {courseData.members.map((member) => (
                    <li key={member.id}>{member.username} ({member.email})</li>
                ))}
            </ul>
            <h2>Assignments</h2>
            <p>TODO</p>
        </div>
    );
}
