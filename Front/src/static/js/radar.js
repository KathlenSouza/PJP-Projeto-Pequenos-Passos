// radar.js
import { get } from "./conectaApi.js";

// ==== CHAMADA AO BACKEND DO RADAR (SEM IA) ====
async function carregarRadarBackend(criancaId) {
  try {
    const dados = await get(`/radar/${criancaId}`);
    return dados;
  } catch (erro) {
    console.error("❌ Erro ao carregar radar:", erro);
    return null;
  }
}

// ==== CHAMADA AO BACKEND PARA ANÁLISE DA IA ====
async function carregarAnaliseIA(criancaId) {
  try {
    // endpoint só da análise com IA
    const recomendacoes = await get(`/radar/${criancaId}/analise-ia`);
    return recomendacoes;
  } catch (erro) {
    console.error("❌ Erro ao carregar análise IA:", erro);
    return null;
  }
}

let graficoRadar;

document.addEventListener("DOMContentLoaded", async () => {
  const criancaId = 1; // TODO: trocar pelo ID real depois

  // ================== RADAR / PROGRESSO ==================
  const dados = await carregarRadarBackend(criancaId);

  if (!dados) {
    alert("Erro ao carregar o radar. Tente novamente mais tarde.");
    return;
  }

  const rotulos = dados.rotulos || [];
  const valores = dados.valores || [];

  // === Criar gráfico Radar ===
  const ctx = document.getElementById("radarChart");
  if (ctx && window.Chart) {
    graficoRadar = new Chart(ctx, {
      type: "radar",
      data: {
        labels: rotulos,
        datasets: [
          {
            label: "Progresso (%)",
            data: valores,
            backgroundColor: "rgba(204,147,40,0.25)",
            borderColor: "rgba(204,147,40,1)",
            borderWidth: 2,
            pointBackgroundColor: "rgba(204,147,40,1)",
            pointBorderColor: "#fff",
            pointHoverBackgroundColor: "#fff",
            pointHoverBorderColor: "rgba(204,147,40,1)",
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        scales: {
          r: {
            beginAtZero: true,
            max: 100,
            ticks: {
              stepSize: 20,
              callback: (v) => v + "%",
            },
          },
        },
        plugins: { legend: { display: false } },
      },
    });
  }

  // === Criar Blocos de progresso ===
  const container = document.getElementById("blocksContainer");
  if (container) {
    container.innerHTML = "";
    rotulos.forEach((rotulo, i) => {
      const valor = valores[i] || 0;
      const bloco = document.createElement("div");
      bloco.className = "block-item";
      bloco.innerHTML = `
        <strong>${rotulo}</strong>
        <div class="block-progress">
          <div class="block-progress-bar" style="width:${valor}%"></div>
        </div>
        <span class="block-percentage">${valor}%</span>
      `;
      container.appendChild(bloco);
    });
  }

  // === Texto inicial da lista de desenvolvimento ===
  const lista = document.getElementById("listaDesenvolvimento");
  if (lista) {
    lista.innerHTML = `
      <li>
        <i class="fa-solid fa-lightbulb"></i>
        Clique no botão <b>"Gerar análise com IA"</b> para ver observações sobre o desenvolvimento da criança.
      </li>
    `;
  }

  // ================== BOTÃO PARA GERAR ANÁLISE IA ==================
  const btnIA = document.getElementById("btnGerarIA");
  if (btnIA && lista) {
    btnIA.addEventListener("click", async () => {
      const textoOriginal = btnIA.textContent;
      btnIA.disabled = true;
      btnIA.textContent = "Gerando análise com IA...";

      try {
        const recomendacoes = await carregarAnaliseIA(criancaId);

        if (!recomendacoes || recomendacoes.length === 0) {
          lista.innerHTML = `
            <li>
              <i class="fa-solid fa-circle-exclamation"></i>
              Não foi possível gerar a análise agora. Tente novamente mais tarde.
            </li>
          `;
          return;
        }

        // Preenche lista com as frases vindas da IA
        lista.innerHTML = "";
        recomendacoes.forEach((r) => {
          if (!r || !r.trim()) return;
          const li = document.createElement("li");
          li.innerHTML = `<i class="fa-solid fa-child"></i> ${r.trim()}`;
          lista.appendChild(li);
        });
      } catch (erro) {
        console.error("❌ Erro ao gerar análise IA:", erro);
        lista.innerHTML = `
          <li>
            <i class="fa-solid fa-circle-exclamation"></i>
            Ocorreu um erro ao gerar a análise com IA.
          </li>
        `;
      } finally {
        btnIA.disabled = false;
        btnIA.textContent = textoOriginal;
      }
    });
  }

  console.log("Radar carregado do backend com sucesso!");
});
