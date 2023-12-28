// using client because of https://github.com/YIZHUANG/react-multi-carousel/issues/379
"use client";
import Card from "@mui/material/Card";
import CardMedia from "@mui/material/CardMedia";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import CardActions from "@mui/material/CardActions";
import Button from "@mui/material/Button";
import Carousel from 'react-multi-carousel';
import 'react-multi-carousel/lib/styles.css';


export interface CardContent {
    title: string;
    description: string;
    image: string;
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

export default function CardCarousel<T extends CardContent>(
    {
        items
    }: {
        items: T[];
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
                renderDotsOutside={false}
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
                            >
                                {item.title}
                            </Typography>
                            <Typography
                                variant="body2"
                                color="text.secondary"
                            >
                                {item.description}
                            </Typography>
                        </CardContent>
                        <CardActions>
                            <Button size="small">Open</Button>
                            <Button size="small">Settings</Button>
                        </CardActions>
                    </Card>
                ))}
            </Carousel>
        </div>
    );
}