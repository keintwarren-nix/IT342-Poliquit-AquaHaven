import { useEffect } from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import "./AboutPage.css";

const TEAM = [
  { name: "Keint Warren Poliquit", role: "Full-Stack Developer", emoji: "🧑‍💻" },
];

const VALUES = [
  { icon: "🐠", title: "Passion for Aquatics", desc: "We live and breathe aquatic life — from nano tanks to full reef systems." },
  { icon: "🌿", title: "Sustainability", desc: "Every product we carry is sourced responsibly with the ecosystem in mind." },
  { icon: "🔬", title: "Expertise", desc: "Our curated catalog is backed by hobbyists and professionals alike." },
  { icon: "💚", title: "Community", desc: "AquaHaven is built for the community — sharing knowledge, not just products." },
];

export default function AboutPage() {
  useEffect(() => {
    document.title = "AquaHaven | About";
  }, []);

  return (
    <>
      <Navbar />
      <main className="about-page">

        <section className="about-hero">
          <div className="about-hero__overlay" />
          <div className="about-hero__content">
            <p className="about-hero__eyebrow">Our Story</p>
            <h1 className="about-hero__title">Built by Aquarists,<br />for Aquarists</h1>
            <p className="about-hero__sub">
              AquaHaven started as a passion project and grew into a curated
              marketplace for everyone — from first-time goldfish owners to
              advanced reef builders.
            </p>
          </div>
        </section>

        <section className="about-section">
          <div className="about-mission">
            <div className="about-mission__text">
              <h2>Our Mission</h2>
              <p>
                We believe every aquarium tells a story. Our mission is to make
                the hobby accessible — with quality products, honest information,
                and a community that supports you at every stage of your journey.
              </p>
              <p>
                From freshwater planted tanks to vibrant saltwater reefs, we
                stock what you actually need, priced fairly and delivered with care.
              </p>
              <Link to="/products" className="about-cta-btn">
                Browse Our Collection →
              </Link>
            </div>
            <div className="about-mission__visual">
              <div className="about-stat">
                <span className="about-stat__num">500+</span>
                <span className="about-stat__label">Products</span>
              </div>
              <div className="about-stat">
                <span className="about-stat__num">5</span>
                <span className="about-stat__label">Categories</span>
              </div>
              <div className="about-stat">
                <span className="about-stat__num">100%</span>
                <span className="about-stat__label">Passion-Driven</span>
              </div>
            </div>
          </div>
        </section>

        <section className="about-section about-section--alt">
          <h2 className="about-section-title">What We Stand For</h2>
          <div className="about-values">
            {VALUES.map(v => (
              <div key={v.title} className="about-value-card">
                <span className="about-value-card__icon">{v.icon}</span>
                <h3>{v.title}</h3>
                <p>{v.desc}</p>
              </div>
            ))}
          </div>
        </section>

        <section className="about-section">
          <h2 className="about-section-title">The Team</h2>
          <div className="about-team">
            {TEAM.map(member => (
              <div key={member.name} className="about-team-card">
                <div className="about-team-card__avatar">{member.emoji}</div>
                <h3>{member.name}</h3>
                <p>{member.role}</p>
              </div>
            ))}
          </div>
        </section>

        <section className="about-footer-cta">
          <h2>Ready to start your aquatic journey?</h2>
          <div className="about-footer-cta__btns">
            <Link to="/products" className="about-cta-btn">Shop Now</Link>
            <Link to="/register" className="about-cta-btn about-cta-btn--outline">Create Account</Link>
          </div>
        </section>

      </main>
    </>
  );
}