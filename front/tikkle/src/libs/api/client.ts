import axios from "axios";
import {
  ApiErrorResponse,
  ApiRequestConfig,
  ApiResponse,
  ConditionalData,
  HttpMethod,
} from "@/types";

// // axios 기본 인스턴스를 생성하고, baseURL을 설정
// const api = axios.create({
//   baseURL: "http://localhost:8080/api/v1", // 기본적으로 localhost:8080으로 요청을 보냄
//   withCredentials: true, // 쿠키 인증이 필요한 경우에만 사용
// });

// export default api;

const api = axios.create({
  baseURL: "http://localhost:8080/api/v1",
  withCredentials: true,
});

export const handleApiRequest = async <T, M extends HttpMethod, D = undefined>(
  url: string,
  method: M,
  data?: ConditionalData<M, D>,
  config?: ApiRequestConfig,
): Promise<T> => {
  try {
    const response = await api.request<ApiResponse<T>>({
      url,
      method,
      data,
      ...config,
    });
    return response.data.data;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      const apiError: ApiErrorResponse = error.response.data;
      const apiErrorMessage = apiError.message
        ? apiError.message
        : error.message;
      console.error(
        "🚨 Error making API request to %c%s%c 🚨\n\n Error: %c%s%c",
        "color: black; background-color: yellow; font-weight: bold;",
        url,
        "",
        "color: white; background-color: red; font-weight: bold;",
        apiErrorMessage,
        "",
      );
      throw new Error(apiErrorMessage);
    }
    console.error("🚨 Unexpected error making API request 🚨\n", error);
    throw error;
  }
};

export default api;
