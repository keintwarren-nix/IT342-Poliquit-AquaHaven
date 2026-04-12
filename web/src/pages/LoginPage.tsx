import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { loginUser } from "../services/authService";
import { useAuth } from "../context/AuthContext";
import Navbar from "../components/Navbar";
import logo from "../assets/logo_aqua.png";
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
      const res = await loginUser(form);
      if (res.success && res.data) {
        login(res.data.user, res.data.accessToken, res.data.refreshToken);
        navigate("/");
      } else {
        setError(res.error?.message || "Invalid credentials.");
      }
    } catch (err: any) {
      setError("Login failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Navbar />
      <div className="auth-page">
        <div className="auth-bg" />
        <div className="auth-card">
          <img src={logo} alt="AquaHaven" className="auth-logo" />
          <h2 className="auth-title">Welcome Back</h2>
          <p className="auth-subtitle">Sign in to access your account and shop our collection.</p>

          <div className="auth-field">
            <label>Email Address</label>
            <input type="email" name="email" value={form.email} onChange={handleChange} />
          </div>

          <div className="auth-field">
            <div className="auth-label-row">
              <label>Password</label>
              <Link to="/forgot" className="auth-forgot">FORGOT PASSWORD?</Link>
            </div>
            <input type="password" name="password" value={form.password} onChange={handleChange} />
          </div>

          {error && <div className="auth-error">{error}</div>}

          <button className="auth-btn" onClick={handleSubmit} disabled={loading}>
            {loading ? "Signing in..." : "Sign In"}
          </button>

          <div className="auth-divider">
            <span>or</span>
          </div>

          <p className="auth-switch">
            Don't have an account? <Link to="/register" className="auth-link">Create one now</Link>
          </p>
        </div>
      </div>
    </>
  );
}