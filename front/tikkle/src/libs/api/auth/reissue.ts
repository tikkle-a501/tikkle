import { handleApiRequest } from "../client";
import {
  AccessToken,
  RefreshToken,
  ApiRequestConfig,
  TokenPair,
} from "@/types";
import { getCookie, setCookie } from "@/libs/utils";

export const reissueToken = async () => {
  const currentAccessToken: AccessToken | null = getCookie("accessToken");
  const refreshToken: RefreshToken | null = getCookie("refreshToken");

  if (!currentAccessToken || !refreshToken) {
    throw new Error("🔑Tokens are missing");
  }

  return handleApiRequest<{ accessToken: AccessToken }, "post", TokenPair>(
    "/reissue",
    "post",
    {
      accessToken: currentAccessToken,
      refreshToken,
    },
    { bypassInterceptor: true } as ApiRequestConfig,
  );
};

export const saveTokens = (tokenPair: TokenPair) => {
  setCookie("accessToken", tokenPair.accessToken, 7);
  setCookie("refreshToken", tokenPair.refreshToken, 7);
};
