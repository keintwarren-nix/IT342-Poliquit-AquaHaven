// src/App.tsx
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";
import { CartProvider } from "./context/CartContext";
import { ReactNode } from "react";

import HomePage          from "./pages/Homepage";
import LoginPage         from "./pages/LoginPage";
import RegisterPage      from "./pages/RegisterPage";
import ProductsPage      from "./pages/ProductsPage";
import ProductDetailPage from "./pages/ProductDetailPage";
import CartPage          from "./pages/CartPage";
import CheckoutPage      from "./pages/CheckoutPage";
import OrdersPage        from "./pages/OrdersPage";
import OrderDetailPage   from "./pages/OrderDetailPage";
import AboutPage         from "./pages/AboutPage";

// Guard — redirect to /login if not authenticated
function PrivateRoute({ children }: { children: ReactNode }) {
  const { user } = useAuth();
  return user ? <>{children}</> : <Navigate to="/login" replace />;
}

// Guard — redirect to / if already logged in
function GuestRoute({ children }: { children: ReactNode }) {
  const { user } = useAuth();
  return !user ? <>{children}</> : <Navigate to="/" replace />;
}

function AppRoutes() {
  return (
    <Routes>
      {/* ── Public ──────────────────────────────────────────────────── */}
      <Route path="/"                element={<HomePage />} />
      <Route path="/about"           element={<AboutPage />} />
      <Route path="/products"        element={<ProductsPage />} />
      <Route path="/products/:id"    element={<ProductDetailPage />} />

      {/* ── Guest only ──────────────────────────────────────────────── */}
      <Route path="/login"    element={<GuestRoute><LoginPage /></GuestRoute>} />
      <Route path="/register" element={<GuestRoute><RegisterPage /></GuestRoute>} />

      {/* ── Auth required ───────────────────────────────────────────── */}
      <Route path="/cart"            element={<PrivateRoute><CartPage /></PrivateRoute>} />
      <Route path="/checkout"        element={<PrivateRoute><CheckoutPage /></PrivateRoute>} />
      <Route path="/orders"          element={<PrivateRoute><OrdersPage /></PrivateRoute>} />
      <Route path="/orders/:ref"     element={<PrivateRoute><OrderDetailPage /></PrivateRoute>} />

      {/* ── Fallback ────────────────────────────────────────────────── */}
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