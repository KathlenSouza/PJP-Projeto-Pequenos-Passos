// Página inicial do Pequenos Passos
// Exibe informações da criança, vacinas, livros, vídeos, dicas e painel de progresso.


//  INFORMAÇÕES DA CRIANÇA


function obterCrianca() {
  const dados = localStorage.getItem('pp_crianca');
  return dados ? JSON.parse(dados) : { nome: "", nascimento: "" };
}

function calcularIdade(dataNascimento) {
  if (!dataNascimento) return { anos: 0, meses: 0, total: 0 };
  const nascimento = new Date(dataNascimento);
  const hoje = new Date();

  let anos = hoje.getFullYear() - nascimento.getFullYear();
  let meses = hoje.getMonth() - nascimento.getMonth();

  if (meses < 0) {
    anos--;
    meses += 12;
  }

  return { anos, meses, total: anos * 12 + meses };
}

function exibirCabecalho() {
  const crianca = obterCrianca();
  const nomeElemento = document.getElementById('childName');
  const subtituloElemento = document.getElementById('childSubtitle');

  if (!nomeElemento || !subtituloElemento) return;

  const idade = calcularIdade(crianca.nascimento);
  nomeElemento.textContent = crianca.nome
    ? `${crianca.nome} — ${idade.anos} ano(s)`
    : 'Pequenos Passos';

  subtituloElemento.textContent = crianca.nascimento
    ? `Idade: ${idade.anos} ano(s) e ${idade.meses} mês(es)`
    : 'Defina o nome e a data de nascimento em Configurações.';
}


//  VACINAS


const CHAVE_VACINAS = 'pp_vacinas_4_6';
const LISTA_VACINAS = [
  { id: 'dtp-reforco', titulo: 'DTP (Reforço)', faixa: '4–6 anos' },
  { id: 'polio-reforco', titulo: 'Poliomielite (Ref.)', faixa: '4–6 anos' },
  { id: 'mmr', titulo: 'Sarampo (MMR)', faixa: '4–6 anos' },
  { id: 'influenza', titulo: 'Influenza (Anual)', faixa: 'Anual' },
  { id: 'hepb-reforco', titulo: 'Hepatite B (Ref.)', faixa: '4–6 anos' },
];

function lerVacinas() {
  try {
    return JSON.parse(localStorage.getItem(CHAVE_VACINAS) || '{}');
  } catch {
    return {};
  }
}

function salvarVacinas(status) {
  localStorage.setItem(CHAVE_VACINAS, JSON.stringify(status));
}

function alternarVacina(id) {
  const status = lerVacinas();
  status[id] = status[id] === 'aplicada' ? 'pendente' : 'aplicada';
  salvarVacinas(status);
  exibirVacinas();
}

function limparVacinas() {
  localStorage.removeItem(CHAVE_VACINAS);
  exibirVacinas();
}

function exibirVacinas() {
  const lista = document.getElementById('vaxList');
  if (!lista) return;

  const status = lerVacinas();
  lista.innerHTML = '';

  LISTA_VACINAS.forEach(vacina => {
    const situacao = status[vacina.id] || 'pendente';
    const item = document.createElement('li');
    item.className = 'vacina-item';
    item.innerHTML = `
      <div class="vacina-titulo">
        <span>🩹</span>
        <div>
          <div><strong>${vacina.titulo}</strong></div>
          <div class="vacina-etiqueta">${vacina.faixa}</div>
        </div>
      </div>
      <button class="vacina-status ${situacao === 'aplicada' ? 'vacina-aplicada' : 'vacina-pendente'}">
        ${situacao === 'aplicada' ? 'Aplicada' : 'Pendente'}
      </button>
    `;
    item.querySelector('button').addEventListener('click', () => alternarVacina(vacina.id));
    lista.appendChild(item);
  });

  const botaoResetar = document.getElementById('resetVax');
  if (botaoResetar) botaoResetar.onclick = limparVacinas;
}


//  CONTEÚDO EDUCATIVO


const LIVROS = [
  { titulo: 'A Lagarta Comilona', autor: 'Eric Carle', link: 'https://www.google.com/search?q=A+Lagarta+Comilona' },
  { titulo: 'O Pequeno Príncipe', autor: 'Antoine de Saint-Exupéry', link: 'https://www.google.com/search?q=O+Pequeno+Príncipe+livro' },
  { titulo: 'A Árvore Generosa', autor: 'Shel Silverstein', link: 'https://www.google.com/search?q=A+Árvore+Generosa' }
];

const VIDEOS = [
  { titulo: 'Como ajudar a criança a ler (dicas práticas)', link: 'https://www.youtube.com/results?search_query=como+ajudar+criança+a+ler' },
  { titulo: 'Desenvolvimento infantil 4–6 anos', link: 'https://www.youtube.com/results?search_query=desenvolvimento+infantil+4+6+anos' }
];

const DICAS = [
  'Leitura compartilhada diária (10–15 min).',
  'Brincar com rimas para consciência fonológica.',
  'Atividades motoras finas (recortar, modelar, rasgar).',
  'Converse sobre as emoções do dia.'
];

