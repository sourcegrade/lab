import Navbar from "@repo/ui/components/navbar";
import "./globals.css";
import type { Metadata } from "next";
import { Inter } from "next/font/google";
import Footer from "@repo/ui/components/footer";
import { Box, Container, CssBaseline } from "@mui/material";
import ThemeRegistry from "@repo/ui/lib/ThemeRegistry";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
    title: "YouGrade",
    description: "A tool to grade automatically grade submissions",
};

export default function RootLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    return (
        <html lang="en">
            <body className={inter.className}>
                <ThemeRegistry options={{ key: "mui" }}>
                    <div className="flex flex-col min-h-screen bg-gray-900">
                        <CssBaseline />
                        <Navbar />
                        {/* <Showcase /> */}
                        <Container maxWidth={false} disableGutters>
                            <main className="bg-gray-800 w-[90vw] max-w-7xl mx-auto px-4 py-3 my-6 sm:px-6 lg:px-8 flex-grow rounded min-h-[80vh]">
                                {children}
                            </main>
                        </Container>
                        <Footer />
                    </div>
                </ThemeRegistry>
            </body>
        </html>
    );
}
