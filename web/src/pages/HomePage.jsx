// src/pages/HomePage.jsx
import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Navbar from "../shared/components/Navbar.jsx";
import ProductCard from "../modules/catalog/components/ProductCard.jsx";
import { useAuth } from "../modules/auth/context/AuthContext.jsx";
import { fetchCategories, fetchProducts } from "../modules/catalog/api/catalogApi.js";
import "./HomePage.css";

export default function HomePage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [categories, setCategories] = useState([]);
  const [newArrivals, setNewArrivals] = useState([]);
  const [loadingCats, setLoadingCats] = useState(true);
  const [loadingProds, setLoadingProds] = useState(true);

  useEffect(() => { document.title = "AquaHaven | Home"; }, []);

  useEffect(() => {
    fetchCategories()
      .then((data) => setCategories(data ?? []))
      .catch(() => setCategories([]))
      .finally(() => setLoadingCats(false));
  }, []);

  useEffect(() => {
    fetchProducts({ sortBy: "createdAt", sortDir: "desc", size: 4 })
      .then((res) => setNewArrivals(res?.content ?? []))
      .catch(() => setNewArrivals([]))
      .finally(() => setLoadingProds(false));
  }, []);

  return (
    <>
      <Navbar />
      <main className="home-page">
        <section className="hero">
          <div className="hero-overlay" />
          <div className="hero-content">
            {user ? (
              <>
                <h1>Hello, {user.firstname} 👋</h1>
                <p>Welcome back! Explore what's new in the shop.</p>
                <Link to="/products">Browse Products</Link>
              </>
            ) : (
              <>
                <h1>Your Aquatic World Starts Here</h1>
                <p>Discover fish, plants, corals, and equipment for every level of aquarist — curated with care.</p>
                <Link to="/register">Get Started</Link>
              </>
            )}
          </div>
        </section>

        <section className="home-section">
          <h2>Categories</h2>
          {loadingCats ? <p>Loading...</p> : (
            <div className="category-grid">
              {categories.map((cat) => (
                <button key={cat.slug} onClick={() => navigate(`/products?category=${cat.slug}`)}>
                  {cat.icon} {cat.name}
                </button>
              ))}
            </div>
          )}
        </section>

        <section className="home-section">
          <h2>New Arrivals</h2>
          {loadingProds ? <p>Loading...</p> : (
            <div className="home-product-grid">
              {newArrivals.map((p) => (
                <ProductCard key={p.id} product={p} onClick={() => navigate(`/products/${p.id}`)} />
              ))}
            </div>
          )}
        </section>

        <section className="home-footer-cta">
          <h2>Dive deeper into the hobby</h2>
          <Link to="/products">Browse All Products</Link>
        </section>
      </main>
    </>
  );
}
