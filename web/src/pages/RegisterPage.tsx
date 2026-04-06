import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { registerUser } from "../services/authService";
import { useAuth } from "../context/AuthContext";
import Navbar from "../components/Navbar";
import "./Auth.css";
import logo from "../assets/logo_aqua.png";

export default function RegisterPage() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [form, setForm] = useState({
    firstname: "",
    lastname: "",
    email: "",
    phone: "",
    password: "",
    confirmPassword: "",
  });

  const [agreed, setAgreed] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  const handleSubmit = async () => {
    if (!agreed) {
      setError("You must agree to the Terms of Service.");
      return;
    }
    // ... validation and registration logic ...
  };

  return (
    <>
      <Navbar />
      <div className="auth-page">
        <div className="auth-bg register-bg" />
        <div className="auth-card">
          <img src={logo} alt="Logo" className="auth-logo" />
          <h2 className="auth-title">Create Account</h2>

          <div className="auth-grid">
            <div className="auth-field">
              <label>FIRST NAME</label>
              <input type="text" name="firstname" value={form.firstname} onChange={handleChange} />
            </div>
            <div className="auth-field">
              <label>LAST NAME</label>
              <input type="text" name="lastname" value={form.lastname} onChange={handleChange} />
            </div>
          </div>

          <div className="auth-field">
            <label>EMAIL ADDRESS</label>
            <input type="email" name="email" value={form.email} onChange={handleChange} />
          </div>

          <div className="auth-field">
            <label>PASSWORD</label>
            <input type="password" name="password" value={form.password} onChange={handleChange} />
          </div>

          {/* POLISHED TERMS SECTION */}
          <label className="auth-checkbox-container">
            <input
              type="checkbox"
              checked={agreed}
              onChange={(e) => setAgreed(e.target.checked)}
            />
            <span className="checkmark"></span>
            <span className="auth-checkbox-text">
              I agree to the{" "}
              <Link to="/terms" className="auth-link">Terms of Service</Link>
              {" "}and{" "}
              <Link to="/privacy" className="auth-link">Privacy Policy</Link>
            </span>
          </label>

          {error && <div className="auth-error">{error}</div>}

          <button className="auth-btn" onClick={handleSubmit} disabled={loading}>
            {loading ? "Creating Account..." : "Create Account"}
          </button>

          <p className="auth-switch">
            Already have an account? <Link to="/login" className="auth-link">Sign In</Link>
          </p>
        </div>
      </div>
    </>
  );
}