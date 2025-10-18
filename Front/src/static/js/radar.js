// radar.js — Gera o gráfico de radar e exibe blocos de progresso.

function lerRadar() {
  const dados = localStorage.getItem('pp_radar');
  return dados ? JSON.parse(dados) : {
    rotulos: ["Motor Grosso", "Motor Fino", "Linguagem", "Cognitivo", "Socioemocional"],
    valores: [65, 80, 70, 75, 60] // valores padrão iniciais
  };
}

let graficoRadar;

document.addEventListener('DOMContentLoaded', () => {
  const dados = lerRadar();

  // ======= Gráfico Radar =======
  const ctx = document.getElementById('radarChart');
  if (ctx && Chart) {
    graficoRadar = new Chart(ctx, {
      type: 'radar',
      data: {
        labels: dados.rotulos,
        datasets: [{
          label: 'Progresso (%)',
          data: dados.valores,
          backgroundColor: 'rgba(204,147,40,0.25)',
          borderColor: 'rgba(204,147,40,1)',
          borderWidth: 2,
          pointBackgroundColor: 'rgba(204,147,40,1)',
          pointBorderColor: '#fff',
          pointHoverBackgroundColor: '#fff',
          pointHoverBorderColor: 'rgba(204,147,40,1)'
        }]
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
              callback: valor => valor + '%'
            },
            pointLabels: {
              font: { size: 12, weight: '600' },
              color: '#2b3352'
            },
            grid: {
              color: 'rgba(0,0,0,0.1)'
            },
            angleLines: {
              color: 'rgba(0,0,0,0.05)'
            }
          }
        },
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: ctx => `${ctx.dataset.label}: ${ctx.parsed.r}%`
            }
          }
        }
      }
    });
  }

  // ======= Blocos de Progresso =======
  const container = document.getElementById('blocksContainer');
  if (container) {
    container.innerHTML = '';
    dados.rotulos.forEach((rotulo, i) => {
      const valor = dados.valores[i] || 0;
      const bloco = document.createElement('div');
      bloco.className = 'block-item';
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

  // ======= Lista de Desenvolvimento =======
  const listaDesenvolvimento = document.getElementById('listaDesenvolvimento');
  if (listaDesenvolvimento) {
    listaDesenvolvimento.innerHTML = `
      <li><i class="fa-solid fa-star"></i> A criança apresenta ótimo progresso motor e boa coordenação.</li>
      <li><i class="fa-solid fa-comment"></i> A linguagem está evoluindo — incentive conversas e leitura.</li>
      <li><i class="fa-solid fa-brain"></i> A área cognitiva mostra avanço em memória e raciocínio lógico.</li>
      <li><i class="fa-solid fa-heart"></i> O aspecto socioemocional segue estável — continue reforçando empatia.</li>
    `;
  }

  console.log('Radar do desenvolvimento carregado com sucesso.');
});
