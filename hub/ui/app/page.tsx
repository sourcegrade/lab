import Button from "@mui/material/Button";
import AddCircleIcon from "@mui/icons-material/AddCircle";
import FormatListBulletedIcon from "@mui/icons-material/FormatListBulleted";
import CardCarousel, {CardContent} from "@repo/ui/components/card-carousel";
import _ from "lodash";

const demoCourses: CardContent[] = _.range(10).map((i) => ({
    title: `Course ${i}`,
    description: `Description ${i}`,
    image: "https://www.pngkit.com/png/full/12-120436_open-book-icon-png.png",
    openButtonEnabled: false,
    settingsButtonEnabled: false,
}));

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
            <h1>Dashboard</h1>
            <div className="flex justify-center space-x-6 p-3 rounded-md bg-slate-700">
                <Button variant="contained" className="flex-col flex-grow" disabled>
                    <AddCircleIcon className="!text-5xl" />
                    Create new Course
                </Button>
                <Button variant="contained" className="flex-col flex-grow" disabled>
                    <FormatListBulletedIcon className="!text-5xl" />
                    Show my Courses
                </Button>
            </div>
            <h2>Recent Courses</h2>
            <CardCarousel items={demoCourses}/>
            <h2>Recent Assignments</h2>
            <CardCarousel items={demoAssignments}/>
        </div>
    );
}
