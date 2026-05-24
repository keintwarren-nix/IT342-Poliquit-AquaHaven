import { useEffect, useState } from "react";
import Navbar from "../shared/components/Navbar.jsx";
import "./ContactPage.css";

export default function ContactPage() {
  const [formData, setFormData] = useState({ name: "", email: "", subject: "", message: "" });
  const [submitted, setSubmitted] = useState(false);

  useEffect(() => {
    document.title = "AquaHaven | Contact";
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    setSubmitted(true);
    setTimeout(() => {
      setFormData({ name: "", email: "", subject: "", message: "" });
      setSubmitted(false);
    }, 3000);
  };

  return (
    <>
      <Navbar />
      <main className="contact-page">
        <section className="contact-hero">
          <div className="contact-hero__overlay" />
          <div className="contact-hero__content">
            <h1>Get in Touch</h1>
            <p>Have questions about products, orders, or just want to say hi? We'd love to hear from you.</p>
          </div>
        </section>

        <section className="contact-section">
          <div className="contact-container">
            <div className="contact-info">
              <div className="contact-info-card">
                <span className="contact-info-card__icon">📍</span>
                <h3>Visit Us</h3>
                <p>123 Aquarium Lane<br />Manila, Philippines</p>
              </div>
              <div className="contact-info-card">
                <span className="contact-info-card__icon">📧</span>
                <h3>Email</h3>
                <p>hello@aquahaven.ph</p>
              </div>
              <div className="contact-info-card">
                <span className="contact-info-card__icon">📞</span>
                <h3>Phone</h3>
                <p>+63 917 123 4567</p>
              </div>
            </div>

            <div className="contact-form-wrapper">
              {submitted ? (
                <div className="contact-success">
                  <span>✅</span>
                  <h2>Message Sent!</h2>
                  <p>We'll get back to you as soon as possible.</p>
                </div>
              ) : (
                <form className="contact-form" onSubmit={handleSubmit}>
                  <div className="contact-form__group">
                    <label htmlFor="name">Full Name</label>
                    <input
                      id="name"
                      type="text"
                      required
                      value={formData.name}
                      onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    />
                  </div>
                  <div className="contact-form__group">
                    <label htmlFor="email">Email Address</label>
                    <input
                      id="email"
                      type="email"
                      required
                      value={formData.email}
                      onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    />
                  </div>
                  <div className="contact-form__group">
                    <label htmlFor="subject">Subject</label>
                    <input
                      id="subject"
                      type="text"
                      required
                      value={formData.subject}
                      onChange={(e) => setFormData({ ...formData, subject: e.target.value })}
                    />
                  </div>
                  <div className="contact-form__group">
                    <label htmlFor="message">Message</label>
                    <textarea
                      id="message"
                      rows="6"
                      required
                      value={formData.message}
                      onChange={(e) => setFormData({ ...formData, message: e.target.value })}
                    />
                  </div>
                  <button type="submit" className="contact-form__btn">Send Message</button>
                </form>
              )}
            </div>
          </div>
        </section>
      </main>
    </>
  );
}
