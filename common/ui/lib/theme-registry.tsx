"use client";
import createCache from "@emotion/cache";
import { useServerInsertedHTML } from "next/navigation";
import { CacheProvider } from "@emotion/react";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";
import React, { useMemo } from "react";
import { useMediaQuery } from "@mui/material";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";

// This implementation is from emotion-js
// https://github.com/emotion-js/emotion/issues/2928#issuecomment-1319747902
export default function ThemeRegistry(props: any) {
    const { options, children } = props;

    const [{ cache, flush }] = React.useState(() => {
        const ecache = createCache(options);
        ecache.compat = true;
        const prevInsert = ecache.insert;
        let inserted: string[] = [];
        ecache.insert = (...args) => {
            const serialized = args[1];
            if (ecache.inserted[serialized.name] === undefined) {
                inserted.push(serialized.name);
            }
            return prevInsert(...args);
        };
        const flush = () => {
            const prevInserted = inserted;
            inserted = [];
            return prevInserted;
        };
        return { cache: ecache, flush };
    });

    useServerInsertedHTML(() => {
        const names = flush();
        if (names.length === 0) {
            return null;
        }
        let styles = "";
        for (const name of names) {
            styles += cache.inserted[name];
        }
        return (
            <style
                dangerouslySetInnerHTML={{
                    __html: styles,
                }}
                data-emotion={`${cache.key} ${names.join(" ")}`}
                key={cache.key}
            />
        );
    });

    const prefersDarkMode = useMediaQuery("(prefers-color-scheme: dark)");

    const theme = useMemo(
        () =>
            createTheme({
                palette: {
                    mode: "dark",
                },
            }),
        [prefersDarkMode],
    );

    return (
        <LocalizationProvider
            dateAdapter={AdapterDayjs}>
            <CacheProvider value={cache}>
                <ThemeProvider theme={theme}>
                    <CssBaseline/>
                    {children}
                </ThemeProvider>
            </CacheProvider>
        </LocalizationProvider>
    );
}
