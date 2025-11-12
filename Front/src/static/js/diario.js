import { get, post } from "./conectaApi.js";

const list = document.getElementById("diaryList");
const form = document.getElementById("diaryForm");
const inputDescricao = document.getElementById("diaryText");

// ===============================
// Função para exibir registros
// ===============================
async function exibirRegistros() {
  list.innerHTML = "<li>Carregando...</li>";

  try {
    const registros = await get("/diario");

    if (!registros.length) {
      list.innerHTML = "<li>Sem registros ainda.</li>";
      return;
    }

    list.innerHTML = registros
      .map((r) => {
        // formata a data no padrão brasileiro
        const dataFormatada = r.dataRegistro
          ? new Date(r.dataRegistro).toLocaleDateString("pt-BR")
          : "sem data";

        return `
          <li class="item">
            <strong>${r.emocao}</strong> — ${r.descricao}<br>
            <small style="color:#777;">Data: ${dataFormatada}</small><br>
            ${
              r.fotos && r.fotos.length
                ? r.fotos
                    .map(
                      (f) =>
                        `<img src="data:image/jpeg;base64,${f}" width="80" style="margin:4px;border-radius:8px;">`
                    )
                    .join("")
                : ""
            }
          </li>
        `;
      })
      .join("");
  } catch (err) {
    list.innerHTML = `<li>Erro ao carregar: ${err.message}</li>`;
  }
}

// ===============================
// Função para salvar um novo registro
// ===============================
async function salvarRegistro(e) {
  e.preventDefault();

  const emocaoSelecionada = document.querySelector(
    'input[name="emocao"]:checked'
  );
  if (!emocaoSelecionada) {
    alert("Escolha uma emoção antes de salvar!");
    return;
  }

  const emocao = emocaoSelecionada.value;
  const descricao = inputDescricao.value.trim();
  const arquivos = document.getElementById("entradaFotos").files;

  // converter imagens em base64 (sem prefixo)
  const fotos = await Promise.all(
    Array.from(arquivos).map(async (file) => {
      const base64 = await toBase64(file);
      return base64.split(",")[1]; // remove o prefixo data:image/png;base64,
    })
  );

  const novoRegistro = {
    emocao,
    descricao,
    fotos,
  };

  try {
    await post("/diario", novoRegistro);
    if (window.Swal) {
      Swal.fire({
        icon: "success",
        title: "Registro salvo com sucesso!",
        confirmButtonColor: "#28a745"
      });
    } else {
      alert("Registro salvo com sucesso!");
    }
    form.reset();
    exibirRegistros();
  } catch (err) {
    if (window.Swal) {
      Swal.fire({
        icon: "error",
        title: "Erro ao salvar",
        text: err.message,
        confirmButtonColor: "#dc3545"
      });
    } else {
      alert(`Erro ao salvar: ${err.message}`);
    }
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
// Inicialização
// ===============================
form.addEventListener("submit", salvarRegistro);
document.addEventListener("DOMContentLoaded", exibirRegistros);
