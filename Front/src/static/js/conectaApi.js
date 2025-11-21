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
// 📌 USUÁRIOS
// =============================
export const conectaApi = {
  cadastrarUsuario(usuario) {
    return post('/usuarios', usuario);
  },

  listarTarefas() {
    return get('/tarefas');
  },

  criarTarefa(tarefa) {
    return post('/tarefas', tarefa);
  },

  excluirTarefa(id) {
    return del(`/tarefas/${id}`);
  },

  sugerirTarefas(descricao) {
    return post('/tarefas/sugerir', { descricao });
  }
};



// =============================
// 📌 PROFISSIONAIS API
// =============================
export const profissionaisApi = {
  listar: () => get('/profissionais/indicacoes'),
  criar: (dados) => post('/profissionais/indicacao', dados),
  excluir: (id) => del(`/profissionais/${id}`)
};



// =============================
// 📌 AGENDA API
// =============================
export const agendaApi = {
  listar: () => get('/agenda'),
  criar: (dados) => post('/agenda', dados),
  excluir: (id) => del(`/agenda/${id}`)
};



// =============================
// 📌 DIÁRIO API
// =============================
//  🔥 Importante: SEM ID, porque backend não usa ID no path
export const diarioApi = {
  hoje: () => get('/diario/hoje'),
  semana: () => get('/diario/semana'),
  criar: (dados) => post('/diario', dados)
};



// =============================
// 📌 RADAR API
// =============================
export const radarApi = {
  progresso: (criancaId) => get(`/radar/${criancaId}`),
  analiseIA: (criancaId) => get(`/radar/${criancaId}/analise-ia`)
};



// =============================
// 📌 RECURSOS PEDAGÓGICOS API
// =============================
export const recursosApi = {
  listar: () => get('/recursos'),
  porIdade: (idade) => get(`/recursos/idade/${idade}`)
};



// =============================
// 📌 NOTIFICAÇÕES API
// =============================
export const notificacaoApi = {
  listar: () => get('/notificacoes'),
  criar: (dados) => post('/notificacoes', dados),
  excluir: (id) => del(`/notificacoes/${id}`)
};