import { handleApiRequest } from "../client";
import { ApiRequestConfig, LoginCredentials, TokenPair } from "@/types";

export const loginUser = async (credentials: LoginCredentials) => {
  return handleApiRequest<TokenPair, "post", LoginCredentials>(
    "/login/mattermost",
    "post",
    credentials,
    {
      bypassInterceptor: true,
    } as ApiRequestConfig,
  );
};

export const loginWithOauth = async (code: string) => {
  return handleApiRequest<TokenPair, "get">(
    "/login/callback",
    "get",
    undefined,
    {
      bypassInterceptor: true,
    } as ApiRequestConfig,
  );
};
