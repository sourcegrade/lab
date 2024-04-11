import Button from "@mui/material/Button";
import AddCircleIcon from "@mui/icons-material/AddCircle";
import GradingIcon from "@mui/icons-material/Grading";
import FormatListBulletedIcon from "@mui/icons-material/FormatListBulleted";
import type { CardContent } from "@repo/ui/components/card-carousel";
import CardCarousel from "@repo/ui/components/card-carousel";
import Courses from "@repo/ui/components/courses";
import _ from "lodash";

const demoAssignments: CardContent[] = _.range(10).map((i) => ({
    title: `Assignment ${i}`,
    description: `Description ${i}`,
    image: "https://www.pngkit.com/png/full/143-1435748_qb-d-grade-d-grade-png.png",
    openButtonEnabled: false,
    settingsButtonEnabled: false,
}));

export default function Page() {
    return (
        <div>
            <div className="flex justify-center space-x-6 p-3 rounded-md bg-slate-700">
                <Button className="flex-col flex-grow" href="/rubrics" variant="contained">
                    <GradingIcon className="!text-5xl"/>
                    View Test Rubric
                </Button>
            </div>
            <h1>Dashboard</h1>
            <div className="flex justify-center space-x-6 p-3 rounded-md bg-slate-700">
                <Button className="flex-col flex-grow" disabled variant="contained">
                    <AddCircleIcon className="!text-5xl"/>
                    Create new Course
                </Button>
                <Button className="flex-col flex-grow" disabled variant="contained">
                    <FormatListBulletedIcon className="!text-5xl"/>
                    Show my Courses
                </Button>
            </div>
            <h2>Recent Courses</h2>
            <Courses />
            <h2>Recent Assignments</h2>
            <CardCarousel items={demoAssignments}/>
        </div>
    );
}

// export default withApollo(Page, { getDataFromTree });
