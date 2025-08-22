import axios from "axios";
// 쿠키에서 accessToken 추출 함수
function getAccessTokenFromCookie() {
  const match = document.cookie.match(/(?:^|; )accessToken=([^;]*)/);
  return match ? decodeURIComponent(match[1]) : null;
}

const api = axios.create({
  baseURL: "http://localhost:8080", // 필요에 따라 baseURL 수정
  withCredentials: true,
});

// Request interceptor: Authorization 헤더에 accessToken 추가
api.interceptors.request.use(
  (config) => {
    const token = getAccessTokenFromCookie();
    if (token) {
      console.log(token);
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor: 401 에러 시 /auth/refresh로 토큰 갱신
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (
      error.response &&
      error.response.status === 401 &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true;
      try {
        // /auth/refresh로 토큰 갱신 요청
        const { data } = await axios.post(
          "http://localhost:8080/auth/refresh",
          {},
          { withCredentials: true }
        );
        // accessToken이 쿠키에 갱신되었다고 가정하고, 다시 원래 요청 재시도
        const { accessToken, refreshToken } = data;

        if (accessToken && refreshToken) {
          const expiresIn = 60 * 60 * 24; // 1 day
          document.cookie = `accessToken=${accessToken}; max-age=${expiresIn}; path=/`;
          document.cookie = `refreshToken=${refreshToken}; max-age=${expiresIn}; path=/`;
        }

        return api(originalRequest);
      } catch (refreshError) {
        // 갱신 실패 시 로그아웃 등 추가 처리 가능
        alert("토큰 만료로 로그아웃 됩니다.");
        document.cookie = "accessToken=; max-age=0; path=/";
        document.cookie = "refreshToken=; max-age=0; path=/";
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

export default api;
