// src/modules/auth/pages/LoginPage.jsx
import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { loginUser } from "../api/authApi";
import { useAuth } from "../context/AuthContext";
import Navbar from "../../../shared/components/Navbar";
import logo from "../../../assets/logo_aqua.png";
import "./Auth.css";

export default function LoginPage() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [form, setForm] = useState({ email: "", password: "" });
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    document.title = "AquaHaven | Sign In";
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  const handleSubmit = async () => {
    if (!form.email || !form.password) {
      setError("Please fill in all fields.");
      return;
    }
    setLoading(true);
    try {
      const res = await loginUser({ email: form.email, password: form.password });
      if (res.success && res.data) {
        login(res.data.user, res.data.accessToken, res.data.refreshToken);
        navigate("/");
      } else {
        setError(res.error?.message || "Invalid email or password.");
      }
    } catch (err) {
      setError(err.response?.data?.error?.message || "Invalid email or password.");
    } finally {
      setLoading(false);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") handleSubmit();
  };

  return (
    <>
      <Navbar />
      <div className="auth-page">
        <div className="auth-bg" />
        <div className="auth-card">
          <img src={logo} alt="AquaHaven" className="auth-logo" />
          <h2 className="auth-title">Welcome Back</h2>
          <p className="auth-subtitle">Sign in to access your account.</p>

          <div className="auth-field">
            <label>EMAIL ADDRESS</label>
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              onKeyDown={handleKeyDown}
              placeholder="you@email.com"
              autoComplete="email"
            />
          </div>

          <div className="auth-field">
            <label>PASSWORD</label>
            <div className="auth-input-wrap">
              <input
                type={showPassword ? "text" : "password"}
                name="password"
                value={form.password}
                onChange={handleChange}
                onKeyDown={handleKeyDown}
                placeholder="••••••••"
                autoComplete="current-password"
              />
              <button
                type="button"
                className="auth-eye-btn"
                onClick={() => setShowPassword((v) => !v)}
                tabIndex={-1}
                aria-label={showPassword ? "Hide password" : "Show password"}
              >
                {showPassword ? "🙈" : "👁️"}
              </button>
            </div>
          </div>

          {error && <div className="auth-error">{error}</div>}

          <button className="auth-btn" onClick={handleSubmit} disabled={loading}>
            {loading ? "Signing In..." : "Sign In"}
          </button>

          <p className="auth-switch">
            Don't have an account?{" "}
            <Link to="/register" className="auth-link">Create one now</Link>
          </p>
        </div>
      </div>
    </>
  );
}
