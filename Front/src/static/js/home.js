import {
  agendaApi,
  diarioApi,
  radarApi,
  recursosApi,
  profissionaisApi
} from "./conectaApi.js";

/* ============================================================
   🔹 Funções auxiliares — CRIANÇA
============================================================ */
function obterCriancaLocal() {
  const dados = localStorage.getItem("pp_crianca");
  try {
    return dados ? JSON.parse(dados) : null;
  } catch {
    return null;
  }
}

function calcularIdade(dataNascimento) {
  if (!dataNascimento) return { anos: 0, meses: 0 };

  const nasc = new Date(dataNascimento);
  const hoje = new Date();

  let anos = hoje.getFullYear() - nasc.getFullYear();
  let meses = hoje.getMonth() - nasc.getMonth();

  if (meses < 0) {
    anos--;
    meses += 12;
  }

  return { anos, meses };
}

function exibirCabecalho() {
  const crianca = obterCriancaLocal();
  const nomeElemento = document.getElementById("childName");
  const subtituloElemento = document.getElementById("childSubtitle");

  if (!nomeElemento || !subtituloElemento) return;

  if (!crianca) {
    nomeElemento.textContent = "Pequenos Passos";
    subtituloElemento.textContent = "Configure a criança no menu.";
    return;
  }

  const idade = calcularIdade(crianca.dataNascimento);

  nomeElemento.textContent = `${crianca.nome} — ${idade.anos} ano(s)`;
  subtituloElemento.textContent = `Idade: ${idade.anos} ano(s) e ${idade.meses} mês(es)`;
}

