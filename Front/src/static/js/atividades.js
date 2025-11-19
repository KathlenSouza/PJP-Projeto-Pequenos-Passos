import { get, post } from "./conectaApi.js";

const lista = document.getElementById("activitiesList");
const btnAdd = document.getElementById("btnAdd");
const btnSuggest = document.getElementById("btnSuggest");
const btnClear = document.getElementById("btnClearActivities");
const descInput = document.getElementById("activityDesc");
const catInput = document.getElementById("activityCategory");

// Lista global para armazenar sugestões da IA
let atividadesIA = [];

// Criar botão IA dinamicamente
const btnAI = document.createElement("button");
btnAI.textContent = "Gerar com IA 🤖";
btnAI.classList.add("btn", "ghost");
btnSuggest.insertAdjacentElement("afterend", btnAI);


// ==================== CARREGAR ATIVIDADES DO BANCO ====================
async function carregarAtividades() {
  lista.innerHTML = "<li>Carregando atividades...</li>";

  try {
    const tarefas = await get("/tarefas");

    if (!tarefas || tarefas.length === 0) {
      lista.innerHTML = "<li>Nenhuma atividade cadastrada.</li>";
      return;
    }

    lista.innerHTML = "";
    tarefas.forEach((t) => renderAtividade(t)); // render sem index → é do banco

  } catch (erro) {
    console.error("❌ Erro ao carregar atividades:", erro);
    lista.innerHTML = "<li>Erro ao carregar atividades.</li>";
  }
}


// ==================== RENDERIZAR ATIVIDADE (da IA OU do banco) ====================
function renderAtividade(t, index = null) {
  const li = document.createElement("li");
  li.classList.add("atividade-item");

  li.innerHTML = `
    <div class="atividade-bloco">
      <strong>${t.titulo}</strong>
      <p>${t.descricao || ""}</p>

      <div class="atividade-info">
        <p><b>Área:</b> ${t.areaDesenvolvimento}</p>
        <p><b>Categoria:</b> ${t.categoria}</p>
        <p><b>Idade:</b> ${t.faixaEtariaMin} - ${t.faixaEtariaMax} anos</p>
        <p><b>Nível:</b> ${t.nivelDificuldade}</p>
        <p><b>Duração:</b> ${t.duracaoEstimadaMinutos} min</p>
        <p><b>Materiais:</b> ${t.materiaisNecessarios}</p>
        <p><b>Benefícios:</b> ${t.beneficios}</p>
      </div>

      <div class="atividade-acoes">

        ${index !== null ? `
          <button class="btn btn-small btn-primary"
            onclick="adicionarAtividadeIA(${index})">
            ➕ Adicionar
          </button>
        ` : ""}

        ${t.id ? `
          <button class="btn btn-small btn-success"
            onclick="concluirAtividade(${t.id})">
            ✔ Concluir
          </button>

          <button class="btn btn-small btn-danger"
            onclick="excluirAtividade(${t.id})">
            🗑 Excluir
          </button>
        ` : ""}

      </div>

    </div>
  `;

  lista.appendChild(li);
}


// ==================== ADICIONAR ATIVIDADE MANUAL ====================
btnAdd.addEventListener("click", async () => {
  const descricao = descInput.value.trim();
  const categoria = catInput.value.trim();

  if (!descricao || !categoria) {
    if (window.Swal) {
      Swal.fire({
        icon: "warning",
        title: "Campos obrigatórios",
        text: "Preencha a descrição e a categoria.",
      });
    } else {
      alert("Preencha a descrição e a categoria.");
    }
    return;
  }

  const tarefa = {
    titulo: descricao,
    descricao,
    categoria,
    areaDesenvolvimento: categoria.toUpperCase().replace(" ", "_"),
    faixaEtariaMin: 4,
    faixaEtariaMax: 6,
    nivelDificuldade: "Fácil",
    duracaoEstimadaMinutos: 10,
    materiaisNecessarios: "Nenhum",
    beneficios: "Desenvolvimento infantil",
    ativo: true,
  };

  try {
    await post("/tarefas", tarefa);

    if (window.Swal) {
      Swal.fire({ icon: "success", title: "Atividade adicionada!" });
    }

    descInput.value = "";
    carregarAtividades();

  } catch (erro) {
    console.error("❌ Erro ao criar atividade:", erro);

    if (window.Swal) {
      Swal.fire({
        icon: "error",
        title: "Erro ao criar atividade",
        text: "Verifique os dados e tente novamente.",
      });
    }
  }
});


