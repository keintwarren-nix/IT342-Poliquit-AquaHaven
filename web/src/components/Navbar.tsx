import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useCart } from "../context/CartContext";
import "./Navbar.css";
import logo from "../assets/logo_aqua.png";

export default function Navbar() {
  const { user, logout } = useAuth();
  const { count } = useCart();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <header className="navbar">
      <div className="navbar-left">
        <Link to="/" className="navbar-brand">
          <img src={logo} alt="AquaHaven" className="navbar-logo" />
          <span className="navbar-title">AQUAHAVEN</span>
        </Link>
      </div>

      <nav className="navbar-links">
        {user ? (
          <>
            <Link to="/products" className="nav-link">🔍 Search</Link>

            <Link to="/cart" className="nav-link nav-link--cart">
              🛒 My Cart
              {count > 0 && <span className="cart-badge">{count}</span>}
            </Link>

            <Link to="/orders" className="nav-link">📋 My Orders</Link>

            <Link to="/about" className="nav-link">About</Link>

            <button className="nav-logout-btn" onClick={handleLogout}>
              Logout
            </button>
          </>
        ) : (
          <>
            <Link to="/products" className="nav-link">Shop</Link>
            <Link to="/about" className="nav-link">About</Link>
            <Link to="/login" className="nav-link">Sign In</Link>
            <Link to="/register" className="nav-link nav-link--register">
              Get Started
            </Link>
          </>
        )}
      </nav>
    </header>
  );
}