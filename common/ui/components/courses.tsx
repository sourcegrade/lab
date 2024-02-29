"use client";
import { TypedDocumentNode, useQuery } from "@apollo/client";
import gql from "graphql-tag";
import { CourseDto } from "yougrade-hub/app/__generated__/graphql";
import withApollo, { client } from "@repo/ui/lib/withApollo";
const query: TypedDocumentNode<{course: CourseDto}>
 = gql`
    query fetchAllCourses {
        course {
            fetchAll {
                id
                name
                description
            }
        }
    }
`;
export default function Courses() {
    const { loading, data } = useQuery(query, {client});
    if(loading) {
        return <p>Loading...</p>;
    }
    if(!data) {
        return <p>No data</p>;
    }
    return <p>{JSON.stringify(data)}</p>;
}
