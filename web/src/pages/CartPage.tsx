import { useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { useCart } from "../context/CartContext";
import "./CartPage.css";

export default function CartPage() {
  const { items, removeItem, updateQty, clearCart, total } = useCart();
  const navigate = useNavigate();

  useEffect(() => {
    document.title = "AquaHaven | My Cart";
  }, []);

  return (
    <>
      <Navbar />
      <div className="cart-page">
        <div className="cart-container">
          <div className="cart-header">
            <h1 className="cart-title">My Cart</h1>
            {items.length > 0 && (
              <button className="cart-clear-btn" onClick={clearCart}>
                Clear all
              </button>
            )}
          </div>

          {items.length === 0 ? (
            <div className="cart-empty">
              <span>🛒</span>
              <h3>Your cart is empty</h3>
              <p>Add some fish, plants or equipment to get started.</p>
              <Link to="/products" className="cart-shop-btn">
                Browse Products
              </Link>
            </div>
          ) : (
            <div className="cart-layout">
              <div className="cart-items">
                {items.map(item => (
                  <div key={item.product.id} className="cart-item">
                    <div className="cart-item__img">
                      {item.product.imageUrl ? (
                        <img src={item.product.imageUrl} alt={item.product.name} />
                      ) : (
                        <span className="cart-item__emoji">
                          {item.product.category.icon}
                        </span>
                      )}
                    </div>

                    <div className="cart-item__info">
                      <p className="cart-item__cat">
                        {item.product.category.icon} {item.product.category.name}
                      </p>
                      <h3 className="cart-item__name">{item.product.name}</h3>
                      <p className="cart-item__price">
                        ₱{item.product.price.toLocaleString("en-PH", {
                          minimumFractionDigits: 2,
                        })}
                      </p>
                    </div>

                    <div className="cart-item__controls">
                      <div className="qty-control">
                        <button
                          className="qty-btn"
                          onClick={() => updateQty(item.product.id, item.qty - 1)}
                          disabled={item.qty <= 1}
                        >−</button>
                        <span className="qty-value">{item.qty}</span>
                        <button
                          className="qty-btn"
                          onClick={() => updateQty(item.product.id, item.qty + 1)}
                          disabled={item.qty >= item.product.stock}
                        >+</button>
                      </div>
                      <p className="cart-item__subtotal">
                        ₱{(item.product.price * item.qty).toLocaleString("en-PH", {
                          minimumFractionDigits: 2,
                        })}
                      </p>
                      <button
                        className="cart-item__remove"
                        onClick={() => removeItem(item.product.id)}
                        aria-label="Remove"
                      >✕</button>
                    </div>
                  </div>
                ))}
              </div>

              <aside className="cart-summary">
                <h2 className="cart-summary__title">Order Summary</h2>
                <div className="cart-summary__row">
                  <span>Subtotal ({items.reduce((s, i) => s + i.qty, 0)} items)</span>
                  <span>₱{total.toLocaleString("en-PH", { minimumFractionDigits: 2 })}</span>
                </div>
                <div className="cart-summary__row">
                  <span>Shipping</span>
                  <span className="cart-summary__free">Free</span>
                </div>
                <div className="cart-summary__divider" />
                <div className="cart-summary__row cart-summary__total">
                  <span>Total</span>
                  <span>₱{total.toLocaleString("en-PH", { minimumFractionDigits: 2 })}</span>
                </div>
                <button
                  className="cart-checkout-btn"
                  onClick={() => navigate("/checkout")}
                >
                  Proceed to Checkout
                </button>
                <Link to="/products" className="cart-continue-link">
                  ← Continue Shopping
                </Link>
              </aside>
            </div>
          )}
        </div>
      </div>
    </>
  );
}