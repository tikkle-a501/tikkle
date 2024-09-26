export type AccessToken = string;
export type RefreshToken = string;

export interface TokenPair {
  accessToken: AccessToken;
  refreshToken: RefreshToken;
}

export interface LoginCredentials {
  loginId: string;
  password: string;
}
