import { get } from "./conectaApi.js";

// ========================
// 🔹 Buscar Histórico (Concluídas)
// ========================
async function buscarHistorico() {
  const criancaId = localStorage.getItem("criancaId");
  if (!criancaId) throw new Error("Nenhuma criança encontrada.");

  return await get(`/historico/crianca/${criancaId}`);
}

// ========================
// 🔹 Buscar Pendentes
// ========================
async function buscarPendentes() {
  const criancaId = localStorage.getItem("criancaId");
  if (!criancaId) throw new Error("Nenhuma criança encontrada.");

  return await get(`/tarefas/pendentes/crianca/${criancaId}`);
}

// ========================
// 🔹 Buscar Diário
// ========================
async function buscarDiario() {
  const criancaId = localStorage.getItem("criancaId");
  if (!criancaId) throw new Error("Nenhuma criança encontrada.");

  return await get(`/diario/crianca/${criancaId}`);
}

// ========================
// 🔹 Buscar Vacinas
// (placeholder até endpoint real existir)
// ========================
async function buscarVacinas() {
  return [];
}

// ========================
// 🔹 Montador de HTML
// ========================
function montarSecao(titulo, conteudoHTML) {
  return `
    <h2 style="margin-top:24px; color:#2b3352;">${titulo}</h2>
    <div>${conteudoHTML || "<em>Sem registros.</em>"}</div>
  `;
}

function montarLista(lista, itemFn) {
  if (!lista || lista.length === 0) return "<em>Sem dados.</em>";
  return `<ul style="padding-left:20px;">${lista.map(itemFn).join("")}</ul>`;
}

// ========================
// 🔹 Gerar Relatório
// ========================
async function gerarRelatorio() {
  const incluirConcluidas = document.getElementById("rDone").checked;
  const incluirPendentes = document.getElementById("rTodo").checked;
  const incluirDiario = document.getElementById("rDiary").checked;
  const incluirVacinas = document.getElementById("rVax").checked;

  try {

    const resultados = await Promise.all([
      incluirConcluidas ? buscarHistorico() : [],
      incluirPendentes ? buscarPendentes() : [],
      incluirDiario ? buscarDiario() : [],
      incluirVacinas ? buscarVacinas() : []
    ]);

    const [concluidas, pendentes, diario, vacinas] = resultados;

    const html = `
      <div style="font-family:'Poppins',sans-serif; padding:32px; max-width:800px;">
        <h1>📄 Relatório — Pequenos Passos</h1>
        <p><small>Gerado em ${new Date().toLocaleString("pt-BR")}</small></p>
        <hr>

        ${incluirConcluidas ? montarSecao(
          "Atividades Concluídas",
          montarLista(concluidas, a => `
            <li>
              <strong>${a.tarefaTitulo}</strong><br>
              <small>${new Date(a.dataConclusao).toLocaleString("pt-BR")}</small><br>
              Categoria: ${a.categoria || "-"}<br>
              Área: ${a.areaDesenvolvimento || "-"}
            </li>
          `)
        ) : ""}

        ${incluirPendentes ? montarSecao(
          "Atividades Pendentes",
          montarLista(pendentes, a => `
            <li>
              <strong>${a.titulo}</strong> — ${a.categoria}
            </li>
          `)
        ) : ""}

        ${incluirDiario ? montarSecao(
          "Diário Emocional",
          montarLista(diario, d => `
            <li>
              <strong>${new Date(d.dataRegistro).toLocaleDateString("pt-BR")}</strong><br>
              Emoção: ${d.emocao}<br>
              Descrição: ${d.descricao}
            </li>
          `)
        ) : ""}

        ${incluirVacinas ? montarSecao(
          "Vacinas (Em breve)",
          "<em>Recurso ainda não disponível.</em>"
        ) : ""}

        <hr>
        <small>Fonte: API Pequenos Passos</small>
      </div>
    `;

    exportarPDF(html);

  } catch (err) {
    console.error("Erro ao gerar relatório:", err);
    alert("Erro ao gerar relatório: " + err.message);
  }
}

// ========================
// 🔹 Exportar PDF
// ========================
function exportarPDF(html) {
  const jsPDF = window.jspdf?.jsPDF || window.jsPDF;

  if (!jsPDF) {
    const win = window.open("", "_blank");
    win.document.write(html);
    win.document.close();
    return;
  }

  const doc = new jsPDF({ unit: "pt", format: "a4" });

  doc.html(html, {
    x: 24,
    y: 24,
    width: 550,
    callback: () => doc.save("relatorio-pequenos-passos.pdf")
  });
}

// ========================
// 🔹 Inicializar
// ========================
document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("btnReport").addEventListener("click", gerarRelatorio);
});
