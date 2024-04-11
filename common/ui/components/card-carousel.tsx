// using client because of https://github.com/YIZHUANG/react-multi-carousel/issues/379
"use client";
import Card from "@mui/material/Card";
import CardMedia from "@mui/material/CardMedia";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import CardActions from "@mui/material/CardActions";
import Button from "@mui/material/Button";
import Carousel from "react-multi-carousel";
import VisibilityIcon from "@mui/icons-material/Visibility";
import SettingsIcon from "@mui/icons-material/Settings";
import "react-multi-carousel/lib/styles.css";
import "./card-carousel.scss";


export interface CardContent {
    title: string;
    description: string;
    image: string;
    openButtonEnabled?: boolean;
    settingsButtonEnabled?: boolean;
    openButtonHref?: string | null;
}

const responsive = {
    superLargeDesktop: {
        // the naming can be any, depends on you.
        breakpoint: { max: 4000, min: 3000 },
        items: 5,
    },
    desktop: {
        breakpoint: { max: 3000, min: 1024 },
        items: 3,
    },
    tablet: {
        breakpoint: { max: 1024, min: 464 },
        items: 2,
    },
    mobile: {
        breakpoint: { max: 464, min: 0 },
        items: 1,
    },
};

export default function CardCarousel(
    {
        items,
    }: {
        items: CardContent[];
    },
) {
    return (
        <div className="p-3 rounded-md bg-slate-700">
            <Carousel
                // ssr={true}
                // deviceType={"desktop"}
                arrows
                autoPlay={false}
                centerMode={false}
                infinite={false}
                keyBoardControl
                renderButtonGroupOutside
                renderDotsOutside
                responsive={responsive}
                showDots
            >
                {items.map(item => (
                    <Card sx={{ width: "90%" }}>
                        <CardMedia
                            alt="green iguana"
                            component="img"
                            image={item.image}
                            style={{
                                maxHeight: "200px",
                                objectFit: "contain",
                                backgroundColor: "white",
                            }}
                        />
                        <CardContent>
                            <Typography
                                component="div"
                                gutterBottom
                                style={{
                                    whiteSpace: "nowrap",
                                    overflow: "hidden",
                                    textOverflow: "ellipsis",
                                }}
                                variant="h5"
                            >
                                {item.title}
                            </Typography>
                            <Typography
                                color="text.secondary"
                                style={{
                                    height: "100px",
                                    overflow: "scroll",
                                }}
                                variant="body2"
                            >
                                {item.description}
                            </Typography>
                        </CardContent>
                        <CardActions>
                            <Button disabled={!(item.openButtonEnabled ?? true)}
                                    size="small"
                                    startIcon={<VisibilityIcon/>}
                                    variant="contained"
                                    {...(item.openButtonHref ? {
                                        href: item.openButtonHref,
                                    } : {})}
                            >
                                Open
                            </Button>
                            <Button disabled={!(item.settingsButtonEnabled ?? true)}
                                    size="small"
                                    startIcon={<SettingsIcon/>}
                                    variant="contained">
                                Settings
                            </Button>
                        </CardActions>
                    </Card>
                ))}
            </Carousel>
        </div>
    );
}
