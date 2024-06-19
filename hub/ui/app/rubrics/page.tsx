"use client";
import { Box, Grid, Paper, Typography } from "@mui/material";
import SwipeableViews from "react-swipeable-views";
import * as React from "react";
import { useState } from "react";
import TabContext from "@mui/lab/TabContext";
import Tab from "@mui/material/Tab";
import TabList from "@mui/lab/TabList";
import RubricView from "@repo/ui/components/rubric-view";
import { useTheme } from "@mui/material/styles";
import TabPanel from "@mui/lab/TabPanel";
import * as rubric from "./example_rubric.json";

function RubricPage() {
    const theme = useTheme();
    // const [rubric, setRubric] = useState<any>(null);
    //
    // useEffect(() => {
    //     const fetchRubric = async () => {
    //         try {
    //             const response = await fetch('./example_rubric.json');
    //             const data = await response.json();
    //             setRubric(data);
    //         } catch (error) {
    //             console.error('Error fetching rubric:', error);
    //         }
    //     };
    //
    //     fetchRubric();
    // }, []);
    const [tab, setTab] = useState("0");
    return (
        <div>
            <TabContext value={tab}>
                <Box sx={{ width: "100%" }}>
                    <TabList
                        centered
                        onChange={(event, newValue) => { setTab(newValue); }}
                    >
                        <Tab label="Rubric" value="0"/>
                        <Tab label="Tests" value="1"/>
                        <Tab label="Source Code" value="2"/>
                    </TabList>
                </Box>
                <TabPanel value="0">
                    <div className="flex justify-center space-x-6 p-3 rounded-md bg-slate-700">
                        <RubricView rubric={rubric} />
                        {/*<Grid container spacing={3}>*/}
                        {/*    <Grid item xs={12}>*/}
                        {/*        <Typography align="center" variant="h1">*/}
                        {/*            Rubric: {rubric.name.text}*/}
                        {/*        </Typography>*/}
                        {/*    </Grid>*/}
                        {/*    <Grid item xs={12}>*/}
                        {/*        <Typography align="center" variant="body1">*/}
                        {/*            {rubric.description.text}*/}
                        {/*        </Typography>*/}
                        {/*    </Grid>*/}
                        {/*    <Grid item xs={12}>*/}
                        {/*        {rubric.criteria.map((criterion: any, index: number) => (*/}
                        {/*            <Paper elevation={3} key={index} style={{ padding: "20px" }}>*/}
                        {/*                <Typography variant="h2">{criterion?.name?.text}</Typography>*/}
                        {/*                <Typography variant="body1">{criterion?.description?.text}</Typography>*/}
                        {/*                <Typography variant="body1">*/}
                        {/*                    Possible*/}
                        {/*                    Points: {criterion?.possible_points?.min} - {criterion?.possible_points?.max}*/}
                        {/*                </Typography>*/}
                        {/*                <Typography variant="body1">Achieved*/}
                        {/*                    Points: {criterion?.achieved_points?.single}</Typography>*/}
                        {/*                /!* Render tests if present *!/*/}
                        {/*                {criterion?.tests?.length > 0 && (*/}
                        {/*                    <>*/}
                        {/*                        <Typography variant="h3">Tests:</Typography>*/}
                        {/*                        {criterion.tests.map((test: any, idx: number) => (*/}
                        {/*                            <Paper elevation={2} key={idx}*/}
                        {/*                                   style={{ padding: "10px", margin: "10px 0" }}>*/}
                        {/*                                <Typography*/}
                        {/*                                    variant="body1">Test {idx + 1}: {test?.name}</Typography>*/}
                        {/*                                <Typography variant="body2">State: {test?.state}</Typography>*/}
                        {/*                                <Typography*/}
                        {/*                                    variant="body2">Message: {test?.message?.text}</Typography>*/}
                        {/*                                <Typography*/}
                        {/*                                    variant="body2">Duration: {test?.duration} ms</Typography>*/}
                        {/*                            </Paper>*/}
                        {/*                        ))}*/}
                        {/*                    </>*/}
                        {/*                )}*/}
                        {/*            </Paper>*/}
                        {/*        ))}*/}
                        {/*    </Grid>*/}
                        {/*</Grid>*/}
                    </div>
                </TabPanel>
                <TabPanel value="1">
                    <div className="flex justify-center space-x-6 p-3 rounded-md bg-slate-700">
                        <Grid container spacing={3}>
                            <Grid item xs={12}>
                                <Typography align="center" variant="h1">
                                    Tests
                                </Typography>
                            </Grid>
                        </Grid>
                    </div>
                </TabPanel>
                <SwipeableViews
                    axis={theme.direction === "rtl" ? "x-reverse" : "x"}
                    index={tab}
                    onChangeIndex={(index) => { setTab(index); }}
                    >

                <TabPanel value="2">
                    <div className="flex justify-center space-x-6 p-3 rounded-md bg-slate-700">
                        <Grid container spacing={3}>
                            <Grid item xs={12}>
                                <Typography align="center" variant="h1">
                                    Source Code
                                </Typography>
                            </Grid>
                        </Grid>
                    </div>
                </TabPanel>
                </SwipeableViews>
            </TabContext>
        </div>
    );
}

export default RubricPage;