/* ============================================================
   🔥 INICIALIZAÇÃO
============================================================ */
document.addEventListener("DOMContentLoaded", async () => {
  let crianca = obterCriancaLocal();
  const criancaIdSelecionada = localStorage.getItem("criancaSelecionadaId");

  /* ------------------------------------------------------------
     1) BUSCAR CRIANÇA DO BACKEND
  ------------------------------------------------------------- */
  if (criancaIdSelecionada) {
    try {
      const resp = await fetch(`http://localhost:8080/api/criancas/${criancaIdSelecionada}`);

      if (resp.ok) {
        crianca = await resp.json();
        localStorage.setItem("pp_crianca", JSON.stringify(crianca));
      } else {
        console.warn("⚠️ Criança não encontrada no backend.");
      }
    } catch (e) {
      console.error("❌ Erro ao buscar criança:", e);
    }
  }

  exibirCabecalho();

  /* ------------------------------------------------------------
     2) VACINAS
  ------------------------------------------------------------- */
  const CHAVE_VACINAS = "pp_vacinas_4_6";
  const LISTA_VACINAS = [
    { id: "dtp-reforco", titulo: "DTP (Reforço)", faixa: "4–6 anos" },
    { id: "polio-reforco", titulo: "Poliomielite (Ref.)", faixa: "4–6 anos" },
    { id: "mmr", titulo: "Sarampo (MMR)", faixa: "4–6 anos" },
    { id: "influenza", titulo: "Influenza (Anual)", faixa: "Anual" },
    { id: "hepb-reforco", titulo: "Hepatite B (Ref.)", faixa: "4–6 anos" }
  ];

  function lerVacinas() {
    return JSON.parse(localStorage.getItem(CHAVE_VACINAS) || "{}");
  }

  function salvarVacinas(status) {
    localStorage.setItem(CHAVE_VACINAS, JSON.stringify(status));
  }

  function exibirVacinas() {
    const lista = document.getElementById("vaxList");
    if (!lista) return;

    lista.innerHTML = "";

    const status = lerVacinas();

    LISTA_VACINAS.forEach(v => {
      const situacao = status[v.id] || "pendente";
      const li = document.createElement("li");

      li.innerHTML = `
        <div class="vacina-titulo">
          <span>🩹</span>
          <div>
            <strong>${v.titulo}</strong>
            <div class="vacina-etiqueta">${v.faixa}</div>
          </div>
        </div>
        <button class="vacina-status ${situacao === "aplicada" ? "vacina-aplicada" : "vacina-pendente"}">
          ${situacao === "aplicada" ? "Aplicada" : "Pendente"}
        </button>
      `;

      li.querySelector("button").onclick = () => {
        status[v.id] = status[v.id] === "aplicada" ? "pendente" : "aplicada";
        salvarVacinas(status);
        exibirVacinas();
      };

      lista.appendChild(li);
    });

    document.getElementById("resetVax").onclick = () => {
      localStorage.removeItem(CHAVE_VACINAS);
      exibirVacinas();
    };
  }

  exibirVacinas();

  /* ------------------------------------------------------------
     3) CARREGAR RESTO DO BACKEND
  ------------------------------------------------------------- */
  const radarPromise = crianca?.id ? radarApi.progresso(crianca.id) : Promise.resolve(null);

  const [agenda, diario, radar, recursos, profissionais] = await Promise.all([
    agendaApi.listar().catch(() => []),
    diarioApi.semana().catch(() => []),
    radarPromise.catch(() => null),
    recursosApi.listar().catch(() => []),
    profissionaisApi.listar().catch(() => [])
  ]);

  /* ------------------------------------------------------------
     4) AGENDA
  ------------------------------------------------------------- */
  const listaAgenda = document.getElementById("listaAgenda");
  if (listaAgenda) {
    listaAgenda.innerHTML = "";
    agenda.forEach(item => {
      listaAgenda.innerHTML += `
        <li><input type="checkbox" ${item.concluido ? "checked" : ""}>
        <b>${item.hora}</b> — ${item.descricao}</li>
      `;
    });
  }

  /* ------------------------------------------------------------
     5) DIÁRIO
  ------------------------------------------------------------- */
  const resumoEmocional = document.getElementById("resumoEmocional");

  if (resumoEmocional) {
    if (diario.length > 0) {
      const ultimo = diario[diario.length - 1];
      resumoEmocional.innerHTML = `
        ${ultimo.emoji ?? "🙂"} Humor da semana:
        <b>${ultimo.emocao ?? "Não informado"}</b>
      `;
    } else {
      resumoEmocional.textContent = "Sem registros emocionais ainda 😕";
    }
  }

  /* ------------------------------------------------------------
     6) RADAR (AJUSTADO PARA SEU BACKEND)
  ------------------------------------------------------------- */
  const barras = document.getElementById("barrasProgresso");

  // converter backend → formato usado pelo front
  let progressoFormatado = [];

  if (radar && radar.rotulos && radar.valores) {
    progressoFormatado = radar.rotulos.map((area, index) => ({
      area: area,
      percentual: radar.valores[index]
    }));
  }

  if (barras && progressoFormatado.length > 0) {
    barras.innerHTML = "";

    progressoFormatado.forEach(item => {
      barras.innerHTML += `
        <div class="progress-item">
          <label>${item.area}</label>
          <div class="progress-bar">
            <div class="progress-fill" style="width:${item.percentual}%"></div>
          </div>
        </div>
      `;
    });
  }

  /* ------------------------------------------------------------
     7) RECURSOS
  ------------------------------------------------------------- */
  function criarItemLink(t, s, u) {
    const li = document.createElement("li");
    li.className = "item";
    li.innerHTML = `
      <div><strong>${t}</strong>${s ? `<div class="subtitulo">${s}</div>` : ""}</div>
      <a class="btn ghost" href="${u}" target="_blank">Abrir</a>
    `;
    return li;
  }

  function criarItemTexto(txt) {
    const li = document.createElement("li");
    li.className = "item";
    li.innerHTML = `<div>${txt}</div>`;
    return li;
  }

  if (recursos.length > 0) {
    const listaLivros = document.getElementById("booksList");
    const listaVideos = document.getElementById("videosList");
    const listaDicas = document.getElementById("tipsList");

    listaLivros.innerHTML = "";
    listaVideos.innerHTML = "";
    listaDicas.innerHTML = "";

    recursos.forEach(r => {
      if (r.tipo === "livro") listaLivros.appendChild(criarItemLink(r.titulo, r.autor, r.link));
      if (r.tipo === "video") listaVideos.appendChild(criarItemLink(r.titulo, "Vídeo", r.link));
      if (r.tipo === "dica") listaDicas.appendChild(criarItemTexto("• " + r.titulo));
    });
  }

  /* ------------------------------------------------------------
     8) PROFISSIONAIS
  ------------------------------------------------------------- */
  const boxProf = document.getElementById("listaProfissionais");

  if (boxProf) {
    boxProf.innerHTML = "";
    profissionais.forEach(p => {
      boxProf.innerHTML += `
        <div class="card">
          <h3>${p.nome}</h3>
          <p>${p.area}</p>
          <p>${"⭐".repeat(p.avaliacao)}</p>
        </div>`;
    });
  }

  console.log("🏁 Dashboard carregado com sucesso!");
});

/* ============================================================
   📅 CALENDÁRIO
============================================================ */
const calendario = document.getElementById("calendar");
const janelaEvento = document.getElementById("eventModal");
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
  const ultimoDia = new Date(ano, mes + 1, 0).getDate();

  calendario.innerHTML = "";

  for (let dia = 1; dia <= ultimoDia; dia++) {
    const chave = `${ano}-${String(mes + 1).padStart(2, "0")}-${String(dia).padStart(2, "0")}`;
    const diaEl = document.createElement("div");
    diaEl.className = "dia-calendario";
    diaEl.textContent = dia;

    if (eventos[chave]) {
      diaEl.classList.add("evento-existe");
      diaEl.title = eventos[chave].titulo;
    }

    diaEl.onclick = () => abrirJanela(chave);
    calendario.appendChild(diaEl);
  }
}

function abrirJanela(data) {
  dataSelecionada = data;
  const evento = eventos[data] ?? null;

  document.getElementById("modalTitle").textContent =
    evento ? "Editar Evento" : "Novo Evento";

  entradaTituloEvento.value = evento?.titulo ?? "";
  tipoEvento.value = evento?.tipo ?? "atividade";

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
    tipo: tipoEvento.value
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

janelaEvento.onclick = e => {
  if (e.target === janelaEvento) fecharJanela();
};

exibirCalendario();

