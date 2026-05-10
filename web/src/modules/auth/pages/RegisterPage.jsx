// src/modules/auth/pages/RegisterPage.jsx
import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { registerUser } from "../api/authApi";
import { useAuth } from "../context/AuthContext";
import Navbar from "../../../shared/components/Navbar";
import logo from "../../../assets/logo_aqua.png";
import "./Auth.css";

function getPasswordStrength(password) {
  if (!password) return { score: 0, label: "", color: "" };
  let score = 0;
  if (password.length >= 8) score++;
  if (password.length >= 12) score++;
  if (/[A-Z]/.test(password)) score++;
  if (/[0-9]/.test(password)) score++;
  if (/[^A-Za-z0-9]/.test(password)) score++;

  if (score <= 1) return { score, label: "Weak", color: "#EF4444" };
  if (score <= 2) return { score, label: "Fair", color: "#F59E0B" };
  if (score <= 3) return { score, label: "Good", color: "#3B82F6" };
  return { score, label: "Strong", color: "#3D7A56" };
}

export default function RegisterPage() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [form, setForm] = useState({
    firstname: "", lastname: "", email: "",
    phone: "", password: "", confirmPassword: "",
  });
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [agreed, setAgreed] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    document.title = "AquaHaven | Create Account";
  }, []);

  const strength = getPasswordStrength(form.password);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
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
    if (!agreed) {
      setError("You must agree to the Terms of Service.");
      return;
    }

    setLoading(true);
    try {
      const res = await registerUser({
        firstname: form.firstname,
        lastname: form.lastname,
        email: form.email,
        phone: form.phone,
        password: form.password,
      });
      if (res.success && res.data) {
        login(res.data.user, res.data.accessToken, res.data.refreshToken);
        navigate("/");
      } else {
        setError(res.error?.message || "Registration failed. Please try again.");
      }
    } catch (err) {
      setError(err.response?.data?.error?.message || "Registration failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Navbar />
      <div className="auth-page">
        <div className="auth-bg register-bg" />
        <div className="auth-card">
          <img src={logo} alt="AquaHaven" className="auth-logo" />
          <h2 className="auth-title">Create Account</h2>
          <p className="auth-subtitle">Join us to start your aquatic journey.</p>

          <div className="auth-grid">
            <div className="auth-field">
              <label>FIRST NAME</label>
              <input type="text" name="firstname" value={form.firstname} onChange={handleChange} placeholder="Juan" />
            </div>
            <div className="auth-field">
              <label>LAST NAME</label>
              <input type="text" name="lastname" value={form.lastname} onChange={handleChange} placeholder="dela Cruz" />
            </div>
          </div>

          <div className="auth-field">
            <label>EMAIL ADDRESS</label>
            <input type="email" name="email" value={form.email} onChange={handleChange} placeholder="you@email.com" autoComplete="email" />
          </div>

          <div className="auth-field">
            <label>PHONE (optional)</label>
            <input type="tel" name="phone" value={form.phone} onChange={handleChange} placeholder="09XX-XXX-XXXX" />
          </div>

          <div className="auth-field">
            <label>PASSWORD</label>
            <div className="auth-input-wrap">
              <input
                type={showPassword ? "text" : "password"}
                name="password"
                value={form.password}
                onChange={handleChange}
                placeholder="••••••••"
                autoComplete="new-password"
              />
              <button type="button" className="auth-eye-btn" onClick={() => setShowPassword((v) => !v)} tabIndex={-1}>
                {showPassword ? "🙈" : "👁️"}
              </button>
            </div>
            {form.password.length > 0 && (
              <div className="auth-strength">
                <div className="auth-strength__bars">
                  {[1, 2, 3, 4].map((i) => (
                    <div
                      key={i}
                      className="auth-strength__bar"
                      style={{ background: i <= Math.ceil(strength.score / 1.25) ? strength.color : "#E2DDD6" }}
                    />
                  ))}
                </div>
                <span className="auth-strength__label" style={{ color: strength.color }}>{strength.label}</span>
              </div>
            )}
          </div>

          <div className="auth-field">
            <label>CONFIRM PASSWORD</label>
            <div className="auth-input-wrap">
              <input
                type={showConfirm ? "text" : "password"}
                name="confirmPassword"
                value={form.confirmPassword}
                onChange={handleChange}
                placeholder="••••••••"
                autoComplete="new-password"
                style={{
                  borderColor:
                    form.confirmPassword && form.confirmPassword !== form.password ? "#EF4444"
                    : form.confirmPassword && form.confirmPassword === form.password ? "#3D7A56"
                    : undefined,
                }}
              />
              <button type="button" className="auth-eye-btn" onClick={() => setShowConfirm((v) => !v)} tabIndex={-1}>
                {showConfirm ? "🙈" : "👁️"}
              </button>
            </div>
            {form.confirmPassword && form.confirmPassword !== form.password && (
              <span className="auth-field__hint auth-field__hint--error">Passwords do not match</span>
            )}
            {form.confirmPassword && form.confirmPassword === form.password && (
              <span className="auth-field__hint auth-field__hint--ok">✓ Passwords match</span>
            )}
          </div>

          <label className="auth-checkbox-container">
            <input type="checkbox" checked={agreed} onChange={(e) => setAgreed(e.target.checked)} />
            <span className="checkmark" />
            <span className="auth-checkbox-text">
              I agree to the{" "}
              <Link to="/terms" className="auth-link">Terms of Service</Link>{" "}
              and{" "}
              <Link to="/privacy" className="auth-link">Privacy Policy</Link>
            </span>
          </label>

          {error && <div className="auth-error">{error}</div>}

          <button className="auth-btn" onClick={handleSubmit} disabled={loading}>
            {loading ? "Creating Account..." : "Create Account"}
          </button>

          <p className="auth-switch">
            Already have an account?{" "}
            <Link to="/login" className="auth-link">Sign In</Link>
          </p>
        </div>
      </div>
    </>
  );
}
