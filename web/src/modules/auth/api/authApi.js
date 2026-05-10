// src/modules/auth/api/authApi.js
import httpClient from "../../../core/http/client";

/**
 * Backend AuthResponse shape (flat — no nested "data" wrapper):
 * {
 *   success: boolean,
 *   user: { email, firstname, lastname, role },
 *   accessToken: string,
 *   refreshToken: string,
 *   message: string,      // present on failure
 *   errorCode: string,    // present on failure
 * }
 */

export async function registerUser(payload) {
  try {
    const res = await httpClient.post("/auth/register", payload);
    const raw = res.data;

    if (raw.success) {
      return {
        success: true,
        data: {
          user: raw.user,
          accessToken: raw.accessToken,
          refreshToken: raw.refreshToken,
        },
        error: null,
      };
    }

    // Backend returned success:false (e.g. email already registered)
    return {
      success: false,
      data: null,
      error: { message: raw.message || "Registration failed. Please try again." },
    };
  } catch (err) {
    const raw = err.response?.data;
    return {
      success: false,
      data: null,
      error: {
        message:
          raw?.message ||
          raw?.error?.message ||
          "Registration failed. Please try again.",
      },
    };
  }
}

export async function loginUser(payload) {
  try {
    const res = await httpClient.post("/auth/login", payload);
    const raw = res.data;

    if (raw.success) {
      return {
        success: true,
        data: {
          user: raw.user,
          accessToken: raw.accessToken,
          refreshToken: raw.refreshToken,
        },
        error: null,
      };
    }

    // Backend returned success:false (e.g. invalid credentials)
    return {
      success: false,
      data: null,
      error: { message: raw.message || "Invalid email or password." },
    };
  } catch (err) {
    const raw = err.response?.data;
    return {
      success: false,
      data: null,
      error: {
        message:
          raw?.message ||
          raw?.error?.message ||
          "Invalid email or password.",
      },
    };
  }
}