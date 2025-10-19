// relatorio.js — gera ou imprime o relatório de progresso do Pequenos Passos

function lerArmazenamento(chave, padrao = []) {
  try {
    return JSON.parse(localStorage.getItem(chave) || JSON.stringify(padrao));
  } catch {
    return padrao;
  }
}

function montarHTMLRelatorio({ incluirConcluidas, incluirPendentes, incluirDiario, incluirVacinas }) {
  const historico = lerArmazenamento('pp_historico', []);
  const diario = lerArmazenamento('pp_diario', []);
  const vacinas = lerArmazenamento('pp_vacinas', []);

  const concluidas = historico.filter(a => a.status?.toLowerCase() === 'concluída');
  const pendentes = historico.filter(a => a.status?.toLowerCase() !== 'concluída');

  const secao = (titulo, conteudo) => `
    <h2 style="margin-top:18px; color:#2b3352;">${titulo}</h2>
    <div>${conteudo || '<em>Sem registros.</em>'}</div>
  `;

  const lista = (itens, formato) =>
    !itens?.length ? '<em>Sem dados.</em>' : `<ul style="padding-left:20px;">${itens.map(formato).join('')}</ul>`;

  const html = `
    <div style="font-family:'Poppins',sans-serif; padding:24px; max-width:800px; color:#333;">
      <h1>Relatório — Pequenos Passos</h1>
      <p><small>Gerado em ${new Date().toLocaleString('pt-BR')}</small></p>
      <hr>

      ${incluirConcluidas ? secao('✅ Atividades concluídas',
        lista(concluidas, a => `<li>${a.tarefa || a.descricao || 'Atividade'} — <small>${a.data || '-'}</small></li>`)) : ''}

      ${incluirPendentes ? secao('🕓 Atividades pendentes',
        lista(pendentes, a => `<li>${a.tarefa || a.descricao || 'Atividade pendente'}</li>`)) : ''}

      ${incluirDiario ? secao('📔 Diário emocional',
        lista(diario, d => `<li><strong>${d.data}</strong> — ${d.emocao || '-'}<br>${d.texto || ''}</li>`)) : ''}

      ${incluirVacinas ? secao('💉 Vacinas registradas',
        lista(vacinas, v => `<li>${v.nome} — <small>${v.data}</small></li>`)) : ''}

      <hr>
      <small>Fonte: armazenamento local do navegador (localStorage).</small>
    </div>
  `;
  return html;
}

function exportarPDF(html) {
  const jsPDF = window.jspdf?.jsPDF || window.jsPDF;
  const botao = document.getElementById('btnReport');

  botao.disabled = true;
  botao.textContent = 'Gerando...';

  if (jsPDF) {
    const doc = new jsPDF({ unit: 'pt', format: 'a4' });
    doc.html(html, {
      x: 24,
      y: 24,
      width: 550,
      callback: () => {
        doc.save('relatorio-pequenos-passos.pdf');
        botao.disabled = false;
        botao.textContent = 'Gerar Relatório';
      }
    });
  } else {
    const janela = window.open('', '_blank');
    janela.document.write(`<!doctype html><html><head><meta charset="utf-8"><title>Relatório</title></head><body>${html}</body></html>`);
    janela.document.close();
    janela.focus();
    botao.disabled = false;
    botao.textContent = 'Gerar Relatório';
  }
}

document.addEventListener('DOMContentLoaded', () => {
  const botao = document.getElementById('btnReport');
  botao.addEventListener('click', () => {
    const opcoes = {
      incluirConcluidas: document.getElementById('rDone').checked,
      incluirPendentes: document.getElementById('rTodo').checked,
      incluirDiario: document.getElementById('rDiary').checked,
      incluirVacinas: document.getElementById('rVax').checked
    };

    const html = montarHTMLRelatorio(opcoes);
    exportarPDF(html);
  });
});
