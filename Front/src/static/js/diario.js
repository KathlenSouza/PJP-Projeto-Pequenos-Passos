import { get, post } from "./conectaApi.js";

const list = document.getElementById("diaryList");
const form = document.getElementById("diaryForm");
const inputDescricao = document.getElementById("diaryText");

async function exibirRegistros() {
  list.innerHTML = '<li>Carregando...</li>';
  try {
    const registros = await get("/diario");

    if (!registros.length) {
      list.innerHTML = '<li>Sem registros ainda.</li>';
      return;
    }

    list.innerHTML = registros
      .map(
        (r) => `
        <li class="item">
          <strong>${r.emocao}</strong> - ${r.descricao}
          <br>
          ${
            r.fotos && r.fotos.length
              ? r.fotos.map((f) => `<img src="${f}" width="80" style="margin:4px;border-radius:8px;">`).join('')
              : ''
          }
        </li>
      `
      )
      .join("");
  } catch (err) {
    list.innerHTML = `<li>Erro ao carregar: ${err.message}</li>`;
  }
}

async function salvarRegistro(e) {
  e.preventDefault();
  const emocao = document.querySelector('input[name="emocao"]:checked').value;
  const descricao = inputDescricao.value.trim();
  const arquivos = document.getElementById("entradaFotos").files;

  // Converter imagens em base64
  const fotos = await Promise.all(
    Array.from(arquivos).map(file => toBase64(file))
  );

  try {
    await post("/diario", { emocao, descricao, fotos });
    alert("Registro salvo com sucesso!");
    exibirRegistros();
  } catch (err) {
    alert(`Erro ao salvar: ${err.message}`);
  }
}

// Função auxiliar
function toBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = reject;
  });
}


form.addEventListener("submit", salvarRegistro);
document.addEventListener("DOMContentLoaded", exibirRegistros);
