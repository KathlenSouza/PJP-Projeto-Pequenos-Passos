import {
  agendaApi,
  diarioApi,
  radarApi,
  recursosApi,
  profissionaisApi
} from "./conectaApi.js";

// -----------------------------------------
// 🔹 Funções auxiliares (criança)
// -----------------------------------------
function obterCrianca() {
  const dados = localStorage.getItem('pp_crianca');
  return dados ? JSON.parse(dados) : { id: null, nome: null, nascimento: null };
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
    : "Pequenos Passos";

  subtituloElemento.textContent = crianca.nascimento
    ? `Idade: ${idade.anos} ano(s) e ${idade.meses} mês(es)`
    : "Defina o nome e a data de nascimento em Configurações.";
}


// -----------------------------------------
// 🔹 Vacinas
// -----------------------------------------
const CHAVE_VACINAS = 'pp_vacinas_4_6';

const LISTA_VACINAS = [
  { id: 'dtp-reforco', titulo: 'DTP (Reforço)', faixa: '4–6 anos' },
  { id: 'polio-reforco', titulo: 'Poliomielite (Ref.)', faixa: '4–6 anos' },
  { id: 'mmr', titulo: 'Sarampo (MMR)', faixa: '4–6 anos' },
  { id: 'influenza', titulo: 'Influenza (Anual)', faixa: 'Anual' },
  { id: 'hepb-reforco', titulo: 'Hepatite B (Ref.)', faixa: '4–6 anos' },
];

function lerVacinas() {
  try { return JSON.parse(localStorage.getItem(CHAVE_VACINAS) || '{}'); }
  catch { return {}; }
}

function salvarVacinas(status) {
  localStorage.setItem(CHAVE_VACINAS, JSON.stringify(status));
}

function alternarVacina(id) {
  const status = lerVacinas();
  status[id] = status[id] === "aplicada" ? "pendente" : "aplicada";
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

  lista.innerHTML = "";
  const status = lerVacinas();

  LISTA_VACINAS.forEach(vacina => {
    const situacao = status[vacina.id] || 'pendente';

    const item = document.createElement("li");
    item.innerHTML = `
      <div class="vacina-titulo">
        <span>🩹</span>
        <div>
          <strong>${vacina.titulo}</strong>
          <div class="vacina-etiqueta">${vacina.faixa}</div>
        </div>
      </div>
      <button class="vacina-status ${situacao === "aplicada" ? "vacina-aplicada" : "vacina-pendente"}">
        ${situacao === "aplicada" ? "Aplicada" : "Pendente"}
      </button>
    `;

    item.querySelector("button").onclick = () => alternarVacina(vacina.id);
    lista.appendChild(item);
  });

  document.getElementById("resetVax").onclick = limparVacinas;
}


// -----------------------------------------
// 🔹 Criar itens (livros, vídeos, dicas)
// -----------------------------------------
function criarItemLink(titulo, subtitulo, url) {
  const item = document.createElement("li");
  item.className = "item";
  item.innerHTML = `
    <div>
      <strong>${titulo}</strong>
      ${subtitulo ? `<div class="subtitulo">${subtitulo}</div>` : ""}
    </div>
    <a class="btn ghost" href="${url}" target="_blank">Abrir</a>
  `;
  return item;
}

function criarItemTexto(texto) {
  const item = document.createElement("li");
  item.className = "item";
  item.innerHTML = `<div>${texto}</div>`;
  return item;
}


