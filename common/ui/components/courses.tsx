"use client";
import { TypedDocumentNode, useQuery } from "@apollo/client";
import gql from "graphql-tag";
import { CourseDto } from "lab-hub/app/__generated__/graphql";
import withApollo, { client } from "@repo/ui/lib/withApollo";
import CardCarousel, {CardContent} from "./card-carousel";
const query: TypedDocumentNode<{course: {fetchAll: CourseDto[]}}>
= gql`
    query fetchAllCourses {
        course {
            fetchAll {
                id
                name
                description
                semesterStartYear
                semesterType
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
    const courses = data?.course.fetchAll;
    console.log("courses", courses)
    const cardCourses: CardContent[] = courses?.map((course) => ({
        title: course.name,
        description: course.description,
        image: "https://www.pngkit.com/png/full/12-120436_open-book-icon-png.png",
        openButtonEnabled: true,
        settingsButtonEnabled: false,
        openButtonHref: `/courses/${course.id}`,
    })) || [];
    return <CardCarousel items={cardCourses}/>;
}
