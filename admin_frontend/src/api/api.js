const API_BASE = "http://localhost:8001";

export async function request(path, options = {}) {
  const res = await fetch(`${API_BASE}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });

  const text = await res.text();

  if (!res.ok) {
    throw new Error(text);
  }

  return text;
}
