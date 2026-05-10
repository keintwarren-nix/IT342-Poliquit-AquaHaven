// src/modules/catalog/components/ProductCard.jsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useCart } from "../../cart/context/CartContext.jsx";
import { useAuth } from "../../auth/context/AuthContext.jsx";
import { proxyImage } from "../../../shared/utils/imageProxy.js";
import { formatPHP } from "../../../shared/utils/format.js";

const WATER_BADGE = {
  freshwater: { label: "Freshwater", color: "#1d6a3e" },
  saltwater:  { label: "Saltwater",  color: "#1a4f7a" },
  brackish:   { label: "Brackish",   color: "#5a3e1b" },
};

const CATEGORY_META = {
  fish:      { icon: "🐟", name: "Fish" },
  plants:    { icon: "🌿", name: "Plants" },
  corals:    { icon: "🪸", name: "Corals" },
  equipment: { icon: "⚙️", name: "Equipment" },
  shrimp:    { icon: "🦐", name: "Shrimp" },
  snail:     { icon: "🐌", name: "Snail" },
};

export default function ProductCard({ product, onClick }) {
  const { addItem } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [showGate, setShowGate] = useState(false);
  const [imgFailed, setImgFailed] = useState(false);

  const badge = product.waterType ? WATER_BADGE[product.waterType] : null;
  const proxiedSrc = proxyImage(product.imageUrl);

  const categoryKey =
    typeof product.category === "string"
      ? product.category.toLowerCase()
      : product.category?.slug?.toLowerCase() ?? "";

  const categoryData = CATEGORY_META[categoryKey] || {
    icon: "📦",
    name: typeof product.category === "string" ? product.category : product.category?.name ?? "Unknown",
  };

  const handleAddToCart = (e) => {
    e.stopPropagation();
    if (!user) { setShowGate(true); return; }
    addItem(product, 1);
  };

  return (
    <>
      <div
        className="product-card"
        onClick={() => onClick?.(product)}
        role="button"
        tabIndex={0}
        onKeyDown={(e) => e.key === "Enter" && onClick?.(product)}
      >
        <div className="product-card__img-wrap">
          {proxiedSrc && !imgFailed ? (
            <img
              src={proxiedSrc}
              alt={product.name}
              className="product-card__img"
              loading="lazy"
              onError={() => setImgFailed(true)}
            />
          ) : (
            <div className="product-card__img-placeholder">
              <span>{categoryData.icon}</span>
            </div>
          )}
          {badge && (
            <span className="product-card__water-badge" style={{ background: badge.color }}>
              {badge.label}
            </span>
          )}
        </div>

        <div className="product-card__body">
          <span className="product-card__cat-chip">{categoryData.icon} {categoryData.name}</span>
          <h3 className="product-card__name">{product.name}</h3>
          <p className="product-card__desc">{product.description}</p>
          <div className="product-card__footer">
            <span className="product-card__price">{formatPHP(product.price)}</span>
            <span className="product-card__stock" style={{ color: product.stock < 10 ? "#c0392b" : "#2e9e5b" }}>
              {product.stock < 10 ? `Only ${product.stock} left` : `${product.stock} in stock`}
            </span>
          </div>
          <button className="product-card__btn" onClick={handleAddToCart}>Add to Cart</button>
        </div>
      </div>

      {showGate && (
        <div className="gate-overlay" onClick={() => setShowGate(false)}>
          <div className="gate-card" onClick={(e) => e.stopPropagation()}>
            <button className="gate-close" onClick={() => setShowGate(false)}>✕</button>
            <div className="gate-icon">🔒</div>
            <h2 className="gate-title">Sign in to continue</h2>
            <p className="gate-desc">You need an account to add items to your cart and place orders.</p>
            <div className="gate-product">
              <span>{categoryData.icon}</span>
              <span>{product.name}</span>
            </div>
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
