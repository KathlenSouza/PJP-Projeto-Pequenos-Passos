// SRC/STATIC/JS/historico.js
import { get, del } from "./conectaApi.js";

const GRID = document.getElementById("historyGrid");
const DATA_INICIAL = document.getElementById("dataInicial");
const DATA_FINAL = document.getElementById("dataFinal");
const BTN_FILTRAR = document.getElementById("botaoFiltrar");
const BTN_LIMPAR = document.getElementById("botaoLimparFiltro");

let historicoCache = []; // lista completa vinda do backend

// ---------------------------
// Util: formatar data
// ---------------------------
function formatarDataISOParaBR(iso) {
  if (!iso) return "-";
  const d = new Date(iso);
  if (Number.isNaN(d.getTime())) return "-";
  return d.toLocaleString("pt-BR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}

// ---------------------------
// Renderização dos cards
// ---------------------------
function renderizarHistorico(lista) {
  GRID.innerHTML = "";

  if (!lista || !lista.length) {
    GRID.innerHTML =
      '<div class="item">Nenhuma atividade concluída nesse período.</div>';
    return;
  }

  lista.forEach((a) => {
    const artigo = document.createElement("article");
    artigo.className = "card historico-item";

    const dataFmt = formatarDataISOParaBR(a.dataConclusao);

    artigo.innerHTML = `
      <h3>${a.tarefaTitulo || a.descricao || "Atividade"}</h3>
      <p><strong>Concluída em:</strong> ${dataFmt}</p>
      <p><strong>Categoria:</strong> ${a.categoria || "-"}</p>
      <p><strong>Área de desenvolvimento:</strong> ${
        a.areaDesenvolvimento || "-"
      }</p>
      <p><strong>Observações:</strong> ${a.observacao || "-"}</p>
      <span class="status-tag">${a.status || "Concluída"}</span>
      <button class="btn ghost btn-remover" data-id="${a.id}">
        Remover do histórico
      </button>
    `;

    GRID.appendChild(artigo);
  });
}

// ---------------------------
// Aplica filtros de data
// ---------------------------
function aplicarFiltros() {
  let lista = [...historicoCache];

  const inicioVal = DATA_INICIAL.value; // yyyy-MM-dd
  const fimVal = DATA_FINAL.value; // yyyy-MM-dd

  if (inicioVal || fimVal) {
    const dInicio = inicioVal
      ? new Date(inicioVal + "T00:00:00")
      : new Date("1900-01-01T00:00:00");
    const dFim = fimVal
      ? new Date(fimVal + "T23:59:59")
      : new Date("3000-01-01T00:00:00");

    lista = lista.filter((a) => {
      const dAtv = new Date(a.dataConclusao);
      if (Number.isNaN(dAtv.getTime())) return false;
      return dAtv >= dInicio && dAtv <= dFim;
    });
  }

  renderizarHistorico(lista);
}

// ---------------------------
// Limpar filtros
// ---------------------------
function limparFiltros() {
  DATA_INICIAL.value = "";
  DATA_FINAL.value = "";
  renderizarHistorico(historicoCache);
}

// ---------------------------
// Carregar histórico do backend
// ---------------------------
async function carregarHistorico() {
  GRID.innerHTML = "<div class='item'>Carregando histórico...</div>";

  const criancaId = localStorage.getItem("criancaSelecionadaId");

  if (!criancaId) {
    alert("Nenhuma criança selecionada! Volte à tela inicial e escolha uma criança.");
    GRID.innerHTML =
      "<div class='item'>Nenhuma criança selecionada.</div>";
    return;
  }

  try {
    // busca só do histórico da criança
    const dados = await get(`/historico/crianca/${criancaId}`);
    historicoCache = Array.isArray(dados) ? dados : [];

    aplicarFiltros(); // renderiza já respeitando possíveis datas selecionadas
  } catch (err) {
    console.error("Erro ao carregar histórico:", err);
    GRID.innerHTML = `<div class="item">Erro ao carregar histórico: ${err.message}</div>`;
  }
}

// ---------------------------
// Remover item do histórico
// ---------------------------
async function removerDoHistorico(id) {
  if (!id) return;

  const temSwal = typeof Swal !== "undefined";

  const confirmar = temSwal
    ? await Swal.fire({
        icon: "warning",
        title: "Remover do histórico?",
        text: "Essa ação não pode ser desfeita.",
        showCancelButton: true,
        confirmButtonText: "Sim, remover",
        cancelButtonText: "Cancelar",
      })
    : { isConfirmed: confirm("Remover este registro do histórico?") };

  const confirmado = temSwal ? confirmar.isConfirmed : confirmar.isConfirmed;

  if (!confirmado) return;

  try {
    await del(`/historico/${id}`);

    // Remove da lista em memória
    historicoCache = historicoCache.filter((h) => h.id !== id);

    aplicarFiltros();

    if (temSwal) {
      Swal.fire({
        icon: "success",
        title: "Registro removido!",
        timer: 1500,
        showConfirmButton: false,
      });
    } else {
      alert("Registro removido!");
    }
  } catch (err) {
    console.error("Erro ao remover:", err);
    if (temSwal) {
      Swal.fire({
        icon: "error",
        title: "Erro ao remover",
        text: err.message,
      });
    } else {
      alert("Erro ao remover: " + err.message);
    }
  }
}

// ---------------------------
// Eventos
// ---------------------------
document.addEventListener("DOMContentLoaded", () => {
  BTN_FILTRAR.addEventListener("click", (e) => {
    e.preventDefault();
    aplicarFiltros();
  });

  BTN_LIMPAR.addEventListener("click", (e) => {
    e.preventDefault();
    limparFiltros();
  });

  // Delegação para o botão de remover
  GRID.addEventListener("click", (e) => {
    const btn = e.target.closest(".btn-remover");
    if (!btn) return;
    const id = btn.dataset.id;
    removerDoHistorico(id);
  });

  carregarHistorico();
});