//  Gerencia o Diário Emocional (texto + fotos) 

document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('diaryForm');
  const list = document.getElementById('diaryList');
  const inputFotos = document.getElementById('entradaFotos');
  const CHAVE = 'pp_diario';

  let registros = [];
  try {
    registros = JSON.parse(localStorage.getItem(CHAVE) || '[]');
  } catch {
    registros = [];
  }

  // Exibir registros
  
  function exibirRegistros() {
    list.innerHTML = '';

    if (!registros.length) {
      list.innerHTML = '<li class="item">Sem registros ainda.</li>';
      return;
    }

    registros.forEach((r, i) => {
      const fotosHTML = (r.fotos || [])
        .map(f => `<img src="${f}" class="foto-thumb" alt="Foto">`)
        .join('');

      const li = document.createElement('li');
      li.className = 'item';
      li.innerHTML = `
        <div>
          <strong>${r.data}</strong> — <b>${r.emocao}</b>
          <p>${r.texto || ''}</p>
          <div class="photo-preview">${fotosHTML}</div>
        </div>
        <div style="margin-top:8px;">
          <button class="btn danger" data-indice="${i}">Excluir</button>
        </div>
      `;

      li.querySelector('button').addEventListener('click', () => {
        registros.splice(i, 1);
        localStorage.setItem(CHAVE, JSON.stringify(registros));
        exibirRegistros();
      });

      list.appendChild(li);
    });
  }

  
  // Adicionar novo registro

  form.addEventListener('submit', async e => {
    e.preventDefault();

    const emocao = document.querySelector('input[name="emocao"]:checked')?.value;
    const texto = document.getElementById('diaryText').value.trim();

    if (!emocao) {
      alert('Selecione uma emoção antes de salvar.');
      return;
    }

    const arquivos = Array.from(inputFotos.files || []);
    if (arquivos.length > 5) {
      alert('Limite de 5 fotos por registro.');
      return;
    }

    const fotos = [];
    for (const arquivo of arquivos) {
      const dataUrl = await new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result);
        reader.onerror = reject;
        reader.readAsDataURL(arquivo);
      });
      fotos.push(dataUrl);
    }

    const novo = {
      emocao,
      texto,
      fotos,
      data: new Date().toLocaleDateString('pt-BR'),
      timestamp: Date.now()
    };

    registros.unshift(novo);
    localStorage.setItem(CHAVE, JSON.stringify(registros));

    form.reset();
    exibirRegistros();
  });

  exibirRegistros();
});
