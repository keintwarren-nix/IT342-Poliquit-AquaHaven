// src/modules/auth/context/AuthContext.jsx
import { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const stored = sessionStorage.getItem("user");
    return stored ? JSON.parse(stored) : null;
  });

  const [token, setToken] = useState(() =>
    sessionStorage.getItem("accessToken")
  );

  const login = (userData, accessToken, refreshToken) => {
    setUser(userData);
    setToken(accessToken);
    sessionStorage.setItem("accessToken", accessToken);
    sessionStorage.setItem("refreshToken", refreshToken);
    sessionStorage.setItem("user", JSON.stringify(userData));
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    sessionStorage.clear();
  };

  return (
    <AuthContext.Provider
      value={{ user, token, login, logout, isAuthenticated: !!token }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside AuthProvider");
  return ctx;
}