function criarItemLink(titulo, subtitulo, url) {
  const item = document.createElement('li');
  item.className = 'item';
  item.innerHTML = `
    <div>
      <strong>${titulo}</strong>
      ${subtitulo ? `<div class="subtitulo">${subtitulo}</div>` : ''}
    </div>
    <a class="btn ghost" href="${url}" target="_blank" rel="noopener">Abrir</a>
  `;
  return item;
}

function criarItemTexto(texto) {
  const item = document.createElement('li');
  item.className = 'item';
  item.innerHTML = `<div>${texto}</div>`;
  return item;
}


//  PAINEL PRINCIPAL 


function formatarDataAtual() {
  const opcoes = { weekday: 'long', day: '2-digit', month: 'long' };
  return new Date().toLocaleDateString('pt-BR', opcoes);
}

const dadosAgenda = [
  { hora: '08:00', descricao: 'Tomar café da manhã', concluido: true },
  { hora: '10:00', descricao: 'Atividade: montar blocos', concluido: false },
  { hora: '14:00', descricao: 'Sessão de leitura', concluido: false }
];

const dadosProgresso = [
  { area: 'Motor Fino', percentual: 80 },
  { area: 'Linguagem', percentual: 60 },
  { area: 'Cognitivo', percentual: 70 },
  { area: 'Socioemocional', percentual: 65 }
];

const dadosAvisos = [
  { tipo: 'alerta', texto: 'Revisar rotina da tarde — duas atividades pendentes.' },
  { tipo: 'notificacao', texto: 'Nova sugestão disponível: Desenhar formas geométricas.' }
];

const dadosSugestoes = ['Pintar figuras com as mãos', 'Brincar de rimas com palavras simples', 'Fazer um quebra-cabeça de 10 peças'];

const dadosDiario = { emoji: '😊', humor: 'Feliz' };

const dadosProfissionais = [
  { nome: 'Dra. Helena Silva', area: 'Psicóloga Infantil', avaliacao: 5 },
  { nome: 'Prof. Marcos Lima', area: 'Terapeuta Ocupacional', avaliacao: 4 },
  { nome: 'Dra. Juliana Costa', area: 'Fonoaudióloga', avaliacao: 4 }
];


//  RENDERIZAÇÃO GERAL


document.addEventListener('DOMContentLoaded', () => {
  exibirCabecalho();
  exibirVacinas();

  const listaLivros = document.getElementById('booksList');
  const listaVideos = document.getElementById('videosList');
  const listaDicas = document.getElementById('tipsList');

  if (listaLivros) LIVROS.forEach(l => listaLivros.appendChild(criarItemLink(l.titulo, l.autor, l.link)));
  if (listaVideos) VIDEOS.forEach(v => listaVideos.appendChild(criarItemLink(v.titulo, 'YouTube', v.link)));
  if (listaDicas) DICAS.forEach(d => listaDicas.appendChild(criarItemTexto(`• ${d}`)));

  // Agenda
  const listaAgenda = document.getElementById('listaAgenda');
  if (listaAgenda) {
    dadosAgenda.forEach(a => {
      const li = document.createElement('li');
      li.innerHTML = `<input type="checkbox" ${a.concluido ? 'checked' : ''}> <b>${a.hora}</b> — ${a.descricao}`;
      listaAgenda.appendChild(li);
    });
  }

  // Barras de progresso
  const containerBarras = document.getElementById('barrasProgresso');
  if (containerBarras) {
    dadosProgresso.forEach(p => {
      const barra = document.createElement('div');
      barra.className = 'progress-item';
      barra.innerHTML = `
        <label>${p.area}</label>
        <div class="progress-bar">
          <div class="progress-fill" style="width:${p.percentual}%;"></div>
        </div>
      `;
      containerBarras.appendChild(barra);
    });
  }

  // Avisos
  const listaAvisos = document.getElementById('listaAvisos');
  if (listaAvisos) {
    dadosAvisos.forEach(a => {
      const li = document.createElement('li');
      li.innerHTML = `<i class="fa-solid ${a.tipo === 'alerta' ? 'fa-triangle-exclamation' : 'fa-bell'}"></i> ${a.texto}`;
      listaAvisos.appendChild(li);
    });
  }

  // Sugestões
  const listaSugestoes = document.getElementById('listaSugestoes');
  if (listaSugestoes) {
    dadosSugestoes.forEach(s => {
      const li = document.createElement('li');
      li.textContent = s;
      listaSugestoes.appendChild(li);
    });
  }

  // Diário (usa o id que existe no HTML)
  const resumoEmocional = document.getElementById('resumoEmocional');
  if (resumoEmocional) {
    resumoEmocional.innerHTML = `${dadosDiario.emoji} Humor médio da semana: <b>${dadosDiario.humor}</b>`;
  }

  // Profissionais
  const listaProfissionais = document.getElementById('listaProfissionais');
  if (listaProfissionais) {
    dadosProfissionais.forEach(p => {
      const cartao = document.createElement('div');
      cartao.className = 'card';
      const estrelas = '⭐'.repeat(p.avaliacao);
      cartao.innerHTML = `<h3>${p.nome}</h3><p>${p.area}</p><p>${estrelas}</p>`;
      listaProfissionais.appendChild(cartao);
    });
  }

  console.log('Página inicial carregada com dados simulados (corrigida).');
});
