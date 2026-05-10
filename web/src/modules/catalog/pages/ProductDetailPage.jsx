// src/modules/catalog/pages/ProductDetailPage.jsx
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Navbar from "../../../shared/components/Navbar.jsx";
import { fetchProduct } from "../api/catalogApi.js";
import { proxyImage } from "../../../shared/utils/imageProxy.js";
import { formatPHP } from "../../../shared/utils/format.js";
import { useCart } from "../../cart/context/CartContext.jsx";
import { useAuth } from "../../auth/context/AuthContext.jsx";
import "./ProductDetailPage.css";

const WATER_BADGE = {
  freshwater: { label: "Freshwater", color: "#1d6a3e", bg: "#d1fae5" },
  saltwater:  { label: "Saltwater",  color: "#1a4f7a", bg: "#dbeafe" },
  brackish:   { label: "Brackish",   color: "#5a3e1b", bg: "#fef3c7" },
};

export default function ProductDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { addItem } = useCart();
  const { user } = useAuth();

  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [qty, setQty] = useState(1);
  const [added, setAdded] = useState(false);
  const [imgError, setImgError] = useState(false);
  const [showGate, setShowGate] = useState(false);

  useEffect(() => {
    if (!id) { setError("Invalid product."); setLoading(false); return; }
    setLoading(true);
    setError("");
    setImgError(false);
    fetchProduct(Number(id))
      .then((p) => { setProduct(p); document.title = `AquaHaven | ${p.name}`; })
      .catch(() => setError("Product not found."))
      .finally(() => setLoading(false));
  }, [id]);

  const handleAddToCart = () => {
    if (!user) { setShowGate(true); return; }
    if (!product) return;
    addItem(product, qty);
    setAdded(true);
    setTimeout(() => setAdded(false), 2000);
  };

  const badge = product?.waterType ? WATER_BADGE[product.waterType] : null;

  if (loading) return (
    <>
      <Navbar />
      <div className="pdp-state"><div className="pdp-spinner" /><p>Loading product…</p></div>
    </>
  );

  if (error || !product) return (
    <>
      <Navbar />
      <div className="pdp-state pdp-state--error">
        <span>😕</span><p>{error || "Product not found."}</p>
        <button onClick={() => navigate("/products")}>← Back to Shop</button>
      </div>
    </>
  );

  return (
    <>
      <Navbar />
      <div className="pdp" style={{ marginTop: "var(--navbar-h)" }}>
        <div className="pdp-container">

          <nav className="pdp-breadcrumb">
            <button onClick={() => navigate("/products")}>Shop</button>
            <span>/</span>
            <button onClick={() => navigate(`/products?category=${product.category.slug}`)}>
              {product.category.icon} {product.category.name}
            </button>
            <span>/</span>
            <span>{product.name}</span>
          </nav>

          <div className="pdp-layout">
            <div className="pdp-img-panel">
              {product.imageUrl && !imgError ? (
                <img src={proxyImage(product.imageUrl)} alt={product.name} className="pdp-img" onError={() => setImgError(true)} />
              ) : (
                <div className="pdp-img-placeholder"><span>{product.category.icon}</span></div>
              )}
              {badge && (
                <div className="pdp-water-badge" style={{ color: badge.color, background: badge.bg }}>{badge.label}</div>
              )}
            </div>

            <div className="pdp-info">
              <span className="pdp-cat-chip">{product.category.icon} {product.category.name}</span>
              <h1 className="pdp-name">{product.name}</h1>
              <p className="pdp-desc">{product.description}</p>

              <div className="pdp-price-row">
                <span className="pdp-price">{formatPHP(product.price)}</span>
                <span className="pdp-stock" style={{ color: product.stock < 10 ? "#c0392b" : "#2e9e5b" }}>
                  {product.stock === 0 ? "Out of stock" : product.stock < 10 ? `Only ${product.stock} left` : `${product.stock} in stock`}
                </span>
              </div>

              {product.stock > 0 && (
                <div className="pdp-qty-row">
                  <label className="pdp-qty-label">Quantity</label>
                  <div className="pdp-qty-ctrl">
                    <button className="pdp-qty-btn" onClick={() => setQty((q) => Math.max(1, q - 1))} disabled={qty <= 1}>−</button>
                    <span className="pdp-qty-val">{qty}</span>
                    <button className="pdp-qty-btn" onClick={() => setQty((q) => Math.min(product.stock, q + 1))} disabled={qty >= product.stock}>+</button>
                  </div>
                </div>
              )}

              <button
                className={`pdp-add-btn ${added ? "pdp-add-btn--added" : ""}`}
                onClick={handleAddToCart}
                disabled={product.stock === 0}
              >
                {product.stock === 0 ? "Out of Stock" : added ? "✓ Added to Cart" : "Add to Cart"}
              </button>

              <div className="pdp-meta">
                <div className="pdp-meta-row"><span>Category</span><strong>{product.category.name}</strong></div>
                {product.waterType && (
                  <div className="pdp-meta-row">
                    <span>Water Type</span>
                    <strong style={{ color: badge?.color }}>{product.waterType.charAt(0).toUpperCase() + product.waterType.slice(1)}</strong>
                  </div>
                )}
                <div className="pdp-meta-row">
                  <span>Availability</span>
                  <strong style={{ color: product.stock > 0 ? "#2e9e5b" : "#c0392b" }}>{product.stock > 0 ? "In Stock" : "Out of Stock"}</strong>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {showGate && (
        <div className="gate-overlay" onClick={() => setShowGate(false)}>
          <div className="gate-card" onClick={(e) => e.stopPropagation()}>
            <button className="gate-close" onClick={() => setShowGate(false)}>✕</button>
            <div className="gate-icon">🔒</div>
            <h2 className="gate-title">Sign in to continue</h2>
            <p className="gate-desc">You need an account to add items to your cart and place orders.</p>
            <div className="gate-product"><span>{product.category.icon}</span><span>{product.name}</span></div>
            <div className="gate-actions">
              <button className="gate-btn gate-btn--primary" onClick={() => { setShowGate(false); navigate("/login"); }}>Sign In</button>
              <button className="gate-btn gate-btn--outline" onClick={() => { setShowGate(false); navigate("/register"); }}>Create Account</button>
            </div>
            <p className="gate-note">It's free and takes less than a minute.</p>
          </div>
        </div>
      )}
    </>
  );
}