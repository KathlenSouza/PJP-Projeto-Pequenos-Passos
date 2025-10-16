const STORAGE_ACT='pp_activities';
const DIMENSIONS=["Motor Grosso","Motor Fino","Linguagem","Cognitivo","Socioemocional"];
const MILESTONES=[
  {description:"Faz-de-conta por 10 min",category:"Cognitivo"},
  {description:"Contar história do dia",category:"Linguagem"},
  {description:"Equilíbrio em linha",category:"Motor Grosso"},
  {description:"Cortar figuras simples",category:"Motor Fino"},
  {description:"Jogo cooperativo simples",category:"Socioemocional"}
];
function readActs(){ try{ return JSON.parse(localStorage.getItem(STORAGE_ACT)||'[]'); }catch{return [];} }
function writeActs(list){ localStorage.setItem(STORAGE_ACT, JSON.stringify(list)); }
function renderActs(){
  const ul=document.getElementById('activitiesList'); const list=readActs(); ul.innerHTML='';
  if(!list.length){ ul.innerHTML='<li class="item">Nenhuma atividade. Use "Sugerir" ou adicione.</li>'; return; }
  list.forEach(it=>{
    const li=document.createElement('li'); li.className='item';
    li.innerHTML=`<div class="center"><input type="checkbox" ${it.completed?'checked':''}>
      <span>${it.description}</span><span class="badge">${it.category}</span></div>
      <button class="btn danger">Excluir</button>`;
    li.querySelector('input').addEventListener('change',e=>{ it.completed=e.target.checked; writeActs(readActs().map(a=>a.id===it.id?it:a)); syncRadar(); renderActs(); });
    li.querySelector('button').addEventListener('click',()=>{ writeActs(readActs().filter(a=>a.id!==it.id)); syncRadar(); renderActs(); });
    ul.appendChild(li);
  });
}
function addAct(){
  const desc=document.getElementById('activityDesc').value.trim();
  const cat=document.getElementById('activityCategory').value;
  if(!desc){ alert('Descreva a atividade'); return; }
  const list=readActs(); list.push({id:Date.now(),description:desc,category:cat,completed:false});
  writeActs(list); document.getElementById('activityDesc').value=''; syncRadar(); renderActs();
}
function suggest(){
  const list=readActs(); MILESTONES.forEach((s,i)=>list.push({id:Date.now()+i, ...s, completed:false})); writeActs(list); syncRadar(); renderActs();
}
function clearAll(){ localStorage.removeItem(STORAGE_ACT); syncRadar(); renderActs(); }
function syncRadar(){
  const acts=readActs(); const tot={}, done={};
  DIMENSIONS.forEach(d=>{tot[d]=0;done[d]=0});
  acts.forEach(a=>{ const d=a.category||"Cognitivo"; tot[d]=(tot[d]||0)+1; if(a.completed) done[d]=(done[d]||0)+1; });
  const values=DIMENSIONS.map(d=> tot[d]? Math.round(done[d]*100/tot[d]) : 0 );
  localStorage.setItem('pp_radar', JSON.stringify({labels:DIMENSIONS, values}));
}
document.addEventListener('DOMContentLoaded',()=>{
  document.getElementById('btnAdd').addEventListener('click', addAct);
  document.getElementById('btnSuggest').addEventListener('click', suggest);
  document.getElementById('btnClearActivities').addEventListener('click', clearAll);
  renderActs(); syncRadar();
});