// src/core/http/client.js
import axios from "axios";

const API_BASE = import.meta.env.VITE_API_BASE_URL ?? import.meta.env.VITE_API_URL ?? "http://localhost:8080/api/v1";

const httpClient = axios.create({
  baseURL: API_BASE,
  headers: { "Content-Type": "application/json" },
  withCredentials: true, // must match backend CorsConfig.setAllowCredentials(true)
});

httpClient.interceptors.request.use((config) => {
  const token = sessionStorage.getItem("accessToken");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

httpClient.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error(
      "API Error:",
      error.response?.status,
      error.response?.data,
      error.config?.url
    );
    return Promise.reject(error);
  }
);

export default httpClient;