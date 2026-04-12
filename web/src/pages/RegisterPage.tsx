import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { registerUser } from "../services/authService";
import { useAuth } from "../context/AuthContext";
import Navbar from "../components/Navbar";
import logo from "../assets/logo_aqua.png";
import "./Auth.css";

export default function RegisterPage() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [form, setForm] = useState({
    firstname: "", lastname: "", email: "", phone: "", password: "", confirmPassword: ""
  });
  const [agreed, setAgreed] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => { document.title = "AquaHaven | Create Account"; }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  const handleSubmit = async () => {
    if (form.password !== form.confirmPassword) return setError("Passwords do not match.");
    if (!agreed) return setError("You must agree to the Terms.");

    setLoading(true);
    try {
      const res = await registerUser(form);
      if (res.success && res.data) {
        login(res.data.user, res.data.accessToken, res.data.refreshToken);
        navigate("/");
      } else {
        setError(res.error?.message || "Registration failed.");
      }
    } catch (err) {
      setError("Registration failed.");
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
          <img src={logo} alt="Logo" className="auth-logo" />
          <h2 className="auth-title">Create Account</h2>
          <p className="auth-subtitle">Join us to start your collection.</p>

          <div className="auth-grid">
            <div className="auth-field">
              <label>First Name</label>
              <input type="text" name="firstname" value={form.firstname} onChange={handleChange} />
            </div>
            <div className="auth-field">
              <label>Last Name</label>
              <input type="text" name="lastname" value={form.lastname} onChange={handleChange} />
            </div>
          </div>

          <div className="auth-field">
            <label>Email Address</label>
            <input type="email" name="email" value={form.email} onChange={handleChange} />
          </div>

          <div className="auth-field">
            <label>Password</label>
            <input type="password" name="password" value={form.password} onChange={handleChange} />
          </div>

          <label className="auth-checkbox-container">
            <input type="checkbox" checked={agreed} onChange={(e) => setAgreed(e.target.checked)} />
            <span className="auth-checkbox-text">
              I agree to the <Link to="/terms" className="auth-link">Terms</Link> and <Link to="/privacy" className="auth-link">Privacy</Link>
            </span>
          </label>

          {error && <div className="auth-error">{error}</div>}

          <button className="auth-btn" onClick={handleSubmit} disabled={loading}>
            {loading ? "Creating Account..." : "Create Account"}
          </button>

          <div className="auth-divider">
            <span>or</span>
          </div>

          <p className="auth-switch">
            Already have an account? <Link to="/login" className="auth-link">Sign In</Link>
          </p>
        </div>
      </div>
    </>
  );
}