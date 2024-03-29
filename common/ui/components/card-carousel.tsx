// using client because of https://github.com/YIZHUANG/react-multi-carousel/issues/379
"use client";
import Card from "@mui/material/Card";
import CardMedia from "@mui/material/CardMedia";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import CardActions from "@mui/material/CardActions";
import Button from "@mui/material/Button";
import Carousel from 'react-multi-carousel';
import VisibilityIcon from '@mui/icons-material/Visibility';
import SettingsIcon from '@mui/icons-material/Settings';
import 'react-multi-carousel/lib/styles.css';
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
        breakpoint: {max: 4000, min: 3000},
        items: 5
    },
    desktop: {
        breakpoint: {max: 3000, min: 1024},
        items: 3
    },
    tablet: {
        breakpoint: {max: 1024, min: 464},
        items: 2
    },
    mobile: {
        breakpoint: {max: 464, min: 0},
        items: 1
    }
};

export default function CardCarousel(
    {
        items
    }: {
        items: CardContent[];
    }
) {
    return (
        <div className="p-3 rounded-md bg-slate-700">
            <Carousel
                // ssr={true}
                // deviceType={"desktop"}
                responsive={responsive}
                infinite={false}
                autoPlay={false}
                arrows={true}
                showDots={true}
                renderButtonGroupOutside={true}
                renderDotsOutside={true}
                centerMode={false}
                keyBoardControl={true}
            >
                {items.map(item => (
                    <Card sx={{width: "90%"}}>
                        <CardMedia
                            component="img"
                            alt="green iguana"
                            image={item.image}
                            style={{
                                maxHeight: "200px",
                                objectFit: "contain",
                                backgroundColor: "white"
                            }}
                        />
                        <CardContent>
                            <Typography
                                gutterBottom
                                variant="h5"
                                component="div"
                                style={{
                                    whiteSpace: "nowrap",
                                    overflow: "hidden",
                                    textOverflow: "ellipsis"
                                }}
                            >
                                {item.title}
                            </Typography>
                            <Typography
                                variant="body2"
                                color="text.secondary"
                                style={{
                                    height: "100px",
                                    overflow: "scroll",
                                }}
                            >
                                {item.description}
                            </Typography>
                        </CardContent>
                        <CardActions>
                            <Button size="small"
                                    disabled={!(item.openButtonEnabled ?? true)}
                                    startIcon={<VisibilityIcon/>}
                                    variant={"contained"}
                                    {...(item.openButtonHref ? {
                                        href: item.openButtonHref,
                                    } : {})}
                            >
                                Open
                            </Button>
                            <Button size="small"
                                    disabled={!(item.settingsButtonEnabled ?? true)}
                                    startIcon={<SettingsIcon/>}
                                    variant={"contained"}>
                                Settings
                            </Button>
                        </CardActions>
                    </Card>
                ))}
            </Carousel>
        </div>
    );
}
