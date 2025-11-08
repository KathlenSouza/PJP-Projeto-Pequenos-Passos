import { get, post } from "./conectaApi.js";

const lista = document.getElementById("activitiesList");
const btnAdd = document.getElementById("btnAdd");
const btnSuggest = document.getElementById("btnSuggest");
const btnClear = document.getElementById("btnClearActivities");
const descInput = document.getElementById("activityDesc");
const catInput = document.getElementById("activityCategory");

// Cria botão de IA dinamicamente
const btnAI = document.createElement("button");
btnAI.textContent = "Gerar com IA 🤖";
btnAI.classList.add("btn", "ghost");
btnSuggest.insertAdjacentElement("afterend", btnAI);

// ==================== CARREGAR ATIVIDADES ====================
async function carregarAtividades() {
  lista.innerHTML = "<li>Carregando atividades...</li>";

  try {
    const tarefas = await get("/tarefas");
    if (tarefas.length === 0) {
      lista.innerHTML = "<li>Nenhuma atividade cadastrada.</li>";
      return;
    }

    lista.innerHTML = "";
    tarefas.forEach((t) => renderAtividade(t));
  } catch (erro) {
    console.error("❌ Erro ao carregar atividades:", erro);
    lista.innerHTML = "<li>Erro ao carregar atividades.</li>";
  }
}

// ==================== RENDERIZAR ATIVIDADE ====================
function renderAtividade(t) {
  const li = document.createElement("li");
  li.classList.add("atividade-item");

  const texto = document.createElement("span");
  texto.textContent = `${t.titulo || t.descricao} — ${t.categoria}`;
  texto.classList.add("atividade-texto");

  const btnConcluir = document.createElement("button");
  btnConcluir.textContent = "✔ Concluir";
  btnConcluir.classList.add("btn", "btn-small", "btn-success");
  btnConcluir.addEventListener("click", () => concluirAtividade(t.id));

  li.appendChild(texto);
  li.appendChild(btnConcluir);
  lista.appendChild(li);
}

// ==================== ADICIONAR ATIVIDADE ====================
btnAdd.addEventListener("click", async () => {
  const descricao = descInput.value.trim();
  const categoria = catInput.value.trim();

  if (!descricao || !categoria) {
    alert("Preencha a descrição e a categoria.");
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
    alert("✅ Atividade adicionada com sucesso!");
    descInput.value = "";
    carregarAtividades();
  } catch (erro) {
    console.error("❌ Erro ao criar atividade:", erro);
    alert("Erro ao criar atividade. Verifique os dados.");
  }
});

// ==================== SUGERIR (BANCO) ====================
btnSuggest.addEventListener("click", async () => {
  btnSuggest.textContent = "Gerando sugestões...";
  btnSuggest.disabled = true;

  const idade = 5;
  const area = catInput.value.trim();

  try {
    const sugestoes = await get(`/tarefas/sugerir?idade=${idade}&area=${encodeURIComponent(area)}`);

    if (!sugestoes || sugestoes.length === 0) {
      alert("Nenhuma sugestão encontrada no banco.");
    } else {
      lista.innerHTML = "";
      sugestoes.forEach((t) => renderAtividade(t));
    }
  } catch (erro) {
    console.error("❌ Erro ao gerar sugestões:", erro);
    alert("Erro ao gerar sugestões.");
  } finally {
    btnSuggest.textContent = "Sugerir";
    btnSuggest.disabled = false;
  }
});

// ==================== GERAR COM IA ====================
btnAI.addEventListener("click", async () => {
  btnAI.textContent = "✨ Gerando com IA...";
  btnAI.disabled = true;

  const crianca = {
    idade: 5,
    genero: "feminino", // futuramente vem do usuário logado
  };

  try {
    const resposta = await post("/tarefas/sugerir-ia", crianca);

    if (resposta.sugestoes && resposta.sugestoes.length > 0) {
      lista.innerHTML = "";
      resposta.sugestoes.forEach((s) => {
        const li = document.createElement("li");
        li.textContent = s;
        lista.appendChild(li);
      });
    } else {
      alert("A IA não retornou sugestões no momento.");
    }
  } catch (erro) {
    console.error("❌ Erro ao gerar sugestões IA:", erro);
    alert("Erro ao gerar sugestões via IA.");
  } finally {
    btnAI.textContent = "Gerar com IA 🤖";
    btnAI.disabled = false;
  }
});

// ==================== CONCLUIR ATIVIDADE ====================
async function concluirAtividade(id) {
  if (!confirm("Marcar esta atividade como concluída?")) return;

  try {
    await fetch(`/api/tarefas/${id}/desativar`, { method: "PATCH" });
    alert("✅ Atividade concluída!");
    carregarAtividades();
  } catch (erro) {
    console.error("❌ Erro ao concluir atividade:", erro);
    alert("Erro ao concluir a atividade.");
  }
}

// ==================== LIMPAR TODAS ====================
btnClear.addEventListener("click", () => {
  if (confirm("Tem certeza que deseja limpar todas as atividades?")) {
    lista.innerHTML = "<li>Nenhuma atividade cadastrada.</li>";
  }
});

// ==================== INICIALIZA ====================
carregarAtividades();
