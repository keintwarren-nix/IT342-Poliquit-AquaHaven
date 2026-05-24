import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../modules/auth/context/AuthContext.jsx";
import { useCart } from "../../modules/cart/context/CartContext.jsx";
import logoAqua from "../../assets/logo_aqua.png";
import "./Navbar.css";

export default function Navbar() {
  const { user, logout } = useAuth();
  const { count } = useCart();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate("/");
    setMenuOpen(false);
  };

  return (
    <nav className="navbar">
      <div className="navbar__inner">
        <Link to="/" className="navbar__brand">
          <img
            src={logoAqua}
            alt="AquaHaven Logo"
            className="navbar__logo"
          />
          <span>AquaHaven</span>
        </Link>

        <div className="navbar__links">
          <Link to="/products" className="navbar__link">
            Shop
          </Link>

          <Link to="/about" className="navbar__link">
            About
          </Link>

          <Link to="/contact" className="navbar__link">
            Contact
          </Link>
        </div>

        <div className="navbar__actions">
          {user && (
            <Link
              to="/cart"
              className="navbar__cart-btn"
              aria-label="Cart"
            >
              🛒
              {count > 0 && (
                <span className="navbar__cart-badge">
                  {count}
                </span>
              )}
            </Link>
          )}

          {user ? (
            <div className="navbar__user-menu">
              <button
                className="navbar__user-btn"
                onClick={() => setMenuOpen((v) => !v)}
              >
                {user.firstname} ▾
              </button>

              {menuOpen && (
                <div className="navbar__dropdown">
                  <Link
                    to="/profile"
                    className="navbar__dropdown-item"
                    onClick={() => setMenuOpen(false)}
                  >
                    👤 My Profile
                  </Link>
                  <Link
                    to="/orders"
                    className="navbar__dropdown-item"
                    onClick={() => setMenuOpen(false)}
                  >
                    📋 My Orders
                  </Link>
                  {user.role === "ADMIN" && (
                    <Link
                      to="/admin"
                      className="navbar__dropdown-item"
                      onClick={() => setMenuOpen(false)}
                    >
                      ⚙️ Admin Panel
                    </Link>
                  )}

                  <button
                    className="navbar__dropdown-item navbar__dropdown-item--logout"
                    onClick={handleLogout}
                  >
                    Sign Out
                  </button>
                </div>
              )}
            </div>
          ) : (
            <div className="navbar__auth">
              <Link to="/login" className="navbar__link">
                Sign In
              </Link>

              <Link to="/register" className="navbar__btn">
                Get Started
              </Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}