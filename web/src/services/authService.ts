import axios from "axios";

const API_BASE = "http://localhost:8080/api/v1";

const api = axios.create({
  baseURL: API_BASE,
  headers: { "Content-Type": "application/json" },
});

export interface RegisterPayload {
  firstname: string;
  lastname: string;
  email: string;
  phone: string;
  password: string;
}

export interface LoginPayload {
  email: string;
  password: string;
}

export interface AuthResponse {
  success: boolean;
  data: {
    user: {
      email: string;
      firstname: string;
      lastname: string;
      role: string;
    };
    accessToken: string;
    refreshToken: string;
  };
  error: {
    code: string;
    message: string;
    details: any;
  } | null;
  timestamp: string;
}

export const registerUser = async (payload: RegisterPayload): Promise<AuthResponse> => {
  const response = await api.post<AuthResponse>("/auth/register", payload);
  return response.data;
};

export const loginUser = async (payload: LoginPayload): Promise<AuthResponse> => {
  const response = await api.post<AuthResponse>("/auth/login", payload);
  return response.data;
};