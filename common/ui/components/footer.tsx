import { Box, Typography } from "@mui/material";

export default function Footer() {
  return (
    <Box
      component="footer"
      sx={{
        mt: "auto",
      }}
    >
      <Typography className="bg-gray-800 text-white text-center p-4">
        Copyright &copy; 2023 Ruben Deisenroth
      </Typography>
    </Box>
  );
}
