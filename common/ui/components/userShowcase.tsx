"use client";

import { useUser } from "../lib/auth";
import { Avatar, Button } from "@mui/material";
import md5 from "md5";

export function UserDisplay() {
  let user = useUser();
  return (
    <div className="bg-slate-700 p-5 rounded-sm flex flex-col items-center">
      <Avatar
        alt={user?.username ?? undefined}
        src={
          user?.email
            ? `http://www.gravatar.com/avatar/${md5(user?.email)}?s=256`
            : undefined
        }
        sx={{ width: 128, height: 128 }}
      />
      <p>User: {user?.username ?? "null"}</p>
      <p>Email: {user?.email ?? "null"}</p>
      <p>Last Seen: {user?.lastSeen.toString() ?? "null"}</p>
      <Button
        variant="contained"
        onClick={() => {
          fetch("/login/logout", {
            method: "POST",
            credentials: "include",
          }).then(() => {
            window.location.reload();
          });
        }}
      >Logout</Button>
    </div>
  );
}
