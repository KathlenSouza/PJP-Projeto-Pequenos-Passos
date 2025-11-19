// =============================
// 📌 Configuração da base da API
// =============================
const API_BASE = '/api';


// =============================
// 📌 Funções padrões GET / POST / DELETE
// =============================
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

  if (!resp.ok && resp.status !== 201) {
    const errorBody = await resp.json().catch(() => ({}));
    throw new Error(errorBody.erro || `Erro ${resp.status}`);
  }

  try {
    return await resp.json();
  } catch {
    return {}; 
  }
}

// =============================
// 📌 Função DELETE padrão
// =============================
export async function del(endpoint) {
  const resp = await fetch(`${API_BASE}${endpoint}`, {
    method: 'DELETE'
  });

  if (!resp.ok) {
    throw new Error(`Erro ${resp.status}`);
  }

  try {
    return await resp.json();
  } catch {
    return {};
  }
}



// =============================
// 📌 conectaApi PRINCIPAL
// =============================
export const conectaApi = {

  // ---------- USUÁRIOS ----------
  async cadastrarUsuario(usuario) {
    return await post('/usuarios', usuario);
  },

  // ---------- TAREFAS ----------
  async listarTarefas() {
    return await get('/tarefas');
  },

  async criarTarefa(tarefa) {
    return await post('/tarefas', tarefa);
  },

  async excluirTarefa(id) {
    return await del(`/tarefas/${id}`);
  },

  async sugerirTarefas(descricao) {
    return await post('/tarefas/sugerir', { descricao });
  }
};



// =============================
// 📌 Profissionais API
// =============================
export const profissionaisApi = {

  listar: () => get('/profissionais/indicacoes'),

  criar: (dados) => post('/profissionais/indicacao', dados),

  excluir: (id) => del(`/profissionais/${id}`)
};
