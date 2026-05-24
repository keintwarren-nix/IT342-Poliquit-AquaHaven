import httpClient from "../../../core/http/client";

export async function fetchProfile() {
  const res = await httpClient.get("/users/profile");
  return res.data.data;
}

export async function updateProfile(payload) {
  const res = await httpClient.put("/users/profile", payload);
  return res.data.data;
}

export async function changePassword(payload) {
  await httpClient.post("/users/change-password", payload);
}
