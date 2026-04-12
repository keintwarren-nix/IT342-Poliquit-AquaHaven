// src/components/ProductCard.tsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import type { Product } from "../services/productService";
import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";

interface Props {
  product: Product;
  onClick?: (product: Product) => void;
}

const WATER_BADGE: Record<string, { label: string; color: string }> = {
  freshwater: { label: "Freshwater", color: "#1d6a3e" },
  saltwater:  { label: "Saltwater",  color: "#1a4f7a" },
  brackish:   { label: "Brackish",   color: "#5a3e1b" },
};

export default function ProductCard({ product, onClick }: Props) {
  const { addItem } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [showGate, setShowGate] = useState(false);
  const badge = product.waterType ? WATER_BADGE[product.waterType] : null;

  const handleAddToCart = (e: React.MouseEvent) => {
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
        onKeyDown={e => e.key === "Enter" && onClick?.(product)}
      >
        <div className="product-card__img-wrap">
          {product.imageUrl ? (
            <img
              src={product.imageUrl}
              alt={product.name}
              className="product-card__img"
              loading="lazy"
              onError={e => {
                (e.currentTarget as HTMLImageElement).src =
                  "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='200' height='160' viewBox='0 0 200 160'%3E%3Crect fill='%23eae6df' width='200' height='160'/%3E%3Ctext fill='%23aaa' font-size='36' font-family='sans-serif' x='50%25' y='54%25' text-anchor='middle' dominant-baseline='middle'%3E🐟%3C/text%3E%3C/svg%3E";
              }}
            />
          ) : (
            <div className="product-card__img-placeholder">
              <span>{product.category.icon}</span>
            </div>
          )}
          {badge && (
            <span className="product-card__water-badge" style={{ background: badge.color }}>
              {badge.label}
            </span>
          )}
        </div>

        <div className="product-card__body">
          <span className="product-card__cat-chip">
            {product.category.icon} {product.category.name}
          </span>
          <h3 className="product-card__name">{product.name}</h3>
          <p className="product-card__desc">{product.description}</p>
          <div className="product-card__footer">
            <span className="product-card__price">
              ₱{product.price.toLocaleString("en-PH", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
            </span>
            <span className="product-card__stock" style={{ color: product.stock < 10 ? "#c0392b" : "#2e9e5b" }}>
              {product.stock < 10 ? `Only ${product.stock} left` : `${product.stock} in stock`}
            </span>
          </div>
          <button className="product-card__btn" onClick={handleAddToCart}>
            Add to Cart
          </button>
        </div>
      </div>

      {/* ── Login gate modal ─────────────────────────────────────────── */}
      {showGate && (
        <div className="gate-overlay" onClick={() => setShowGate(false)}>
          <div className="gate-card" onClick={e => e.stopPropagation()}>
            <button className="gate-close" onClick={() => setShowGate(false)}>✕</button>

            <div className="gate-icon">🔒</div>
            <h2 className="gate-title">Sign in to continue</h2>
            <p className="gate-desc">
              You need an account to add items to your cart and place orders.
            </p>

            <div className="gate-product">
              <span>{product.category.icon}</span>
              <span>{product.name}</span>
            </div>

            <div className="gate-actions">
              <button
                className="gate-btn gate-btn--primary"
                onClick={() => { setShowGate(false); navigate("/login"); }}
              >
                Sign In
              </button>
              <button
                className="gate-btn gate-btn--outline"
                onClick={() => { setShowGate(false); navigate("/register"); }}
              >
                Create Account
              </button>
            </div>

            <p className="gate-note">It's free and takes less than a minute.</p>
          </div>
        </div>
      )}
    </>
  );
}