// ✅ Define o endereço base da API (via proxy configurado no server.js)
const API_BASE = '/api';

export async function get(endpoint) {
  const resp = await fetch(`${API_BASE}${endpoint}`);

  if (!resp.ok) {
    throw new Error(`Erro ${resp.status}`);
  }

  try {
    return await resp.json();
  } catch {
    return [];
  }
}

export async function post(endpoint, body) {
  const resp = await fetch(`${API_BASE}${endpoint}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  });

  // Aceita 200 e 201 como sucesso
  if (!resp.ok && resp.status !== 201) {
    throw new Error(`Erro ${resp.status}`);
  }

  try {
    return await resp.json();
  } catch {
    return {}; // evita erro ao tentar ler json vazio
  }
}
