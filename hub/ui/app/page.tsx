import Button from "@mui/material/Button";
import AddCircleIcon from "@mui/icons-material/AddCircle";
import FormatListBulletedIcon from "@mui/icons-material/FormatListBulleted";
import CardCarousel, {CardContent} from "@repo/ui/components/card-carousel";
import Courses from "@repo/ui/components/courses";
import _ from "lodash";
import withApollo, { client } from "@repo/ui/lib/withApollo";
import { getDataFromTree } from "@apollo/react-ssr";
// import { gql } from "yougrade-hub/app/__generated__/gql";
import gql from "graphql-tag";
import { TypedDocumentNode, useQuery } from "@apollo/client";
import { CourseDto } from "./__generated__/graphql";

const demoCourses: CardContent[] = _.range(10).map((i) => ({
    title: `Course ${i}`,
    description: `Description ${i}`,
    image: "https://www.pngkit.com/png/full/12-120436_open-book-icon-png.png",
    openButtonEnabled: true,
    settingsButtonEnabled: false,
    openButtonHref: `/courses/${i}`,
}));

const demoAssignments: CardContent[] = _.range(10).map((i) => ({
    title: `Assignment ${i}`,
    description: `Description ${i}`,
    image: "https://www.pngkit.com/png/full/143-1435748_qb-d-grade-d-grade-png.png",
    openButtonEnabled: false,
    settingsButtonEnabled: false,
}));

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

export default function Page() {
    // const { loading, data } = useQuery(query, {client});
    // var recentCourses:JSX.Element;
    // if (loading) {
    //     recentCourses = <p>Loading...</p>;
    // } else {
    //     recentCourses = <p>{JSON.stringify(data)}</p>;
    // }
    return (
        <div>
            <h1>Dashboard</h1>
            {/* {recentCourses} */}
            <Courses></Courses>
            <br/>
            <div className="flex justify-center space-x-6 p-3 rounded-md bg-slate-700">
                <Button variant="contained" className="flex-col flex-grow" disabled>
                    <AddCircleIcon className="!text-5xl" />
                    Create new Course
                </Button>
                <Button variant="contained" className="flex-col flex-grow" disabled>
                    <FormatListBulletedIcon className="!text-5xl" />
                    Show my Courses
                </Button>
            cCbb</div>
            <h2>Recent Courses</h2>
            <CardCarousel items={demoCourses}/>
            <h2>Recent Assignments</h2>
            <CardCarousel items={demoAssignments}/>
        </div>
    );
}

// export default withApollo(Page, { getDataFromTree });
