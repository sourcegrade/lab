"use client";
import { redirect } from "next/navigation";
import { useRouter } from "next/router";
import LoginIcon from "@mui/icons-material/Login";
import { useState } from "react";
import { useUser, loginOIDC, loginWithCredentials } from "@repo/ui/lib/auth";


export function OIDCSignInButton() {
    const handleOIDCClick = () => {
      loginOIDC();
    };
    return (
      <button
        className="w-full flex items-center font-semibold justify-center h-14 px-6 mt-4 text-xl  transition-colors duration-300 bg-white border-2 border-black text-black rounded-lg focus:shadow-outline hover:bg-slate-200"
        onClick={handleOIDCClick}
      >
        <LoginIcon />
        <span className="ml-4">Login with SSO</span>
      </button>
    );
}

interface CredentialsFormProps {
  csrfToken?: string;
  returnUrl?: string;
}

export function CredentialsForm(props: CredentialsFormProps) {
  const [error, setError] = useState<string | null>(null);

  const returnURL = props.returnUrl;

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const data = new FormData(e.currentTarget);

    const username = data.get("username")?.toString();
    const password = data.get("password")?.toString();
    if (!username || !password) {
      setError("Please enter a username and password");
      return;
    }

    const user = await loginWithCredentials(username, password);

    if (!user) {
      setError("Your Email or Password is wrong!");
    } else {
      redirectToReturnURL(returnURL);
    }
  };
  return (
    <form
      className="w-full mt-8 text-xl text-black font-semibold flex flex-col"
      onSubmit={handleSubmit}
    >
      {error ? <span className="p-4 mb-2 text-lg font-semibold text-white bg-red-500 rounded-md">
          {error}
        </span> : null}
      <input
        className="w-full px-4 py-4 mb-4 border border-gray-300 rounded-md"
        name="username"
        placeholder="Username"
        required
        type="text"
      />

      <input
        className="w-full px-4 py-4 mb-4 border border-gray-300 rounded-md"
        name="password"
        placeholder="Password"
        required
        type="password"
      />

      <button
        className="w-full h-12 px-6 mt-4 text-lg text-white transition-colors duration-150 bg-blue-600 rounded-lg focus:shadow-outline hover:bg-blue-700"
        type="submit"
      >
        Log in
      </button>
    </form>
  );
}

export function redirectToReturnURL(returnUrl?: unknown) {
  // if no return url is set, redirect to home page
  console.log("redirecting to return url", returnUrl);
  redirect(returnUrl && typeof returnUrl === "string" ? returnUrl : "/");
}

export async function redirectToReturnURLIfLoggedIn(returnUrl?: unknown) {
  // const user = await useUser();
  // if (user) {
  //   redirectToReturnURL(returnUrl);
  // }
}
