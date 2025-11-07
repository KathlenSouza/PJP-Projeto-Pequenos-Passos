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
    return {}; // evita erro ao tentar ler json vazios
  }
}

// 🔹 Mantém o cadastro de usuário (NÃO ALTERAR)
export const conectaApi = {
  async cadastrarUsuario(usuario) {
    const response = await fetch("http://localhost:8080/usuarios", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(usuario)
    });

    if (!response.ok) {
      const erro = await response.json();
      throw new Error(erro.erro || "Erro ao criar usuário");
    }

    return await response.json();
  },

  // 🔹 Adiciona endpoints de tarefas e sugestões (NOVOS)
  async listarTarefas() {
    return await get('/tarefas');
  },

  async criarTarefa(tarefa) {
    return await post('/tarefas', tarefa);
  },

  async excluirTarefa(id) {
    const resp = await fetch(`${API_BASE}/tarefas/${id}`, { method: 'DELETE' });
    if (!resp.ok) throw new Error(`Erro ${resp.status}`);
    return true;
  },

  async sugerirTarefas(descricao) {
    return await post('/tarefas/sugerir', { descricao });
  }
};
