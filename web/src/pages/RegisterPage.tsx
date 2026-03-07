import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { registerUser } from "../services/authService";
import { useAuth } from "../context/AuthContext";
import "./Auth.css";

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

  const validate = () => {
    if (!form.firstname || !form.lastname || !form.email || !form.password) {
      return "Please fill in all required fields.";
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
      return "Please enter a valid email address.";
    }
    if (form.password.length < 8 || !/\d/.test(form.password)) {
      return "Password must be at least 8 characters with at least one number.";
    }
    if (form.password !== form.confirmPassword) {
      return "Passwords do not match.";
    }
    if (!agreed) {
      return "You must agree to the Terms of Service and Privacy Policy.";
    }
    return null;
  };

  const handleSubmit = async () => {
    const validationError = validate();
    if (validationError) {
      setError(validationError);
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
        setError(res.error?.message || "Registration failed.");
      }
    } catch (err: any) {
      setError(
        err.response?.data?.error?.message ||
          "Registration failed. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page register-page">
      <div className="auth-bg register-bg" />

      <div className="auth-card register-card">
        <div className="auth-brand">AQUAHAVEN</div>
        <h2 className="auth-title">Create Account</h2>
        <p className="auth-subtitle">
          Join thousands of aquarists building their dream setups.
        </p>
        <div className="auth-divider-line" />

        <div className="auth-row">
          <div className="auth-field">
            <label>FIRST NAME</label>
            <input
              type="text"
              name="firstname"
              value={form.firstname}
              onChange={handleChange}
            />
          </div>
          <div className="auth-field">
            <label>LAST NAME</label>
            <input
              type="text"
              name="lastname"
              value={form.lastname}
              onChange={handleChange}
            />
          </div>
        </div>

        <div className="auth-field">
          <label>EMAIL ADDRESS</label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            autoComplete="email"
          />
        </div>

        <div className="auth-field">
          <label>PHONE NUMBER</label>
          <input
            type="tel"
            name="phone"
            value={form.phone}
            onChange={handleChange}
          />
        </div>

        <div className="auth-field">
          <label>PASSWORD</label>
          <input
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            autoComplete="new-password"
          />
          <span className="auth-hint">
            Minimum 8 characters with at least one number
          </span>
        </div>

        <div className="auth-field">
          <label>CONFIRM PASSWORD</label>
          <input
            type="password"
            name="confirmPassword"
            value={form.confirmPassword}
            onChange={handleChange}
            autoComplete="new-password"
          />
        </div>

        <label className="auth-checkbox">
          <input
            type="checkbox"
            checked={agreed}
            onChange={(e) => setAgreed(e.target.checked)}
          />
          I agree to the{" "}
          <Link to="/terms" className="auth-link">
            Terms of Service
          </Link>{" "}
          and{" "}
          <Link to="/privacy" className="auth-link">
            Privacy Policy
          </Link>
        </label>

        {error && <div className="auth-error">{error}</div>}

        <button
          className="auth-btn"
          onClick={handleSubmit}
          disabled={loading}
        >
          {loading ? "Creating Account..." : "Create Account"}
        </button>

        <p className="auth-switch">
          Already have an account?{" "}
          <Link to="/login" className="auth-link">
            Sign In
          </Link>
        </p>
      </div>
    </div>
  );
}