// -----------------------------------------
// 🔥 INICIALIZAÇÃO GERAL
// -----------------------------------------
document.addEventListener("DOMContentLoaded", async () => {

  exibirCabecalho();
  exibirVacinas();

  const crianca = obterCrianca();
  const criancaId = crianca?.id ?? null;

  // 🔥 Radar só deve ser carregado se houver criança cadastrada
  const radarPromise = criancaId ? radarApi.progresso(criancaId) : Promise.resolve(null);

  // 🔄 Carregar tudo paralelamente
  const [agenda, diario, radar, recursos, profissionais] = await Promise.all([
    agendaApi.listar().catch(() => []),
    diarioApi.semana().catch(() => null),     // diário não precisa de ID
    radarPromise.catch(() => null),
    recursosApi.listar().catch(() => []),
    profissionaisApi.listar().catch(() => []),
  ]);

  // -------------------------------------
  // 📌 AGENDA
  // -------------------------------------
  const listaAgenda = document.getElementById("listaAgenda");
  if (listaAgenda) {
    listaAgenda.innerHTML = "";
    agenda.forEach(item => {
      const li = document.createElement("li");
      li.innerHTML = `
        <input type="checkbox" ${item.concluido ? "checked" : ""}>
        <b>${item.hora}</b> — ${item.descricao}
      `;
      listaAgenda.appendChild(li);
    });
  }

  // -------------------------------------
// 📌 DIÁRIO (humor semanal)
// -------------------------------------
const resumoEmocional = document.getElementById("resumoEmocional");

if (resumoEmocional && diario && diario.length > 0) {
    // pega o último registro cadastrado
    const ultimo = diario[diario.length - 1];

    resumoEmocional.innerHTML = `
        ${ultimo.emoji ?? "🙂"} Humor da semana: 
        <b>${ultimo.humor ?? "Não informado"}</b>
    `;
} else if (resumoEmocional) {
    resumoEmocional.innerHTML = "Sem registros emocionais ainda 😕";
}

  // -------------------------------------
  // 📌 RADAR / PROGRESSO
  // -------------------------------------
  const barras = document.getElementById("barrasProgresso");

  if (barras && radar && radar.progresso) {
    barras.innerHTML = "";

    radar.progresso.forEach(area => {
      const el = document.createElement("div");
      el.className = "progress-item";

      el.innerHTML = `
        <label>${area.area}</label>
        <div class="progress-bar">
          <div class="progress-fill" style="width:${area.percentual}%"></div>
        </div>
      `;

      barras.appendChild(el);
    });
  }

  // -------------------------------------
  // 📌 RECURSOS (livros, vídeos, dicas)
  // -------------------------------------
  const listaLivros = document.getElementById("booksList");
  const listaVideos = document.getElementById("videosList");
  const listaDicas = document.getElementById("tipsList");

  if (recursos.length > 0) {
    listaLivros.innerHTML = "";
    listaVideos.innerHTML = "";
    listaDicas.innerHTML = "";

    recursos.forEach(r => {
      if (r.tipo === "livro") listaLivros.appendChild(criarItemLink(r.titulo, r.autor, r.link));
      if (r.tipo === "video") listaVideos.appendChild(criarItemLink(r.titulo, "Vídeo", r.link));
      if (r.tipo === "dica") listaDicas.appendChild(criarItemTexto("• " + r.titulo));
    });
  }

  // -------------------------------------
  // 📌 PROFISSIONAIS
  // -------------------------------------
  const boxProf = document.getElementById("listaProfissionais");

  if (boxProf) {
    boxProf.innerHTML = "";

    profissionais.forEach(p => {
      const card = document.createElement("div");
      card.className = "card";

      card.innerHTML = `
        <h3>${p.nome}</h3>
        <p>${p.area}</p>
        <p>${"⭐".repeat(p.avaliacao)}</p>
      `;

      boxProf.appendChild(card);
    });
  }

  console.log("🏁 Dashboard carregado com dados reais!");
});

// =============================
// 📅 CALENDÁRIO LOCAL (EVENTOS DO USUÁRIO)
// =============================

// Elementos do calendário
const calendario = document.getElementById("calendar");
const janelaEvento = document.getElementById("eventModal");
const tituloJanela = document.getElementById("modalTitle");
const entradaTituloEvento = document.getElementById("eventTitle");
const tipoEvento = document.getElementById("eventType");
const botaoSalvar = document.getElementById("botaoSalvarEvento");
const botaoExcluir = document.getElementById("botaoExcluirEvento");
const botaoAdicionar = document.getElementById("addEventBtn");

let dataSelecionada = null;
let eventos = JSON.parse(localStorage.getItem("pp_eventos")) || {};

function exibirCalendario() {
  const hoje = new Date();
  const ano = hoje.getFullYear();
  const mes = hoje.getMonth();

  const primeiroDia = new Date(ano, mes, 1);
  const ultimoDia = new Date(ano, mes + 1, 0);

  calendario.innerHTML = "";

  for (let dia = 1; dia <= ultimoDia.getDate(); dia++) {
    const elementoDia = document.createElement("div");
    elementoDia.classList.add("dia-calendario");

    const chaveDia = `${ano}-${String(mes + 1).padStart(2, "0")}-${String(dia).padStart(2, "0")}`;
    elementoDia.textContent = dia;

    if (eventos[chaveDia]) {
      elementoDia.classList.add("evento-existe");
      elementoDia.title = eventos[chaveDia].titulo;
    }

    elementoDia.onclick = () => abrirJanela(chaveDia);
    calendario.appendChild(elementoDia);
  }
}

function abrirJanela(data) {
  dataSelecionada = data;
  const eventoExistente = eventos[data];

  tituloJanela.textContent = eventoExistente ? "Editar Evento" : "Novo Evento";
  entradaTituloEvento.value = eventoExistente ? eventoExistente.titulo : "";
  tipoEvento.value = eventoExistente ? eventoExistente.tipo : "atividade";

  janelaEvento.classList.add("mostrar");
}

function fecharJanela() {
  janelaEvento.classList.remove("mostrar");
  entradaTituloEvento.value = "";
}

botaoSalvar.onclick = () => {
  if (!dataSelecionada) return;

  eventos[dataSelecionada] = {
    titulo: entradaTituloEvento.value,
    tipo: tipoEvento.value,
  };

  localStorage.setItem("pp_eventos", JSON.stringify(eventos));
  fecharJanela();
  exibirCalendario();
};

botaoExcluir.onclick = () => {
  if (dataSelecionada && eventos[dataSelecionada]) {
    delete eventos[dataSelecionada];
    localStorage.setItem("pp_eventos", JSON.stringify(eventos));
    fecharJanela();
    exibirCalendario();
  }
};

botaoAdicionar.onclick = () => {
  const hoje = new Date().toISOString().split("T")[0];
  abrirJanela(hoje);
};

janelaEvento.onclick = (e) => {
  if (e.target === janelaEvento) fecharJanela();
};

// Inicia calendário
exibirCalendario();
