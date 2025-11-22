import { get, post, del } from "./conectaApi.js"; 
/* Elementos do DOM */
const list = document.getElementById("diaryList");
const form = document.getElementById("diaryForm");
const inputDescricao = document.getElementById("diaryText");
const modalRegistro = document.getElementById("modalRegistro");
const fecharModalRegistro = document.getElementById("fecharModalRegistro");
const modalContent = document.getElementById("modalContent");
const btnExcluirRegistro = document.getElementById("btnExcluirRegistro");

let registroAtualId = null; // guarda o ID do registro aberto no modal

// ===============================
// Função para exibir registros
// ===============================
async function exibirRegistros() {
  list.innerHTML = "<li>Carregando...</li>";

  try {
    const registros = await get("/diario");

    if (!registros || !registros.length) {
      list.innerHTML = "<li>Sem registros ainda.</li>";
      return;
    }

    list.innerHTML = registros
      .map((r, index) => {
        const dataFormatada = r.dataRegistro
          ? new Date(r.dataRegistro).toLocaleDateString("pt-BR")
          : "sem data";

        return `
          <li class="item registro-click" data-index="${index}">
            <strong>${escapeHtml(r.emocao || "")}</strong> — ${escapeHtml(r.descricao || "")}<br>
            <small style="color:#777;">Data: ${dataFormatada}</small><br>
            ${
              r.fotos && r.fotos.length
                ? r.fotos
                    .map(
                      (f) => `
                        <img 
                          src="data:image/jpeg;base64,${f}"
                          width="80"
                          style="margin:4px; border-radius:8px;"
                          alt="foto registro"
                        >
                      `
                    )
                    .join("")
                : ""
            }
          </li>
        `;
      })
      .join("");
  } catch (err) {
    console.error("Erro ao carregar registros:", err);
    list.innerHTML = `<li>Erro ao carregar: ${err.message}</li>`;
  }
}




async function salvarRegistro(e) {
  e.preventDefault();

  const emocaoSelecionada = document.querySelector('input[name="emocao"]:checked');
  if (!emocaoSelecionada) {
    alert("Escolha uma emoção antes de salvar!");
    return;
  }

  const criancaId = localStorage.getItem("criancaId");

  if (!criancaId) {
    alert("Nenhuma criança selecionada! Volte à tela inicial.");
    return;
  }

  const emocao = emocaoSelecionada.value;
  const descricao = inputDescricao.value.trim();
  const arquivos = document.getElementById("entradaFotos").files;

  const fotos = await Promise.all(
    Array.from(arquivos).map(async (file) => {
      const base64 = await toBase64(file);
      return base64.split(",")[1]; 
    })
  );

  const novoRegistro = { 
    criancaId: Number(criancaId),
    emocao, 
    descricao, 
    fotos 
  };

  try {
    await post("/diario", novoRegistro);
    Swal.fire({ icon: "success", title: "Registro salvo!" });
    form.reset();
    exibirRegistros();
  } catch (err) {
    Swal.fire({ icon: "error", title: "Erro ao salvar", text: err.message });
  }
}


// ===============================
// Função auxiliar: converter imagem em base64
// ===============================
function toBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = reject;
  });
}

// ===============================
// Segurança: escapando HTML para evitar injeção ao montar a lista
// ===============================
function escapeHtml(str) {
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}

// ===============================
// Normaliza texto para virar classe CSS
// remove acentos, espaços e caracteres especiais
// ===============================
function normalizarClasse(text) {
  if (!text) return "";
  return text
    .toString()
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "") // remove acentos
    .replace(/[^a-zA-Z0-9]/g, " ") // troca não-alfa por espaço
    .trim()
    .toLowerCase()
    .replace(/\s+/g, "-"); // espaços para hífen (ex: "muito feliz" -> "muito-feliz")
}

// ===============================
// Abrir modal ao clicar no registro
// ===============================
document.addEventListener("click", async (e) => {
  const li = e.target.closest(".registro-click");
  if (!li) return;

  const index = li.dataset.index;
  try {
    const registros = await get("/diario");
    const r = registros[index];

    if (!r) {
      console.error("Registro não encontrado no índice:", index);
      return;
    }

    // Guarda ID real do backend (pode ser campo id ou identificador diferente)
    registroAtualId = r.id ?? r._id ?? null;

    // Remove classes antigas e adiciona a nova normalizada
    modalContent.className = "modal-content";
    const classe = normalizarClasse(r.emocao);
    if (classe) modalContent.classList.add(classe);

    // Preenche modal
    document.getElementById("modalEmocao").textContent = r.emocao || "";
    document.getElementById("modalDescricao").textContent = r.descricao || "";

    const dataFormatada = r.dataRegistro
      ? new Date(r.dataRegistro).toLocaleDateString("pt-BR")
      : "sem data";

    document.getElementById("modalData").textContent = "Data: " + dataFormatada;

    const fotosContainer = document.getElementById("modalFotos");
    fotosContainer.innerHTML =
      r.fotos?.map((f) => `<img src="data:image/jpeg;base64,${f}" alt="foto modal">`).join("") ||
      "<p>Sem fotos</p>";

    modalRegistro.style.display = "flex";
  } catch (err) {
    console.error("Erro ao abrir modal:", err);
    if (window.Swal) {
      Swal.fire({ icon: "error", title: "Erro", text: err.message });
    } else {
      alert("Erro ao abrir registro: " + err.message);
    }
  }
});

// Fechar modal (X e clique fora)
fecharModalRegistro.onclick = () => (modalRegistro.style.display = "none");
modalRegistro.onclick = (e) => {
  if (e.target === modalRegistro) modalRegistro.style.display = "none";
};

// ===============================
// Excluir registro (usa del() do conectaApi.js)
// ===============================
btnExcluirRegistro.addEventListener("click", async () => {
  if (!registroAtualId) {
    alert("Registro sem ID para exclusão.");
    return;
  }

  const confirmed = window.Swal
    ? await Swal.fire({
        icon: "warning",
        title: "Excluir registro?",
        text: "Essa ação não pode ser desfeita.",
        showCancelButton: true,
        confirmButtonText: "Sim, excluir",
        cancelButtonText: "Cancelar",
      })
    : { isConfirmed: confirm("Tem certeza que deseja excluir este registro?") };

  // para compatibilidade quando usamos confirm() retornamos boolean no campo isConfirmed
  const isConfirmed = window.Swal ? confirmed.isConfirmed : confirmed.isConfirmed !== false;

  if (!isConfirmed) return;

  try {
    // chama o DELETE no endpoint correto
    await del(`/diario/${registroAtualId}`);

    if (window.Swal) {
      Swal.fire({ icon: "success", title: "Registro excluído!" });
    } else {
      alert("Registro excluído!");
    }

    modalRegistro.style.display = "none";
    registroAtualId = null;
    exibirRegistros();
  } catch (err) {
    console.error("Erro ao excluir registro:", err);
    if (window.Swal) {
      Swal.fire({ icon: "error", title: "Erro ao excluir", text: err.message });
    } else {
      alert("Erro ao excluir: " + err.message);
    }
  }
});

// ===============================
// Inicialização
// ===============================
form.addEventListener("submit", salvarRegistro);
document.addEventListener("DOMContentLoaded", exibirRegistros);