// ==================== ADICIONAR ATIVIDADE GERADA PELA IA ====================
async function adicionarAtividadeIA(index) {
  const tarefa = atividadesIA[index];

  try {
    await post("/tarefas", tarefa);

    if (window.Swal) {
      Swal.fire({
        icon: "success",
        title: "Atividade adicionada ao sistema!",
        confirmButtonColor: "#007bff"
      });
    }

    carregarAtividades();

  } catch (erro) {
    console.error("❌ Erro ao salvar atividade IA:", erro);
    alert("Erro ao salvar atividade IA.");
  }
}
window.adicionarAtividadeIA = adicionarAtividadeIA;


// ==================== GERAR SUGESTÕES DO BANCO ====================
btnSuggest.addEventListener("click", async () => {
  btnSuggest.textContent = "Gerando...";
  btnSuggest.disabled = true;

  const idade = 5;
  const area = catInput.value.trim().toUpperCase().replace(" ", "_");

  try {
    const sugestoes = await get(`/tarefas/sugerir?idade=${idade}&area=${encodeURIComponent(area)}`);

    lista.innerHTML = "";
    sugestoes.forEach((t) => renderAtividade(t));

  } catch (erro) {
    console.error("❌ Erro ao gerar sugestões:", erro);
    alert("Erro ao gerar sugestões.");
  }

  btnSuggest.textContent = "Sugerir";
  btnSuggest.disabled = false;
});


// ==================== GERAR SUGESTÕES COM IA ====================
btnAI.addEventListener("click", async () => {
  btnAI.textContent = "✨ Gerando...";
  btnAI.disabled = true;

  const crianca = { idade: 5, genero: "feminino" };

  try {
    const resposta = await post("/tarefas/sugerir-ia", crianca);
    atividadesIA = resposta.sugestoes;

    lista.innerHTML = "";
    atividadesIA.forEach((t, index) => renderAtividade(t, index));

  } catch (erro) {
    console.error("❌ Erro IA:", erro);
    alert("Erro ao gerar sugestões da IA.");
  }

  btnAI.textContent = "Gerar com IA 🤖";
  btnAI.disabled = false;
});


// ==================== CONCLUIR ATIVIDADE ====================
async function concluirAtividade(id) {
  try {
    await fetch(`/api/tarefas/${id}/desativar`, { method: "PATCH" });
    alert("Atividade concluída!");
    carregarAtividades();

  } catch (erro) {
    console.error("❌ Erro ao concluir:", erro);
    alert("Erro ao concluir atividade.");
  }
}
window.concluirAtividade = concluirAtividade;


// ==================== EXCLUIR ATIVIDADE ====================
async function excluirAtividade(id) {
  if (!confirm("Excluir esta atividade?")) return;

  try {
    await fetch(`/api/tarefas/${id}`, { method: "DELETE" });
    alert("Atividade excluída!");
    carregarAtividades();

  } catch (erro) {
    console.error("❌ Erro ao excluir:", erro);
    alert("Erro ao excluir atividade.");
  }
}
window.excluirAtividade = excluirAtividade;


// ==================== LIMPAR LISTA (somente front-end) ====================
btnClear.addEventListener("click", () => {
  lista.innerHTML = "";
});


// ==================== INICIALIZA ====================
carregarAtividades();
