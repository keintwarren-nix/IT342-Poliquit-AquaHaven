import { Link } from "react-router-dom";
import "./Navbar.css";
import logo from "../assets/logo_aqua.png";

export default function Navbar() {
  return (
    <header className="navbar">
      <div className="navbar-left">
        <img src={logo} alt="AquaHaven" className="navbar-logo" />
        <span className="navbar-title">AQUAHAVEN</span>
      </div>

      <nav className="navbar-links">
        <Link to="/" className="nav-link">Home</Link>
        <Link to="/about" className="nav-link">About</Link>
        <Link to="/contact" className="nav-link">Contact</Link>
        <span className="nav-home-icon">⌂</span>
      </nav>
    </header>
  );
}