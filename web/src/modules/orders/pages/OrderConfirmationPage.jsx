import { useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import Navbar from "../../../shared/components/Navbar.jsx";
import "./OrderConfirmationPage.css";

export default function OrderConfirmationPage() {
  const { ref } = useParams();

  useEffect(() => {
    document.title = "AquaHaven | Order Confirmed";
  }, []);

  return (
    <>
      <Navbar />
      <main className="confirmation-page">
        <div className="confirmation-container">
          <div className="confirmation-card">
            <span className="confirmation-icon">🎉</span>
            <h1>Order Confirmed!</h1>
            <p className="confirmation-subtitle">Thank you for your purchase. We're preparing your order for shipment.</p>
            
            <div className="confirmation-ref">
              <span className="confirmation-ref__label">Order Reference</span>
              <span className="confirmation-ref__value">{ref}</span>
            </div>

            <div className="confirmation-actions">
              <Link to={`/orders/${ref}`} className="confirmation-btn confirmation-btn--primary">View Order Details</Link>
              <Link to="/products" className="confirmation-btn confirmation-btn--secondary">Continue Shopping</Link>
            </div>
          </div>
        </div>
      </main>
    </>
  );
}
