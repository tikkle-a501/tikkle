// 쿠키에서 특정 값을 가져오는 함수
export const getCookie = (name: string): string | null => {
  const match = document.cookie.match(new RegExp("(^| )" + name + "=([^;]+)"));
  if (match) {
    return decodeURIComponent(match[2]);
  }
  return null;
};

// 쿠키에 값을 설정하는 함수
export const setCookie = (name: string, value: string, days: number) => {
  const expires = new Date(Date.now() + days * 864e5).toUTCString();
  document.cookie = `${name}=${encodeURIComponent(value)}; expires=${expires}; path=/`;
};

// 쿠키에서 특정 값을 삭제하는 함수
export const removeCookie = (name: string) => {
  document.cookie = `${name}=; Max-Age=0; path=/`;
};
