// src/modules/catalog/api/catalogApi.js
import httpClient from "../../../core/http/client.js";

/**
 * Backend ProductResponse is FLAT:
 *   { id, name, description, price, imageUrl, stock, waterType,
 *     categoryId, categoryName, categorySlug, createdAt }
 *
 * The frontend everywhere expects a NESTED shape:
 *   { ...product, category: { id, name, slug, icon } }
 *
 * We normalise here so no component needs to change.
 */

// Mirrors the icons set in DataSeeder / CategoryResponse
const SLUG_TO_ICON = {
  "freshwater-fish": "🐠",
  "saltwater-fish":  "🐡",
  "aquatic-plants":  "🌿",
  "fish-food":       "🫙",
  "equipment":       "⚙️",
  "coral-marine":    "🪸",
  "decorations":     "🪨",
};

function normalizeProduct(raw) {
  const slug = raw.categorySlug ?? "";
  return {
    id:          raw.id,
    name:        raw.name,
    description: raw.description,
    price:       raw.price,
    imageUrl:    raw.imageUrl,
    stock:       raw.stock,
    waterType:   raw.waterType,
    createdAt:   raw.createdAt,
    category: {
      id:   raw.categoryId,
      name: raw.categoryName ?? "Unknown",
      slug,
      icon: SLUG_TO_ICON[slug] ?? "📦",
    },
  };
}

export async function fetchCategories() {
  const res = await httpClient.get("/categories");
  return res.data.data;
}

export async function fetchProducts(params = {}) {
  const clean = Object.fromEntries(
    Object.entries(params).filter(([, v]) => v !== undefined && v !== "")
  );
  const res = await httpClient.get("/products", { params: clean });
  const page = res.data.data;
  return {
    ...page,
    content: (page.content ?? []).map(normalizeProduct),
  };
}

export async function fetchProduct(id) {
  const res = await httpClient.get(`/products/${id}`);
  return normalizeProduct(res.data.data);
}