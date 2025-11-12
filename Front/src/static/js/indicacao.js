// gerenciamento de indicações de profissionais

const CHAVE_INDICACOES = 'pp_indicacoes';

function lerIndicacoes() {
  try {
    return JSON.parse(localStorage.getItem(CHAVE_INDICACOES) || '[]');
  } catch {
    return [];
  }
}

function salvarIndicacoes(lista) {
  localStorage.setItem(CHAVE_INDICACOES, JSON.stringify(lista));
}

function adicionarProfissional() {
  const nome = document.getElementById('nomeProfissional').value.trim();
  const area = document.getElementById('areaProfissional').value.trim();
  const cidade = document.getElementById('cidadeProfissional').value.trim();
  const contato = document.getElementById('contatoProfissional').value.trim();
  const comentario = document.getElementById('comentarioProfissional').value.trim();
  const avaliacao = document.getElementById('avaliacaoProfissional').value.trim();

  if (!nome || !area || !cidade) {
    if (window.Swal) {
      Swal.fire({
        icon: "warning",
        title: "Campos obrigatórios",
        text: "Preencha pelo menos nome, área e cidade.",
        confirmButtonColor: "#ffc107"
      });
    } else {
      alert('Preencha pelo menos nome, área e cidade.');
    }
    return;
  }

  const lista = lerIndicacoes();
  lista.unshift({
    id: Date.now(),
    nome,
    area,
    cidade,
    contato,
    comentario,
    avaliacao,
    data: new Date().toLocaleDateString('pt-BR')
  });

  salvarIndicacoes(lista);
  limparCampos();
  renderizarIndicacoes();
}

function removerProfissional(id) {
  if (!confirm('Deseja realmente excluir esta indicação?')) return;
  salvarIndicacoes(lerIndicacoes().filter(p => p.id !== id));
  renderizarIndicacoes();
}

function limparCampos() {
  document.querySelectorAll('.input').forEach(i => (i.value = ''));
}

function limparTodas() {
  if (confirm('Apagar todas as indicações?')) {
    localStorage.removeItem(CHAVE_INDICACOES);
    renderizarIndicacoes();
  }
}

function renderizarIndicacoes() {
  const ul = document.getElementById('listaProfissionais');
  const lista = lerIndicacoes();

  ul.innerHTML = '';

  if (!lista.length) {
    ul.innerHTML = '<li class="item">Nenhuma indicação cadastrada ainda.</li>';
    return;
  }

  lista.forEach(p => {
    const li = document.createElement('li');
    li.className = 'item';
    li.innerHTML = `
      <div>
        <strong>${p.nome}</strong> — ${p.area}
        <div class="subtitle">${p.cidade}</div>
        ${p.contato ? `<div><i class="fa-solid fa-phone"></i> ${p.contato}</div>` : ''}
        ${p.comentario ? `<div><em>"${p.comentario}"</em></div>` : ''}
        ${p.avaliacao ? `<div class="avaliacao">${p.avaliacao}</div>` : ''}
        <small class="data">Adicionado em: ${p.data}</small>
      </div>
      <button class="btn danger">Excluir</button>
    `;
    li.querySelector('button').onclick = () => removerProfissional(p.id);
    ul.appendChild(li);
  });
}

document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('botaoAdicionarProfissional').addEventListener('click', adicionarProfissional);
  document.getElementById('botaoLimparIndicacoes').addEventListener('click', limparTodas);
  renderizarIndicacoes();
});

