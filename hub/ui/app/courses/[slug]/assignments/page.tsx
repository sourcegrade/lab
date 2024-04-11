"use client";

import type { Dayjs } from "dayjs";
import dayjs from "dayjs";
import Box from "@mui/material/Box";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import React from "react";
import type { AssignmentDto } from "@repo/ui/components/assignments-view";
import AssignmentsView from "@repo/ui/components/assignments-view";

export default function Page({ params }: { params: { slug: string } }) {
    const [demoDate, setDemoDate] = React.useState<Dayjs | null>(dayjs("2024-01-01"));
    const dummyAssignments: AssignmentDto[] = [
        {
            id: "00",
            title: "Hands on mit Java & FopBot",
            dueDate: "2023-10-27 23:50",
            maxPoints: 5,
            actualPoints: 5,
            status: "graded",
        },
        {
            id: "01",
            title: "Foreign Contaminants",
            dueDate: "2023-11-03 23:50",
            maxPoints: 16,
            actualPoints: 10,
            status: "graded",
        },
        {
            id: "02",
            title: "Cleaning Convoy",
            dueDate: "2023-11-10 23:50",
            maxPoints: 32,
            actualPoints: 15,
            status: "graded",
        },
        {
            id: "03",
            title: "Multi-Family Robots & Synchronizers",
            dueDate: "2023-11-17 23:50",
            maxPoints: 32,
            actualPoints: 0,
            status: "not submitted",
        },
        {
            id: "04",
            title: "Die Übung mit der Maus",
            dueDate: "2023-11-24 23:50",
            maxPoints: 32,
            actualPoints: 0,
            status: "plagiarized",
        },
        {
            id: "05",
            title: "EDV-Zoo",
            dueDate: "2023-12-01 23:50",
            maxPoints: 32,
            actualPoints: 0,
            status: "graded",
        },
        {
            id: "06",
            title: "Maze Runner",
            dueDate: "2023-12-08 23:50",
            maxPoints: 32,
            actualPoints: 15,
            status: "submitted",
        },
        {
            id: "07",
            title: "Ausdrucksbaum",
            dueDate: "2023-12-15 23:50",
            maxPoints: 32,
            actualPoints: 32,
            status: "graded",
        },
        {
            id: "08",
            title: "The Exceptionals - State of Emergency",
            dueDate: "2024-01-05 23:50",
            maxPoints: 32,
            actualPoints: 30,
            status: "graded",
        },
        {
            id: "09",
            title: "Generics",
            dueDate: "2024-01-12 23:50",
            maxPoints: 32,
            actualPoints: 26,
            status: "graded",
        },
        {
            id: "10",
            title: "Mengen",
            dueDate: "2024-01-19 23:50",
            maxPoints: 32,
            actualPoints: 32,
            status: "graded",
        },
        {
            id: "11",
            title: "Running a Business",
            dueDate: "2024-01-31 23:50",
            maxPoints: 32,
            actualPoints: 0,
            status: "not submitted",
        },
        {
            id: "12",
            title: "Automaten parsen",
            dueDate: "2024-02-07 23:50",
            maxPoints: 32,
            actualPoints: 0,
            status: "not submitted",
        },
        {
            id: "13",
            title: "Codecraft",
            dueDate: "2024-02-14 23:50",
            maxPoints: 32,
            actualPoints: 0,
            status: "not submitted",
        },
        {
            "id": "99",
            "title": "Die Siedler von Catan",
            "dueDate": "2024-03-15 23:50",
            "maxPoints": 32,
            "actualPoints": 0,
            "status": "submitted",
        },
    ];
    return (
            <Box sx={{ width: "100%" }}>
                <p>Select Current Date:</p>
                <DatePicker
                    onChange={(newValue) => { setDemoDate(newValue); }}
                    value={demoDate}
                />
                <AssignmentsView assignments={dummyAssignments} demoDate={demoDate}/>
            </Box>
    );
}
