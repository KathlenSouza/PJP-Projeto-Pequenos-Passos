// ==========================
// 🏠 Home: Panda + Nome/Idade + Vacinas + Conteúdos
// ==========================

// Lê configs
function getChild(){
  const raw = localStorage.getItem('pp_child');
  return raw ? JSON.parse(raw) : { name: "", birth: "" };
}
function ageParts(birthStr){
  if(!birthStr) return {years:0, months:0, total:0};
  const n=new Date(birthStr), h=new Date();
  let y=h.getFullYear()-n.getFullYear(), m=h.getMonth()-n.getMonth();
  if(m<0){ y--; m+=12; }
  return {years:y, months:m, total:y*12+m};
}
function renderHeader(){
  const c = getChild();
  const title = document.getElementById('childTitle');
  const ageEl = document.getElementById('childAge');
  if(!title || !ageEl) return;
  const a = ageParts(c.birth);
  title.textContent = c.name ? c.name : 'Pequenos Passos';
  if(c.birth){
    ageEl.textContent = `Idade: ${a.years} ano(s) e ${a.months} mês(es)`;
  }else{
    ageEl.textContent = 'Defina o nome e a data de nascimento em Configurações.';
  }
}

// Vacinas (4–6 anos) simples
const VAX_KEY = 'pp_vax_4_6';
const VAX_LIST = [
  { id:'dtp-reforco',  title:'DTP (Reforço)',       when:'4–6 anos' },
  { id:'polio-reforco',title:'Poliomielite (Ref.)', when:'4–6 anos' },
  { id:'mmr',          title:'Sarampo (MMR)',       when:'4–6 anos' },
  { id:'influenza',    title:'Influenza (Anual)',   when:'Anual'    },
  { id:'hepb-reforco', title:'Hepatite B (Ref.)',   when:'4–6 anos' },
];
function readVax(){
  const raw = localStorage.getItem(VAX_KEY);
  if(!raw) return {};
  try{ return JSON.parse(raw); } catch{ return {}; }
}
function writeVax(state){ localStorage.setItem(VAX_KEY, JSON.stringify(state)); }
function toggleVax(id){
  const st = readVax();
  st[id] = st[id] === 'aplicada' ? 'pendente' : 'aplicada';
  writeVax(st);
  renderVax();
}
function resetVax(){
  localStorage.removeItem(VAX_KEY);
  renderVax();
}
function renderVax(){
  const ul = document.getElementById('vaxList'); if(!ul) return;
  const st = readVax();
  ul.innerHTML = '';
  VAX_LIST.forEach(v=>{
    const status = st[v.id] || 'pendente';
    const li = document.createElement('li'); li.className = 'vax-item';
    li.innerHTML = `
      <div class="vax-title">
        <span>🩹</span>
        <div>
          <div><strong>${v.title}</strong></div>
          <div class="vax-tag">${v.when}</div>
        </div>
      </div>
      <button class="vax-status ${status==='aplicada'?'vax-aplicada':'vax-pendente'}">
        ${status==='aplicada' ? 'Aplicada' : 'Pendente'}
      </button>
    `;
    li.querySelector('button').addEventListener('click', ()=>toggleVax(v.id));
    ul.appendChild(li);
  });
  const resetBtn = document.getElementById('resetVax');
  if(resetBtn){ resetBtn.onclick = resetVax; }
}

// Conteúdos (você pode editar/expandir livremente)
const BOOKS = [
  { title:'A Lagarta Comilona', author:'Eric Carle', link:'https://www.google.com/search?q=A+Lagarta+Comilona' },
  { title:'O Pequeno Príncipe', author:'Antoine de Saint-Exupéry', link:'https://www.google.com/search?q=O+Pequeno+Pr%C3%ADncipe+livro' },
  { title:'A Árvore Generosa', author:'Shel Silverstein', link:'https://www.google.com/search?q=A+%C3%81rvore+Generosa' },
];
const VIDEOS = [
  { title:'Como ajudar seu filho a ler (dicas práticas)', link:'https://www.youtube.com/results?search_query=como+ajudar+crian%C3%A7a+a+ler' },
  { title:'Desenvolvimento infantil 4–6 anos', link:'https://www.youtube.com/results?search_query=desenvolvimento+infantil+4+6+anos' },
];
const TIPS = [
  'Separe 10–15 min/dia para leitura compartilhada.',
  'Brincadeiras com rimas ajudam na consciência fonológica.',
  'Atividades motoras finas (recortar, rasgar, modelar) apoiam a escrita.',
  'Conte o dia juntos (sequências, rotina) para desenvolver linguagem e memória.',
];

function renderList(id, items, renderFn){
  const ul = document.getElementById(id); if(!ul) return;
  ul.innerHTML = '';
  items.forEach(it => ul.appendChild(renderFn(it)));
}
function liLink(text, sub, url){
  const li = document.createElement('li'); li.className = 'item';
  li.innerHTML = `<div><strong>${text}</strong>${sub?`<div class="subtitle">${sub}</div>`:''}</div>
                  <a class="btn ghost" href="${url}" target="_blank" rel="noopener">Abrir</a>`;
  return li;
}
function liText(text){
  const li = document.createElement('li'); li.className = 'item';
  li.innerHTML = `<div>${text}</div>`;
  return li;
}

// Boot
document.addEventListener('DOMContentLoaded', ()=>{
  renderHeader();
  renderVax();
  renderList('booksList', BOOKS, b => liLink(b.title, b.author, b.link));
  renderList('videosList', VIDEOS, v => liLink(v.title, 'YouTube', v.link));
  renderList('tipsList', TIPS, t => liText(`• ${t}`));
});
