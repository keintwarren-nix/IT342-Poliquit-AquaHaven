// src/modules/admin/components/AdminLayout.jsx
import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../auth/context/AuthContext.jsx";
import "./AdminLayout.css";

const NAV = [
  { to: "/admin",          label: "Dashboard", icon: "📊", end: true },
  { to: "/admin/products", label: "Products",  icon: "🐠" },
  { to: "/admin/orders",   label: "Orders",    icon: "📋" },
  { to: "/admin/users",    label: "Users",     icon: "👥" },
];

export default function AdminLayout({ children }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => { logout(); navigate("/admin/login"); };

  return (
    <div className="adm-shell">
      <aside className="adm-sidebar">
        <div className="adm-sidebar__brand">
          <span>🐠</span>
          <div>
            <div className="adm-sidebar__brand-name">AquaHaven</div>
            <div className="adm-sidebar__brand-sub">Admin Panel</div>
          </div>
        </div>

        <nav className="adm-sidebar__nav">
          {NAV.map((n) => (
            <NavLink
              key={n.to}
              to={n.to}
              end={n.end}
              className={({ isActive }) =>
                "adm-sidebar__link" + (isActive ? " adm-sidebar__link--active" : "")
              }
            >
              <span className="adm-sidebar__link-icon">{n.icon}</span>
              {n.label}
            </NavLink>
          ))}
        </nav>

        <div className="adm-sidebar__footer">
          <div className="adm-sidebar__user">
            <div className="adm-sidebar__user-avatar">
              {user?.firstname?.[0]}{user?.lastname?.[0]}
            </div>
            <div>
              <div className="adm-sidebar__user-name">
                {user?.firstname} {user?.lastname}
              </div>
              <div className="adm-sidebar__user-role">Administrator</div>
            </div>
          </div>
          <button className="adm-sidebar__logout" onClick={handleLogout}>
            Sign Out
          </button>
        </div>
      </aside>

      <main className="adm-main">
        {children}
      </main>
    </div>
  );
}