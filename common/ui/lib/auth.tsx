import { User } from "../models/User";
import { plainToClass } from "class-transformer";
import { redirect } from "next/navigation";
import useSWR from "swr";
import { useEffect } from "react";
import { Router } from "next/router";

export const api_url = process.env.BACKEND_URL ?? "http://localhost:3000";

/**
 * Fetches the currently logged in user.
 * @returns The logged in user or null if the user is not logged in.
 */
export function useUser(
  redirectTo?: string,
  redirectIfFound?: string
): User | null {
  const { data: userJson, error } = useSWR(`${api_url}/login/me`, () =>
    fetch(`${api_url}/login/me`, {
      method: "GET",
      credentials: "include",
    }).then((res) => res.json())
  );
  const finished = !!userJson;
  console.log("userJson", userJson);

  useEffect(() => {
    if (!redirectTo || !finished) return;
    if (
      // If redirectTo is set, redirect if the user was not found.
      (redirectTo && !redirectIfFound && !userJson) ||
      // If redirectIfFound is also set, redirect if the user was found
      (redirectIfFound && userJson)
    ) {
      redirect(redirectTo);
    }
  }, [redirectTo, redirectIfFound, finished, userJson]);
  if (error) {
    console.log("error", error);
    return null;
  }
  return !!userJson ? plainToClass(User, userJson) : null;
}

/**
 * Logs the user in using the credentials flow.
 *
 * @param username the username of the user
 * @param password the password of the user
 * @returns The logged in user or null if the user is not logged in.
 */
export async function loginWithCredentials(
  username: string,
  password: string
): Promise<User | null> {
  const response = await fetch(`${api_url}/login/login`, {
    method: "POST",
    body: JSON.stringify({ username, password }),
  });
  // return response.ok ? useUser() : null;
  return null;
}

/**
 * Logs the user in using the OpenID Connect flow.
 *
 * @returns The logged in user or null if the user is not logged in.
 */
export async function loginOIDC(): Promise<User | null> {
  window.location.href = `${api_url}/login/oidc`;
  return null;
}

/**
 * Navigates to the login page and redirects back to the current page after login.
 */
export function loginFlow() {
  redirect("/login?returnUrl=" + window.location.pathname);
}

/**
 * Logs the user out.
 *
 * @returns true if the user was logged out successfully, false otherwise.
 */
export async function logout() {
  const response = await fetch(`${api_url}/login/logout`, {
    method: "GET",
    credentials: "include",
  });
  return response.ok;
}

/**
 * Logs the user out and redirects to the login page.
 */
export async function logoutFlow() {
  await logout();
  if (!logout) {
    throw new Error("Logout failed");
  }
  redirect("/login?returnUrl=" + window.location.pathname);
}
