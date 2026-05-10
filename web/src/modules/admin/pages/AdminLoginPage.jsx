// src/modules/admin/pages/AdminLoginPage.jsx
import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { loginUser } from "../../auth/api/authApi.js";
import { useAuth } from "../../auth/context/AuthContext.jsx";
import "./AdminAuth.css";

export default function AdminLoginPage() {
  const navigate = useNavigate();
  const { user, login } = useAuth();

  const [form, setForm]       = useState({ email: "", password: "" });
  const [error, setError]     = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    document.title = "AquaHaven | Admin Login";
    // If already logged in as admin, redirect straight to dashboard
    if (user?.role === "ADMIN") navigate("/admin", { replace: true });
  }, [user, navigate]);

  const handleChange = (e) => {
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }));
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
        const userData = res.data.user;
        if (userData.role !== "ADMIN") {
          setError("Access denied. This panel is for administrators only.");
          return;
        }
        login(userData, res.data.accessToken, res.data.refreshToken);
        navigate("/admin", { replace: true });
      } else {
        setError(res.error?.message || "Invalid email or password.");
      }
    } catch (err) {
      setError(err.response?.data?.message || "Invalid email or password.");
    } finally {
      setLoading(false);
    }
  };

  const handleKeyDown = (e) => { if (e.key === "Enter") handleSubmit(); };

  return (
    <div className="adm-auth-page">
      <div className="adm-auth-card">
        <div className="adm-auth-logo">
          <span>🐠</span>
          <div>
            <div className="adm-auth-logo-text">AquaHaven</div>
            <div className="adm-auth-logo-sub">Admin Panel</div>
          </div>
        </div>

        <h2 className="adm-auth-title">Admin Sign In</h2>
        <p className="adm-auth-sub">Sign in with your administrator account.</p>

        <div className="adm-auth-field">
          <label>Email Address</label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            onKeyDown={handleKeyDown}
            placeholder="admin@aquahaven.com"
            autoComplete="email"
          />
        </div>

        <div className="adm-auth-field">
          <label>Password</label>
          <input
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            onKeyDown={handleKeyDown}
            placeholder="••••••••"
            autoComplete="current-password"
          />
        </div>

        {error && <div className="adm-auth-error">{error}</div>}

        <button className="adm-auth-btn" onClick={handleSubmit} disabled={loading}>
          {loading ? "Signing In…" : "Sign In"}
        </button>

        <p className="adm-auth-switch">
          <Link to="/" className="adm-auth-link">← Back to storefront</Link>
        </p>
      </div>
    </div>
  );
}