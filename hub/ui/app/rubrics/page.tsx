"use client";
import { Grid, Paper, Typography } from "@mui/material";
import * as rubric from "./example_rubric.json";

function RubricPage() {
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
    return (
        <div className="flex justify-center space-x-6 p-3 rounded-md bg-slate-700">
            <Grid container spacing={3}>
                <Grid item xs={12}>
                    <Typography align="center" variant="h1">
                        Rubric: {rubric.name.text}
                    </Typography>
                </Grid>
                <Grid item xs={12}>
                    <Typography align="center" variant="body1">
                        {rubric.description.text}
                    </Typography>
                </Grid>
                <Grid item xs={12}>
                    {rubric.criteria.map((criterion: any, index: number) => (
                        <Paper elevation={3} key={index} style={{ padding: "20px" }}>
                            <Typography variant="h2">{criterion?.name?.text}</Typography>
                            <Typography variant="body1">{criterion?.description?.text}</Typography>
                            <Typography variant="body1">
                                Possible Points: {criterion?.possible_points?.min} - {criterion?.possible_points?.max}
                            </Typography>
                            <Typography variant="body1">Achieved
                                Points: {criterion?.achieved_points?.single}</Typography>
                            {/* Render tests if present */}
                            {criterion?.tests?.length > 0 && (
                                <>
                                    <Typography variant="h3">Tests:</Typography>
                                    {criterion.tests.map((test: any, idx: number) => (
                                        <Paper elevation={2} key={idx} style={{ padding: "10px", margin: "10px 0" }}>
                                            <Typography variant="body1">Test {idx + 1}: {test?.name}</Typography>
                                            <Typography variant="body2">State: {test?.state}</Typography>
                                            <Typography variant="body2">Message: {test?.message?.text}</Typography>
                                            <Typography variant="body2">Duration: {test?.duration} ms</Typography>
                                        </Paper>
                                    ))}
                                </>
                            )}
                        </Paper>
                    ))}
                </Grid>
            </Grid>
        </div>
    );
}

export default RubricPage;
