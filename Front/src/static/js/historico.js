//  exibe e filtra atividades concluídas

const CHAVE_HISTORICO = 'pp_historico';

function lerHistorico() {
  try {
    return JSON.parse(localStorage.getItem(CHAVE_HISTORICO) || '[]');
  } catch {
    return [];
  }
}

function salvarHistorico(lista) {
  localStorage.setItem(CHAVE_HISTORICO, JSON.stringify(lista));
}

// Renderiza os cards
function renderizarHistorico(lista = lerHistorico()) {
  const grid = document.getElementById('historyGrid');
  grid.innerHTML = '';

  if (!lista.length) {
    grid.innerHTML = '<div class="item">Nenhuma atividade concluída ainda.</div>';
    return;
  }

  lista.forEach((atividade) => {
    const artigo = document.createElement('article');
    artigo.className = 'card historico-item';
    artigo.innerHTML = `
      <h3>${atividade.tarefa || atividade.descricao || 'Atividade'}</h3>
      <p><strong>Concluída em:</strong> ${atividade.data || '-'}</p>
      <p><strong>Categoria:</strong> ${atividade.categoria || '-'}</p>
      <p><strong>Observações:</strong> ${atividade.obs || '-'}</p>
      <span class="status-tag">${atividade.status || 'Concluída'}</span>
    `;
    grid.appendChild(artigo);
  });
}

// Filtra atividades por data
function filtrarHistorico() {
  const inicio = document.getElementById('dataInicial').value;
  const fim = document.getElementById('dataFinal').value;

  let lista = lerHistorico();

  if (inicio || fim) {
    lista = lista.filter((a) => {
      const dataAtividade = new Date(a.data.split('/').reverse().join('-'));
      const dInicio = inicio ? new Date(inicio) : new Date('1900-01-01');
      const dFim = fim ? new Date(fim) : new Date('3000-01-01');
      return dataAtividade >= dInicio && dataAtividade <= dFim;
    });
  }

  renderizarHistorico(lista);
}

// Limpa os filtros e mostra tudo
function limparFiltros() {
  document.getElementById('dataInicial').value = '';
  document.getElementById('dataFinal').value = '';
  renderizarHistorico();
}

// Inicializa
document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('botaoFiltrar').addEventListener('click', filtrarHistorico);
  document.getElementById('botaoLimparFiltro').addEventListener('click', limparFiltros);
  renderizarHistorico();
});
