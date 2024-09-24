import axios from "axios";

// axios 기본 인스턴스를 생성하고, baseURL을 설정
const api = axios.create({
  baseURL: "http://localhost:8080/api/v1", // 기본적으로 localhost:8080으로 요청을 보냄
  withCredentials: true, // 쿠키 인증이 필요한 경우에만 사용
});

export default api;
