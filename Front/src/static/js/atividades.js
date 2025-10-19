// atividades.js — Gerencia as atividades e sincroniza o radar

const CHAVE_ATIVIDADES = 'pp_atividades';
const AREAS = ["Motor Grosso", "Motor Fino", "Linguagem", "Cognitivo", "Socioemocional"];

const SUGESTOES_PADRAO = [
  { descricao: "Brincar de faz-de-conta por 10 minutos", categoria: "Cognitivo" },
  { descricao: "Contar a história do dia", categoria: "Linguagem" },
  { descricao: "Andar em linha reta para equilíbrio", categoria: "Motor Grosso" },
  { descricao: "Cortar figuras simples com tesoura sem ponta", categoria: "Motor Fino" },
  { descricao: "Participar de um jogo cooperativo simples", categoria: "Socioemocional" }
];

// ==============================
// 🔄 Leitura e escrita
// ==============================
function lerAtividades() {
  try {
    return JSON.parse(localStorage.getItem(CHAVE_ATIVIDADES) || '[]');
  } catch {
    return [];
  }
}

function salvarAtividades(lista) {
  localStorage.setItem(CHAVE_ATIVIDADES, JSON.stringify(lista));
}

// ==============================
// 📋 Renderizar lista
// ==============================
function exibirAtividades() {
  const listaHtml = document.getElementById('activitiesList');
  const atividades = lerAtividades();

  listaHtml.innerHTML = '';

  if (!atividades.length) {
    listaHtml.innerHTML = '<li class="item">Nenhuma atividade cadastrada. Use "Sugerir" ou adicione manualmente.</li>';
    return;
  }

  atividades.forEach(atividade => {
    const item = document.createElement('li');
    item.className = 'item';
    item.innerHTML = `
      <div class="center">
        <input type="checkbox" ${atividade.concluida ? 'checked' : ''}>
        <span>${atividade.descricao}</span>
        <span class="badge">${atividade.categoria}</span>
      </div>
      <button class="btn danger pequeno">Excluir</button>
    `;

    const checkbox = item.querySelector('input');
    checkbox.addEventListener('change', e => {
      atividade.concluida = e.target.checked;
      const lista = lerAtividades().map(a => a.id === atividade.id ? atividade : a);
      salvarAtividades(lista);
      sincronizarRadar();
      exibirAtividades();
    });

    item.querySelector('button').addEventListener('click', () => {
      const novaLista = lerAtividades().filter(a => a.id !== atividade.id);
      salvarAtividades(novaLista);
      sincronizarRadar();
      exibirAtividades();
    });

    listaHtml.appendChild(item);
  });
}

// ==============================
// ➕ Adicionar atividade manualmente
// ==============================
function adicionarAtividade() {
  const descricao = document.getElementById('activityDesc').value.trim();
  const categoria = document.getElementById('activityCategory').value;

  if (!descricao) {
    alert('Descreva a atividade antes de adicionar.');
    return;
  }

  const lista = lerAtividades();
  lista.push({ id: Date.now(), descricao, categoria, concluida: false });

  salvarAtividades(lista);
  document.getElementById('activityDesc').value = '';
  sincronizarRadar();
  exibirAtividades();
}

// ==============================
// 💡 Sugerir atividades
// ==============================
function sugerirAtividades() {
  const lista = lerAtividades();
  SUGESTOES_PADRAO.forEach((sugestao, i) =>
    lista.push({ id: Date.now() + i, ...sugestao, concluida: false })
  );
  salvarAtividades(lista);
  sincronizarRadar();
  exibirAtividades();
}

// ==============================
// 🗑️ Limpar todas
// ==============================
function limparAtividades() {
  localStorage.removeItem(CHAVE_ATIVIDADES);
  sincronizarRadar();
  exibirAtividades();
}

// ==============================
// 📊 Sincronizar com o radar
// ==============================
function sincronizarRadar() {
  const atividades = lerAtividades();
  const total = {}, concluidas = {};

  AREAS.forEach(a => {
    total[a] = 0;
    concluidas[a] = 0;
  });

  atividades.forEach(a => {
    const area = a.categoria || "Cognitivo";
    total[area]++;
    if (a.concluida) concluidas[area]++;
  });

  const valores = AREAS.map(area =>
    total[area] ? Math.round((concluidas[area] * 100) / total[area]) : 0
  );

  localStorage.setItem('pp_radar', JSON.stringify({ rotulos: AREAS, valores }));
}

// ==============================
// ⚙️ Inicialização
// ==============================
document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('btnAdd').addEventListener('click', adicionarAtividade);
  document.getElementById('btnSuggest').addEventListener('click', sugerirAtividades);
  document.getElementById('btnClearActivities').addEventListener('click', limparAtividades);

  exibirAtividades();
  sincronizarRadar();
});
