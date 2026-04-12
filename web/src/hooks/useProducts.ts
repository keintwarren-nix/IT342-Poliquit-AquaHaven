import { useState, useEffect, useCallback, useRef } from "react";
import { useSearchParams } from "react-router-dom";
import { fetchProducts, fetchCategories } from "../services/productService";
import type {
  Category,
  Product,
  PageResult,
  ProductSearchParams,
} from "../services/productService";

export interface FilterState {
  keyword: string;
  categorySlug: string;
  waterType: string;
  minPrice: string;
  maxPrice: string;
  sortBy: "name" | "price" | "createdAt";
  sortDir: "asc" | "desc";
}

const DEFAULT_FILTERS: FilterState = {
  keyword:      "",
  categorySlug: "",
  waterType:    "",
  minPrice:     "",
  maxPrice:     "",
  sortBy:       "createdAt",
  sortDir:      "desc",
};

export function useProducts() {
  const [searchParams] = useSearchParams();

  const [categories,    setCategories]    = useState<Category[]>([]);
  const [products,      setProducts]      = useState<Product[]>([]);
  const [page,          setPage]          = useState(0);
  const [totalPages,    setTotalPages]    = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading,       setLoading]       = useState(false);
  const [error,         setError]         = useState("");

  const [filters, setFilters] = useState<FilterState>(() => ({
    ...DEFAULT_FILTERS,
    categorySlug: searchParams.get("category") ?? "",
    keyword:      searchParams.get("keyword")  ?? "",
  }));

  const debounceRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const [debouncedKeyword, setDebouncedKeyword] = useState(filters.keyword);

  useEffect(() => {
    fetchCategories()
      .then(setCategories)
      .catch(() => setError("Failed to load categories."));
  }, []);

  useEffect(() => {
    if (debounceRef.current) clearTimeout(debounceRef.current);
    debounceRef.current = setTimeout(() => {
      setDebouncedKeyword(filters.keyword);
      setPage(0);
    }, 350);
    return () => { if (debounceRef.current) clearTimeout(debounceRef.current); };
  }, [filters.keyword]);

  const fetchPage = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      const params: ProductSearchParams = {
        keyword:      debouncedKeyword         || undefined,
        categorySlug: filters.categorySlug     || undefined,
        waterType:    filters.waterType        || undefined,
        minPrice:     filters.minPrice  ? Number(filters.minPrice)  : undefined,
        maxPrice:     filters.maxPrice  ? Number(filters.maxPrice)  : undefined,
        sortBy:       filters.sortBy,
        sortDir:      filters.sortDir,
        page,
        size: 20,
      };
      const result: PageResult<Product> = await fetchProducts(params);
      setProducts(result.content);
      setTotalPages(result.totalPages);
      setTotalElements(result.totalElements);
    } catch {
      setError("Failed to load products. Is the backend running?");
    } finally {
      setLoading(false);
    }
  }, [debouncedKeyword, filters.categorySlug, filters.waterType,
      filters.minPrice, filters.maxPrice, filters.sortBy, filters.sortDir, page]);

  useEffect(() => { fetchPage(); }, [fetchPage]);

  const updateFilter = useCallback((key: keyof FilterState, value: string) => {
    setFilters(prev => ({ ...prev, [key]: value }));
    if (key !== "keyword") setPage(0);
  }, []);

  const resetFilters = useCallback(() => {
    setFilters(DEFAULT_FILTERS);
    setPage(0);
  }, []);

  const hasActiveFilters =
    filters.keyword      !== "" ||
    filters.categorySlug !== "" ||
    filters.waterType    !== "" ||
    filters.minPrice     !== "" ||
    filters.maxPrice     !== "";

  return {
    categories, products, totalPages, totalElements, page,
    filters, loading, error, hasActiveFilters,
    updateFilter, resetFilters, setPage,
  };
}