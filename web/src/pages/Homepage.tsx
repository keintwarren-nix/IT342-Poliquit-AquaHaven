import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import ProductCard from "../components/ProductCard";
import { useAuth } from "../context/AuthContext";
import { fetchCategories, fetchProducts } from "../services/productService";
import type { Category, Product } from "../services/productService";
import "./HomePage.css";

export default function HomePage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [categories, setCategories] = useState<Category[]>([]);
  const [newArrivals, setNewArrivals] = useState<Product[]>([]);
  const [loadingCats, setLoadingCats] = useState(true);
  const [loadingProds, setLoadingProds] = useState(true);

  useEffect(() => {
    document.title = "AquaHaven | Home";
  }, []);

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

  const handleCategoryClick = (slug: string) => {
    navigate(`/products?category=${slug}`);
  };

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
                <Link to="/products">Browse Products</Link>
              </>
            ) : (
              <>
                <h1>Your Aquatic World Starts Here</h1>
                <Link to="/register">Get Started</Link>
              </>
            )}
          </div>
        </section>

        <section className="home-section">
          <h2>Categories</h2>
          {loadingCats ? (
            <p>Loading...</p>
          ) : (
            <div className="category-grid">
              {categories.map((cat) => (
                <button key={cat.slug} onClick={() => handleCategoryClick(cat.slug)}>
                  {cat.icon} {cat.name}
                </button>
              ))}
            </div>
          )}
        </section>

        <section className="home-section">
          <h2>New Arrivals</h2>
          {loadingProds ? (
            <p>Loading...</p>
          ) : (
            <div className="home-product-grid">
              {newArrivals.map((p) => (
                <ProductCard
                  key={p.id}
                  product={p}
                  onClick={() => navigate("/products")}
                />
              ))}
            </div>
          )}
        </section>
      </main>
    </>
  );
}