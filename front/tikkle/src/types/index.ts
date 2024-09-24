// HttpMethod: API 요청의 HTTP 메서드 타입 정의 (GET, POST, PUT, DELETE 등)
export type HttpMethod = "get" | "post" | "put" | "delete" | "patch";

// ApiResponse: API 응답 형식 정의 (제네릭을 사용하여 다양한 타입의 데이터를 처리)
export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string; // 선택적 메시지, 성공 또는 정보성 메시지로 사용 가능
}

// ApiErrorResponse: API 에러 응답 형식 정의
export interface ApiErrorResponse {
  success: false;
  message: string; // 에러 메시지
  statusCode?: number; // 선택적 HTTP 상태 코드 (예: 400, 404, 500)
  details?: any; // 추가적인 에러 세부 사항 (예: 오류 필드 등)
}

// ApiRequestConfig: Axios 요청 설정을 확장할 수 있는 타입 정의
import { AxiosRequestConfig } from "axios"; // axios의 기본 설정 타입 사용

export interface ApiRequestConfig extends AxiosRequestConfig {
  retry?: boolean; // 선택적 재시도 옵션 (필요시 재요청)
  timeout?: number; // 선택적 타임아웃 설정
}

// ConditionalData: 특정 HTTP 메서드에 따라 요청 데이터 타입을 정의하는 타입
export type ConditionalData<M extends HttpMethod, D> = M extends
  | "get"
  | "delete"
  ? undefined // GET, DELETE 요청은 보통 데이터가 없음
  : D; // POST, PUT, PATCH 요청은 데이터가 필요
