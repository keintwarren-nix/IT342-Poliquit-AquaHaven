// src/shared/utils/format.js
export function formatPHP(amount) {
  return "₱" + Number(amount).toLocaleString("en-PH", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
}
