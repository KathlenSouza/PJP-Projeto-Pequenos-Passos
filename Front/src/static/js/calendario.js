
//precisa ser colocado na tela inicial
// Controle de calendário funcional (adição, edição e exclusão de eventos)

const calendario = document.getElementById("calendar");
const janelaEvento = document.getElementById("eventModal");
const tituloJanela = document.getElementById("modalTitle");
const entradaTituloEvento = document.getElementById("eventTitle");
const tipoEvento = document.getElementById("eventType");
const botaoSalvar = document.getElementById("botaoSalvarEvento");
const botaoExcluir = document.getElementById("botaoExcluirEvento");
const botaoAdicionar = document.getElementById("addEventBtn");

let dataSelecionada = null;
let eventos = JSON.parse(localStorage.getItem("pp_eventos")) || {};

//  Renderiza o calendário
function exibirCalendario() {
  const hoje = new Date();
  const ano = hoje.getFullYear();
  const mes = hoje.getMonth();

  const primeiroDia = new Date(ano, mes, 1);
  const ultimoDia = new Date(ano, mes + 1, 0);

  calendario.innerHTML = "";

  for (let dia = 1; dia <= ultimoDia.getDate(); dia++) {
    const elementoDia = document.createElement("div");
    elementoDia.classList.add("dia-calendario");

    const chaveDia = `${ano}-${mes + 1}-${dia}`;
    elementoDia.textContent = dia;

    if (eventos[chaveDia]) {
      elementoDia.classList.add("evento-existe");
      elementoDia.title = eventos[chaveDia].titulo;
    }

    elementoDia.onclick = () => abrirJanela(chaveDia);
    calendario.appendChild(elementoDia);
  }
}
//  Abre o modal de evento
function abrirJanela(data) {
  dataSelecionada = data;
  const eventoExistente = eventos[data];
  tituloJanela.textContent = eventoExistente ? "Editar Evento" : "Novo Evento";

  entradaTituloEvento.value = eventoExistente ? eventoExistente.titulo : "";
  tipoEvento.value = eventoExistente ? eventoExistente.tipo : "atividade";

  janelaEvento.classList.add("mostrar");
}

//  Fecha o modal de evento

function fecharJanela() {
  janelaEvento.classList.remove("mostrar");
  entradaTituloEvento.value = "";
}

//  Salvar evento

botaoSalvar.onclick = () => {
  if (!dataSelecionada) return;

  eventos[dataSelecionada] = {
    titulo: entradaTituloEvento.value,
    tipo: tipoEvento.value,
  };

  localStorage.setItem("pp_eventos", JSON.stringify(eventos));
  fecharJanela();
  exibirCalendario();
  mostrarAviso("Evento salvo com sucesso!");
};


//  Excluir evento

botaoExcluir.onclick = () => {
  if (dataSelecionada && eventos[dataSelecionada]) {
    delete eventos[dataSelecionada];
    localStorage.setItem("pp_eventos", JSON.stringify(eventos));
    fecharJanela();
    exibirCalendario();
    mostrarAviso("Evento excluído!");
  }
};


//  Adicionar evento rápido

botaoAdicionar.onclick = () => {
  const dataAtual = new Date().toISOString().split("T")[0];
  abrirJanela(dataAtual);
};

// Fecha modal ao clicar fora
janelaEvento.onclick = (e) => e.target === janelaEvento && fecharJanela();


//  Exibir mensagem temporária

function mostrarAviso(mensagem) {
  const aviso = document.createElement("div");
  aviso.className = "aviso-toast";
  aviso.textContent = mensagem;
  document.body.appendChild(aviso);
  setTimeout(() => aviso.remove(), 2500);
}

// Inicializa o calendário
exibirCalendario();
