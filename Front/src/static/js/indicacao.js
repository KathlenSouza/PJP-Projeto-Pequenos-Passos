// // indicacao.js (versão integrada com backend)
import { profissionaisApi } from './conectaApi.js'; 

// DOM IDs
const ID_BOTAO_ADICIONAR = 'botaoAdicionarProfissional';
const ID_LISTA = 'listaProfissionais';
// carregamento inicial
document.addEventListener('DOMContentLoaded', carregarIndicacoes);
document.getElementById(ID_BOTAO_ADICIONAR)
        .addEventListener('click', adicionarProfissional);

  function mostrarMensagemSucesso(msg) {
  alert(msg);
}

function mostrarMensagemErro(msg) {
  alert(msg);
}

// --- Funções de integração com API ---

async function carregarIndicacoes() {
  try {
    const lista = await profissionaisApi.listar();
    renderizarIndicacoes(lista || []);
  } catch (err) {
    console.error('Erro ao carregar indicações', err);
    mostrarMensagemErro('Erro ao carregar indicações. Tente novamente.');
    renderizarIndicacoes([]); // limpa a lista visual
  }
}
async function adicionarProfissional() {
  const nome = document.getElementById('nomeProfissional').value.trim();
  const area = document.getElementById('areaProfissional').value.trim();
  const cidade = document.getElementById('cidadeProfissional').value.trim();
  const contato = document.getElementById('contatoProfissional').value.trim();
  const comentario = document.getElementById('comentarioProfissional').value.trim();
  const avaliacao = document.getElementById('avaliacaoProfissional').value.trim();

  if (!nome || !area || !cidade) {
    alert('Preencha pelo menos nome, área e cidade.');
    return;
  }

  // converte avaliação para número, padrão 5 se inválido
  function parseAvaliacao(avaliacao) {
    try {
      return parseInt(avaliacao);
    } catch (error) {
      return 5;        
    }
  }

  const payload = {
    nome,
    area,
    cidade,
    contato,
    comentario,
    avaliacao: parseAvaliacao(avaliacao),
    indicadoPorPais: true
  };

 
  try {
    await profissionaisApi.criar(payload);
    mostrarMensagemSucesso('Indicação adicionada com sucesso!');
    limparCampos();
    // recarrega a lista (poderia também apenas dar unshift)
    await carregarIndicacoes();
  } catch (err) {
    console.error('Erro ao adicionar profissional', err);
    mostrarMensagemErro('Erro ao adicionar indicação. Tente novamente.');
    
  }
}

//TODO: implementar função de limpar campos
async function removerProfissional(id) {
  if (!confirm('Deseja realmente excluir esta indicação?')) return;

  try {
    await profissionaisApi.excluir(id);
    // alguns endpoints retornam 204 sem body, então apenas recarregamos
    mostrarMensagemSucesso('Indicação excluída.');
    await carregarIndicacoes();
  } catch (err) {
    console.error('Erro ao excluir', err);
    mostrarMensagemErro('Erro ao excluir indicação.');
  }
}

// --- Renderização ---

function renderizarIndicacoes(lista) {
  const ul = document.getElementById(ID_LISTA);

  ul.innerHTML = '';

  if (!lista || !lista.length)
  {
    const li = document.createElement('li');
    li.textContent = 'Nenhuma indicação cadastrada.';
    ul.appendChild(li);
    return;
  }
lista.forEach((item) => {
  const li = document.createElement('li'); 
  li.className = 'item';
li.innerHTML = `
  <div class="prof-card">
    <strong>${item.nome}</strong>
    <p><em>Área:</em> ${item.area}</p>
    <p><em>Cidade:</em> ${item.cidade}</p>
    <p><em>Contato:</em> ${item.contato || 'N/A'}</p>
    <p><em>Comentário:</em> ${item.comentario || 'N/A'}</p>
    <p><em>Avaliação:</em> ${'⭐'.repeat(item.avaliacao || 5)}</p>
    <button class="btn small danger" data-id="${item.id}">🗑️ Excluir</button>
  </div>
`;
  const btnExcluir = li.querySelector('button');
  btnExcluir.addEventListener('click', () => removerProfissional(item.id));

  ul.appendChild(li);
});

//strong = negrito
//em = italico
}

function limparCampos() {
  document.querySelectorAll('#formIndicacao input, #formIndicacao textarea, #formIndicacao select')
    .forEach(el => {
      if (el.type === 'select-one') el.selectedIndex = 0;
      else el.value = '';
    });
}

