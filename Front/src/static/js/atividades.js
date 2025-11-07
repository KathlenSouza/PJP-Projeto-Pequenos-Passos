import { conectaApi } from "./conectaApi.js";

const lista = document.getElementById("activitiesList");
const descricaoInput = document.getElementById("activityDesc");
const categoriaSelect = document.getElementById("activityCategory");
const btnAdd = document.getElementById("btnAdd");
const btnSuggest = document.getElementById("btnSuggest");
const btnClear = document.getElementById("btnClearActivities");

// ============================================================
// 🔹 Função: carregar tarefas salvas no banco
// ============================================================
async function carregarAtividades() {
  try {
    const atividades = await conectaApi.listarTarefas();

    if (!atividades || atividades.length === 0) {
      lista.innerHTML = "<li>Nenhuma atividade cadastrada.</li>";
      return;
    }

    lista.innerHTML = atividades
      .map(
        (a) => `
        <li class="list-item">
          <div>
            <strong>${a.descricao}</strong> 
            <span class="categoria">(${a.categoria || "Sem categoria"})</span>
          </div>
          <div class="acoes">
            <button class="btn danger" onclick="excluirAtividade(${a.id})">🗑️</button>
          </div>
        </li>
      `
      )
      .join("");
  } catch (erro) {
    console.error("❌ Erro ao carregar atividades:", erro);
    lista.innerHTML = "<li>Erro ao carregar atividades.</li>";
  }
}

// ============================================================
// 🔹 Função: adicionar nova atividade
// ============================================================
btnAdd.addEventListener("click", async () => {
  const descricao = descricaoInput.value.trim();
  const categoria = categoriaSelect.value;

  if (!descricao) {
    alert("Por favor, digite a descrição da atividade!");
    return;
  }

  try {
    await conectaApi.criarTarefa({ descricao, categoria });
    descricaoInput.value = "";
    carregarAtividades();
  } catch (erro) {
    console.error("❌ Erro ao criar atividade:", erro);
    alert("Erro ao criar atividade.");
  }
});

// ============================================================
// 🔹 Função: excluir atividade individual
// ============================================================
window.excluirAtividade = async (id) => {
  if (!confirm("Deseja excluir esta atividade?")) return;

  try {
    await conectaApi.excluirTarefa(id);
    carregarAtividades();
  } catch (erro) {
    console.error("❌ Erro ao excluir atividade:", erro);
    alert("Erro ao excluir atividade.");
  }
};

// ============================================================
// 🔹 Função: limpar todas as atividades (opcional, backend pode não ter endpoint específico)
// ============================================================
btnClear.addEventListener("click", async () => {
  if (!confirm("Deseja realmente limpar todas as atividades?")) return;

  try {
    const atividades = await conectaApi.listarTarefas();
    for (const a of atividades) {
      await conectaApi.excluirTarefa(a.id);
    }
    carregarAtividades();
  } catch (erro) {
    console.error("❌ Erro ao limpar atividades:", erro);
  }
});

// ============================================================
// 🔹 Função: pedir sugestões à IA (usa OpenAIService no backend)
// ============================================================
btnSuggest.addEventListener("click", async () => {
  const descricao = descricaoInput.value.trim();

  if (!descricao) {
    alert("Descreva o contexto da atividade para sugerir!");
    return;
  }

  btnSuggest.disabled = true;
  btnSuggest.textContent = "Gerando sugestões...";

  try {
    const resposta = await conectaApi.sugerirTarefas(descricao);

    if (resposta && resposta.sugestoes) {
      const sugestoes = Array.isArray(resposta.sugestoes)
        ? resposta.sugestoes
        : [resposta.sugestoes];

      alert("💡 Sugestões da IA:\n\n" + sugestoes.join("\n"));
    } else {
      alert("Nenhuma sugestão recebida.");
    }
  } catch (erro) {
    console.error("❌ Erro ao gerar sugestões:", erro);
    alert("Erro ao gerar sugestões de tarefas.");
  } finally {
    btnSuggest.disabled = false;
    btnSuggest.textContent = "Sugerir";
  }
});

// ============================================================
// 🔹 Inicializar
// ============================================================
carregarAtividades();