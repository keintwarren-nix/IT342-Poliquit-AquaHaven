import { useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import ProductCard from "../components/ProductCard";
import { useProducts } from "../hooks/useProducts";
import "./ProductsPage.css";

const WATER_TYPES = [
  { value: "", label: "All Water" },
  { value: "freshwater", label: "Freshwater" },
  { value: "saltwater",  label: "Saltwater" },
  { value: "brackish",   label: "Brackish" },
];

const SORT_OPTIONS = [
  { value: "createdAt|desc", label: "Newest" },
  { value: "name|asc",       label: "Name A–Z" },
  { value: "price|asc",      label: "Price: Low → High" },
  { value: "price|desc",     label: "Price: High → Low" },
];

export default function ProductsPage() {
  const navigate = useNavigate();
  const {
    categories, products, totalPages, totalElements, page,
    filters, loading, error, hasActiveFilters,
    updateFilter, resetFilters, setPage,
  } = useProducts();

  useEffect(() => { document.title = "AquaHaven | Shop"; }, []);

  const handleSortChange = (val: string) => {
    const [sortBy, sortDir] = val.split("|");
    updateFilter("sortBy", sortBy);
    updateFilter("sortDir", sortDir);
  };

  const currentSort = `${filters.sortBy}|${filters.sortDir}`;

  return (
    <>
      <Navbar />
      <div className="products-page" style={{ marginTop: 58 }}>

        {/* ── Top bar ──────────────────────────────────────────────────────── */}
        <div className="products-topbar">
          <div>
            <h1 className="products-title">Shop</h1>
            <p className="products-subtitle">
              {loading ? "Loading…" : `${totalElements} products found`}
              {hasActiveFilters && (
                <button
                  onClick={resetFilters}
                  style={{ marginLeft: 10, fontSize: "0.78rem", color: "#3D7A56",
                    background: "none", border: "none", cursor: "pointer", fontWeight: 600 }}
                >
                  ✕ Clear filters
                </button>
              )}
            </p>
          </div>

          {/* Sort */}
          <select
            className="products-sort-select"
            value={currentSort}
            onChange={(e) => handleSortChange(e.target.value)}
          >
            {SORT_OPTIONS.map((o) => (
              <option key={o.value} value={o.value}>{o.label}</option>
            ))}
          </select>
        </div>

        {/* ── Search ───────────────────────────────────────────────────────── */}
        <div className="products-search-wrap">
          <div className="products-search-box">
            <svg width="16" height="16" viewBox="0 0 20 20" fill="none">
              <circle cx="8.5" cy="8.5" r="5.75" stroke="#9ca3af" strokeWidth="1.5"/>
              <path d="M13 13l3.5 3.5" stroke="#9ca3af" strokeWidth="1.5" strokeLinecap="round"/>
            </svg>
            <input
              type="text"
              placeholder="Search fish, plants, equipment…"
              value={filters.keyword}
              onChange={(e) => updateFilter("keyword", e.target.value)}
              style={{ flex: 1, border: "none", background: "transparent",
                fontFamily: "'DM Sans',sans-serif", fontSize: "0.9rem",
                color: "#1C2B22", outline: "none" }}
            />
            {filters.keyword && (
              <button onClick={() => updateFilter("keyword", "")}
                style={{ background: "none", border: "none", cursor: "pointer",
                  color: "#9ca3af", fontSize: "1rem" }}>×</button>
            )}
          </div>
        </div>

        {/* ── Category chips ─────────────────────────────────────────────── */}
        <div className="products-category-chips">
          <button
            className={`chip ${!filters.categorySlug ? "chip--active" : ""}`}
            onClick={() => updateFilter("categorySlug", "")}
          >
            🌊 All
          </button>
          {categories.map((cat) => (
            <button
              key={cat.slug}
              className={`chip ${filters.categorySlug === cat.slug ? "chip--active" : ""}`}
              onClick={() => updateFilter("categorySlug", cat.slug)}
            >
              {cat.icon} {cat.name}
            </button>
          ))}
        </div>

        {/* ── Main layout: sidebar + grid ───────────────────────────────────── */}
        <div className="products-layout">

          {/* Sidebar filters */}
          <aside className="products-sidebar">
            <h3 style={{ fontFamily: "'Playfair Display',serif", fontSize: "1rem",
              color: "#1C2B22", margin: "0 0 1rem" }}>Filters</h3>

            {/* Water type */}
            <div className="sidebar-group">
              <label className="sidebar-label">Water Type</label>
              {WATER_TYPES.map((wt) => (
                <label key={wt.value} className="sidebar-radio">
                  <input
                    type="radio"
                    name="waterType"
                    checked={filters.waterType === wt.value}
                    onChange={() => updateFilter("waterType", wt.value)}
                  />
                  <span>{wt.label}</span>
                </label>
              ))}
            </div>

            {/* Price range */}
            <div className="sidebar-group">
              <label className="sidebar-label">Price Range (₱)</label>
              <div style={{ display: "flex", gap: 8, alignItems: "center" }}>
                <input
                  type="number"
                  className="sidebar-input"
                  placeholder="Min"
                  value={filters.minPrice}
                  onChange={(e) => updateFilter("minPrice", e.target.value)}
                  min="0"
                />
                <span style={{ color: "#9ca3af" }}>–</span>
                <input
                  type="number"
                  className="sidebar-input"
                  placeholder="Max"
                  value={filters.maxPrice}
                  onChange={(e) => updateFilter("maxPrice", e.target.value)}
                  min="0"
                />
              </div>
            </div>

            {hasActiveFilters && (
              <button
                onClick={resetFilters}
                style={{ width: "100%", marginTop: 12, padding: "0.6rem",
                  background: "none", border: "1.5px solid #E2DDD6", borderRadius: 8,
                  fontFamily: "'DM Sans',sans-serif", fontSize: "0.82rem",
                  color: "#7A8278", cursor: "pointer" }}
              >
                Reset All Filters
              </button>
            )}
          </aside>

          {/* Product grid */}
          <div>
            {loading && (
              <div className="products-empty">
                <div className="products-spinner" />
                <p>Loading products…</p>
              </div>
            )}

            {!loading && error && (
              <div className="products-empty">
                <span>⚠️</span>
                <p style={{ color: "#B91C1C" }}>{error}</p>
              </div>
            )}

            {!loading && !error && products.length === 0 && (
              <div className="products-empty">
                <span>🔍</span>
                <p>No products match your filters.</p>
                <button
                  onClick={resetFilters}
                  style={{ marginTop: 12, padding: "0.6rem 1.5rem",
                    background: "#3D7A56", color: "#fff", border: "none",
                    borderRadius: 8, cursor: "pointer",
                    fontFamily: "'DM Sans',sans-serif", fontWeight: 600 }}
                >
                  Clear Filters
                </button>
              </div>
            )}

            {!loading && !error && products.length > 0 && (
              <>
                <div className="products-grid">
                  {products.map((p) => (
                    <ProductCard
                      key={p.id}
                      product={p}
                      onClick={() => navigate(`/products/${p.id}`)}
                    />
                  ))}
                </div>

                {/* Pagination */}
                {totalPages > 1 && (
                  <div className="products-pagination">
                    <button
                      className="page-btn"
                      disabled={page === 0}
                      onClick={() => setPage(page - 1)}
                    >← Prev</button>
                    {Array.from({ length: totalPages }, (_, i) => (
                      <button
                        key={i}
                        className={`page-btn ${i === page ? "page-btn--active" : ""}`}
                        onClick={() => setPage(i)}
                      >
                        {i + 1}
                      </button>
                    ))}
                    <button
                      className="page-btn"
                      disabled={page >= totalPages - 1}
                      onClick={() => setPage(page + 1)}
                    >Next →</button>
                  </div>
                )}
              </>
            )}
          </div>
        </div>
      </div>
    </>
  );
}