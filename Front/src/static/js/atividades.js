import { get, post } from "./conectaApi.js";

const lista = document.getElementById("activitiesList");
const btnAdd = document.getElementById("btnAdd");
const btnSuggest = document.getElementById("btnSuggest");
const btnClear = document.getElementById("btnClearActivities");
const descInput = document.getElementById("activityDesc");
const catInput = document.getElementById("activityCategory");

// ==================== CARREGAR ATIVIDADES ====================
async function carregarAtividades() {
  lista.innerHTML = "<li>Carregando atividades...</li>";

  try {
    const tarefas = await get("/tarefas");
    if (!tarefas || tarefas.length === 0) {
      lista.innerHTML = "<li>Nenhuma atividade cadastrada.</li>";
      return;
    }

    lista.innerHTML = "";
    tarefas.forEach((t) => {
      const li = document.createElement("li");
      li.innerHTML = `
        <span>${t.descricao || t.titulo || "Sem título"} — ${t.categoria || "Sem categoria"}</span>
        <button class="btn-concluir" data-id="${t.id}">Concluir</button>
      `;
      lista.appendChild(li);
    });
  } catch (erro) {
    console.error("❌ Erro ao carregar atividades:", erro);
    lista.innerHTML = "<li>Erro ao carregar atividades.</li>";
  }
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

// ==================== SUGERIR ATIVIDADES ====================
btnSuggest.addEventListener("click", async () => {
  btnSuggest.textContent = "Gerando sugestões...";
  btnSuggest.disabled = true;

  const idade = 5; // Pode futuramente vir da criança logada
  const area = catInput.value.trim();

  try {
    // ✅ Usa GET (compatível com seu backend)
    const sugestoes = await get(`/tarefas/sugerir?idade=${idade}&area=${encodeURIComponent(area)}`);

    if (!sugestoes || sugestoes.length === 0) {
      alert("Nenhuma sugestão encontrada.");
    } else {
      lista.innerHTML = "";
      sugestoes.forEach((t) => {
        const li = document.createElement("li");
        li.innerHTML = `
          <span>${t.descricao || t.titulo || "Atividade sem título"} — ${t.categoria || "Sem categoria"}</span>
          <button class="btn-concluir" data-id="${t.id}">Concluir</button>
        `;
        lista.appendChild(li);
      });
    }
  } catch (erro) {
    console.error("❌ Erro ao gerar sugestões:", erro);
    alert("Erro ao gerar sugestões de tarefas.");
  } finally {
    btnSuggest.textContent = "Sugerir";
    btnSuggest.disabled = false;
  }
});

// ==================== LIMPAR TODAS AS ATIVIDADES ====================
btnClear.addEventListener("click", () => {
  if (confirm("Tem certeza que deseja limpar todas as atividades?")) {
    lista.innerHTML = "<li>Nenhuma atividade cadastrada.</li>";
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

// ==================== EVENTO DE CLIQUE GLOBAL ====================
lista.addEventListener("click", (e) => {
  if (e.target.classList.contains("btn-concluir")) {
    const id = e.target.getAttribute("data-id");
    concluirAtividade(id);
  }
});

// ==================== INICIALIZA ====================
carregarAtividades();
