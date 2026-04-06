import Navbar from "../components/Navbar";
import { useAuth } from "../context/AuthContext";
import "./HomePage.css";

export default function HomePage() {
  const { user, logout } = useAuth();

  return (
    <>
      <Navbar />

      <div className="home-page">
        <div className="home-content">
          <h1>Welcome, {user?.firstname}!</h1>
          <p>Your aquatic journey starts here.</p>

          <button
            onClick={logout}
            style={{
              marginTop: "2rem",
              padding: "0.8rem 2.5rem",
              background: "#1a1a1a",
              color: "#fff",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
              fontFamily: "'DM Sans', sans-serif",
              fontSize: "0.9rem",
              fontWeight: 500,
              transition: "opacity 0.2s ease"
            }}
            onMouseOver={(e) => (e.currentTarget.style.opacity = "0.8")}
            onMouseOut={(e) => (e.currentTarget.style.opacity = "1")}
          >
            Logout
          </button>
        </div>
      </div>
    </>
  );
}