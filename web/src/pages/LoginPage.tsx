import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { loginUser } from "../services/authService";
import { useAuth } from "../context/AuthContext";
import "./Auth.css";

export default function LoginPage() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
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
        setError(res.error?.message || "Invalid credentials.");
      }
    } catch (err: any) {
      setError(
        err.response?.data?.error?.message || "Login failed. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      {/* Aquatic background image */}
      <div className="auth-bg" />

      {/* Floating card */}
      <div className="auth-card">
        <div className="auth-brand">AQUAHAVEN</div>
        <h2 className="auth-title">Welcome Back</h2>
        <p className="auth-subtitle">
          Sign in to access your account and shop our collection.
        </p>

        <div className="auth-field">
          <label>EMAIL ADDRESS</label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            placeholder=""
            autoComplete="email"
          />
        </div>

        <div className="auth-field">
          <label>
            PASSWORD
            <Link to="/forgot-password" className="auth-forgot">
              FORGOT PASSWORD?
            </Link>
          </label>
          <input
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            placeholder=""
            autoComplete="current-password"
          />
        </div>

        {error && <div className="auth-error">{error}</div>}

        <button
          className="auth-btn"
          onClick={handleSubmit}
          disabled={loading}
        >
          {loading ? "Signing in..." : "Sign In"}
        </button>

        <div className="auth-divider">
          <span>or</span>
        </div>

        <p className="auth-switch">
          Don't have an account?{" "}
          <Link to="/register" className="auth-link">
            Create one now
          </Link>
        </p>
      </div>
    </div>
  );
}