// src/App.jsx
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./modules/auth/context/AuthContext";
import { CartProvider } from "./modules/cart/context/CartContext";

import HomePage             from "./pages/HomePage";
import AboutPage            from "./pages/AboutPage";
import ContactPage          from "./pages/ContactPage";
import LoginPage            from "./modules/auth/pages/LoginPage";
import RegisterPage         from "./modules/auth/pages/RegisterPage";
import ProfilePage          from "./modules/auth/pages/ProfilePage";
import ProductsPage         from "./modules/catalog/pages/ProductsPage";
import ProductDetailPage    from "./modules/catalog/pages/ProductDetailPage";
import CartPage             from "./modules/cart/pages/CartPage";
import CheckoutPage         from "./modules/orders/pages/CheckoutPage";
import OrdersPage           from "./modules/orders/pages/OrdersPage";
import OrderDetailPage      from "./modules/orders/pages/OrderDetailPage";
import OrderConfirmationPage from "./modules/orders/pages/OrderConfirmationPage";

// ── Admin ──────────────────────────────────────────────────────────────────
import AdminLoginPage    from "./modules/admin/pages/AdminLoginPage";
import AdminDashboard    from "./modules/admin/pages/AdminDashboard";
import AdminProductsPage from "./modules/admin/pages/AdminProductsPage";
import AdminOrdersPage   from "./modules/admin/pages/AdminOrdersPage";
import AdminUsersPage    from "./modules/admin/pages/AdminUsersPage";

function PrivateRoute({ children }) {
  const { user } = useAuth();
  return user ? children : <Navigate to="/login" replace />;
}

function GuestRoute({ children }) {
  const { user } = useAuth();
  return !user ? children : <Navigate to="/" replace />;
}

// Redirects non-admins to /admin/login
function AdminRoute({ children }) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/admin/login" replace />;
  if (user.role !== "ADMIN") return <Navigate to="/admin/login" replace />;
  return children;
}

function AppRoutes() {
  return (
    <Routes>
      {/* ── Public ── */}
      <Route path="/"             element={<HomePage />} />
      <Route path="/about"        element={<AboutPage />} />
      <Route path="/contact"      element={<ContactPage />} />
      <Route path="/products"     element={<ProductsPage />} />
      <Route path="/products/:id" element={<ProductDetailPage />} />

      {/* ── Auth ── */}
      <Route path="/login"    element={<GuestRoute><LoginPage /></GuestRoute>} />
      <Route path="/register" element={<GuestRoute><RegisterPage /></GuestRoute>} />

      {/* ── Customer protected ── */}
      <Route path="/profile"       element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
      <Route path="/cart"          element={<PrivateRoute><CartPage /></PrivateRoute>} />
      <Route path="/checkout"      element={<PrivateRoute><CheckoutPage /></PrivateRoute>} />
      <Route path="/orders"        element={<PrivateRoute><OrdersPage /></PrivateRoute>} />
      <Route path="/orders/:ref"   element={<PrivateRoute><OrderDetailPage /></PrivateRoute>} />
      <Route path="/order-confirmation/:ref" element={<PrivateRoute><OrderConfirmationPage /></PrivateRoute>} />

      {/* ── Admin ── */}
      <Route path="/admin/login"    element={<AdminLoginPage />} />
      <Route path="/admin"          element={<AdminRoute><AdminDashboard /></AdminRoute>} />
      <Route path="/admin/products" element={<AdminRoute><AdminProductsPage /></AdminRoute>} />
      <Route path="/admin/orders"   element={<AdminRoute><AdminOrdersPage /></AdminRoute>} />
      <Route path="/admin/users"    element={<AdminRoute><AdminUsersPage /></AdminRoute>} />

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <CartProvider>
          <AppRoutes />
        </CartProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}