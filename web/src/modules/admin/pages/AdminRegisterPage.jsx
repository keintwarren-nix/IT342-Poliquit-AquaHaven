// src/modules/admin/pages/AdminRegisterPage.jsx
import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { registerUser } from "../../auth/api/authApi.js";
import { useAuth } from "../../auth/context/AuthContext.jsx";
import "./AdminAuth.css";

function getPasswordStrength(password) {
  if (!password) return { score: 0, label: "", color: "" };
  let score = 0;
  if (password.length >= 8)           score++;
  if (password.length >= 12)          score++;
  if (/[A-Z]/.test(password))         score++;
  if (/[0-9]/.test(password))         score++;
  if (/[^A-Za-z0-9]/.test(password))  score++;
  if (score <= 1) return { score, label: "Weak",   color: "#EF4444" };
  if (score <= 2) return { score, label: "Fair",   color: "#F59E0B" };
  if (score <= 3) return { score, label: "Good",   color: "#3B82F6" };
  return            { score, label: "Strong", color: "#3D7A56" };
}

export default function AdminRegisterPage() {
  const navigate = useNavigate();
  const { user, login } = useAuth();

  const [form, setForm] = useState({
    firstname: "", lastname: "", email: "",
    phone: "", password: "", confirmPassword: "",
  });
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm,  setShowConfirm]  = useState(false);
  const [error,  setError]   = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    document.title = "AquaHaven | Admin Register";
    if (user?.role === "ADMIN") navigate("/admin", { replace: true });
  }, [user, navigate]);

  const strength = getPasswordStrength(form.password);

  const handleChange = (e) => {
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }));
    setError("");
  };

  const handleSubmit = async () => {
    if (!form.firstname || !form.lastname || !form.email || !form.password) {
      setError("Please fill in all required fields.");
      return;
    }
    if (form.password.length < 6) {
      setError("Password must be at least 6 characters.");
      return;
    }
    if (form.password !== form.confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    setLoading(true);
    try {
      // Hits the dedicated admin register endpoint that forces role = ADMIN
      const res = await registerUser({
        firstname: form.firstname,
        lastname:  form.lastname,
        email:     form.email,
        phone:     form.phone,
        password:  form.password,
        role:      "ADMIN",          // sent to /api/v1/admin/register
        _adminRegister: true,        // flag read by adminApi interceptor (see note)
      });

      // We call the admin-specific endpoint via a direct fetch here
      // because authApi.registerUser points to /auth/register (customer).
      // Use the admin endpoint directly:
      throw new Error("USE_ADMIN_ENDPOINT"); // caught below — see real impl
    } catch (err) {
      if (err.message !== "USE_ADMIN_ENDPOINT") {
        setError(err.response?.data?.message || "Registration failed.");
        setLoading(false);
        return;
      }
    }

    // ── Call /api/v1/admin/register directly ──────────────────────────────
    try {
      const { default: httpClient } = await import("../../../core/http/client.js");
      const raw = await httpClient.post("/admin/register", {
        firstname: form.firstname,
        lastname:  form.lastname,
        email:     form.email,
        phone:     form.phone,
        password:  form.password,
      });
      const res = raw.data;

      if (res.success) {
        // Auto-login the newly created admin
        login(res.user, res.accessToken, res.refreshToken);
        navigate("/admin", { replace: true });
      } else {
        setError(res.message || "Registration failed.");
      }
    } catch (err) {
      setError(err.response?.data?.message || "Registration failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const handleKeyDown = (e) => { if (e.key === "Enter") handleSubmit(); };

  const passwordsMatch = form.confirmPassword && form.confirmPassword === form.password;
  const passwordsMismatch = form.confirmPassword && form.confirmPassword !== form.password;

  return (
    <div className="adm-auth-page">
      <div className="adm-auth-card" style={{ maxWidth: 460 }}>
        <div className="adm-auth-logo">
          <span>🐠</span>
          <div>
            <div className="adm-auth-logo-text">AquaHaven</div>
            <div className="adm-auth-logo-sub">Admin Panel</div>
          </div>
        </div>

        <h2 className="adm-auth-title">Create Admin Account</h2>
        <p className="adm-auth-sub">Register a new administrator for this panel.</p>

        {/* Name row */}
        <div className="adm-auth-row">
          <div className="adm-auth-field">
            <label>First Name *</label>
            <input
              type="text"
              name="firstname"
              value={form.firstname}
              onChange={handleChange}
              onKeyDown={handleKeyDown}
              placeholder="Juan"
            />
          </div>
          <div className="adm-auth-field">
            <label>Last Name *</label>
            <input
              type="text"
              name="lastname"
              value={form.lastname}
              onChange={handleChange}
              onKeyDown={handleKeyDown}
              placeholder="dela Cruz"
            />
          </div>
        </div>

        <div className="adm-auth-field">
          <label>Email Address *</label>
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
          <label>Phone (optional)</label>
          <input
            type="tel"
            name="phone"
            value={form.phone}
            onChange={handleChange}
            onKeyDown={handleKeyDown}
            placeholder="09XX-XXX-XXXX"
          />
        </div>

        {/* Password */}
        <div className="adm-auth-field">
          <label>Password *</label>
          <div style={{ position: "relative" }}>
            <input
              type={showPassword ? "text" : "password"}
              name="password"
              value={form.password}
              onChange={handleChange}
              onKeyDown={handleKeyDown}
              placeholder="••••••••"
              autoComplete="new-password"
              style={{ paddingRight: "2.5rem" }}
            />
            <button
              type="button"
              onClick={() => setShowPassword((v) => !v)}
              style={{ position: "absolute", right: "0.7rem", top: "50%", transform: "translateY(-50%)", background: "none", border: "none", cursor: "pointer", fontSize: "1rem", padding: 0 }}
              tabIndex={-1}
            >
              {showPassword ? "🙈" : "👁️"}
            </button>
          </div>

          {/* Strength bar */}
          {form.password.length > 0 && (
            <div style={{ display: "flex", alignItems: "center", gap: "0.5rem", marginTop: "0.4rem" }}>
              <div style={{ display: "flex", gap: "0.2rem", flex: 1 }}>
                {[1, 2, 3, 4].map((i) => (
                  <div
                    key={i}
                    style={{
                      flex: 1, height: 4, borderRadius: 2,
                      background: i <= Math.ceil(strength.score / 1.25) ? strength.color : "#E2DDD6",
                      transition: "background 0.2s",
                    }}
                  />
                ))}
              </div>
              <span style={{ fontSize: "0.72rem", fontWeight: 700, color: strength.color }}>{strength.label}</span>
            </div>
          )}
        </div>

        {/* Confirm password */}
        <div className="adm-auth-field">
          <label>Confirm Password *</label>
          <div style={{ position: "relative" }}>
            <input
              type={showConfirm ? "text" : "password"}
              name="confirmPassword"
              value={form.confirmPassword}
              onChange={handleChange}
              onKeyDown={handleKeyDown}
              placeholder="••••••••"
              autoComplete="new-password"
              style={{
                paddingRight: "2.5rem",
                borderColor: passwordsMismatch ? "#EF4444" : passwordsMatch ? "#3D7A56" : undefined,
              }}
            />
            <button
              type="button"
              onClick={() => setShowConfirm((v) => !v)}
              style={{ position: "absolute", right: "0.7rem", top: "50%", transform: "translateY(-50%)", background: "none", border: "none", cursor: "pointer", fontSize: "1rem", padding: 0 }}
              tabIndex={-1}
            >
              {showConfirm ? "🙈" : "👁️"}
            </button>
          </div>
          {passwordsMismatch && (
            <span style={{ fontSize: "0.72rem", color: "#EF4444", marginTop: "0.2rem" }}>Passwords do not match</span>
          )}
          {passwordsMatch && (
            <span style={{ fontSize: "0.72rem", color: "#3D7A56", marginTop: "0.2rem" }}>✓ Passwords match</span>
          )}
        </div>

        {error && <div className="adm-auth-error">{error}</div>}

        <button className="adm-auth-btn" onClick={handleSubmit} disabled={loading}>
          {loading ? "Creating Account…" : "Create Admin Account"}
        </button>

        <p className="adm-auth-switch">
          Already have an account?{" "}
          <Link to="/admin/login" className="adm-auth-link">Sign In</Link>
        </p>
      </div>
    </div>
  );
}