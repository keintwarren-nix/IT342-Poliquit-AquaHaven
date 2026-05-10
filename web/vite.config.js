// vite.config.js
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  resolve: {
    extensions: [".jsx", ".js", ".json"],
  },
  server: {
    proxy: {
      // Proxies /api requests to the Spring Boot backend during dev.
      // This avoids CORS preflight issues entirely in development.
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
    },
  },
});