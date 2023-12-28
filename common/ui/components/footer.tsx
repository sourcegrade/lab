"use client";
import {Box} from "@mui/material";
import GitHubButton from 'react-github-btn'

export default function Footer() {
    return (
        <Box
            component="footer"
            sx={{
                mt: "auto",
            }}
        >
            <div className="bg-gray-800 text-white text-center p-5">
                <p>Copyright &copy; 2023 Ruben Deisenroth and Alexander Staeding</p>
                <GitHubButton
                    href="https://github.com/sourcegrade/yougrade"
                    data-color-scheme="no-preference: dark; light: light; dark: dark;"
                    data-size="large"
                    data-icon="octicon-star"
                    data-show-count="true"
                    aria-label="Star sourcegrade/yougrade on GitHub"
                >
                    Star on GitHub
                </GitHubButton>
            </div>
        </Box>
    );
}
