import axios from "axios";

const API = import.meta.env.VITE_API_URL ?? "http://localhost:8080/api/v1";

// ── Axios instance with JWT interceptor ─────────────────────────────────────
const authApi = axios.create({ baseURL: API });
authApi.interceptors.request.use((config) => {
  const token = sessionStorage.getItem("accessToken");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// ── Types ────────────────────────────────────────────────────────────────────

export interface Category {
  id: number;
  name: string;
  slug: string;
  icon: string;
  sortOrder: number;
}

export interface ProductCategory {
  id: number;
  name: string;
  slug: string;
  icon: string;
}

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  imageUrl: string | null;
  stock: number;
  waterType: "freshwater" | "saltwater" | "brackish" | null;
  category: ProductCategory;
  createdAt: string;
}

export interface PageResult<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  error?: { code: string; message: string };
  timestamp: string;
}

export interface ProductSearchParams {
  keyword?: string;
  categorySlug?: string;
  waterType?: string;
  minPrice?: number;
  maxPrice?: number;
  sortBy?: "name" | "price" | "createdAt";
  sortDir?: "asc" | "desc";
  page?: number;
  size?: number;
}

// ── Order types ───────────────────────────────────────────────────────────────

export type PaymentMethod = "COD" | "GCASH" | "MAYA" | "BANK_TRANSFER";
export type OrderStatus   = "PENDING" | "PAID" | "PROCESSING" | "SHIPPED" | "DELIVERED" | "CANCELLED";

export interface OrderItemResponse {
  productId: number;
  productName: string;
  productImageUrl: string | null;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

export interface Order {
  id: number;
  orderRef: string;
  status: OrderStatus;
  paymentMethod: PaymentMethod;
  totalAmount: number;
  shippingAddress: string;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
  items: OrderItemResponse[];
}

export interface CartItemRequest {
  productId: number;
  quantity: number;
}

export interface PlaceOrderRequest {
  items: CartItemRequest[];
  shippingAddress: string;
  paymentMethod: PaymentMethod;
  notes?: string;
}

// ── Product API calls ─────────────────────────────────────────────────────────

export async function fetchCategories(): Promise<Category[]> {
  const res = await axios.get<ApiResponse<Category[]>>(`${API}/categories`);
  return res.data.data;
}

export async function fetchProducts(
  params: ProductSearchParams = {}
): Promise<PageResult<Product>> {
  const clean = Object.fromEntries(
    Object.entries(params).filter(([, v]) => v !== undefined && v !== "")
  );
  const res = await axios.get<ApiResponse<PageResult<Product>>>(
    `${API}/products`,
    { params: clean }
  );
  return res.data.data;
}

export async function fetchProduct(id: number): Promise<Product> {
  const res = await axios.get<ApiResponse<Product>>(`${API}/products/${id}`);
  return res.data.data;
}

// ── Order API calls ───────────────────────────────────────────────────────────

export async function placeOrder(req: PlaceOrderRequest): Promise<Order> {
  const res = await authApi.post<ApiResponse<Order>>("/orders", req);
  if (!res.data.success) throw new Error(res.data.error?.message ?? "Order failed");
  return res.data.data;
}

export async function fetchMyOrders(): Promise<Order[]> {
  const res = await authApi.get<ApiResponse<Order[]>>("/orders");
  return res.data.data;
}

export async function fetchOrder(ref: string): Promise<Order> {
  const res = await authApi.get<ApiResponse<Order>>(`/orders/${ref}`);
  return res.data.data;
}

export async function confirmPayment(ref: string): Promise<Order> {
  const res = await authApi.post<ApiResponse<Order>>(`/orders/${ref}/pay`);
  if (!res.data.success) throw new Error(res.data.error?.message ?? "Payment failed");
  return res.data.data;
}