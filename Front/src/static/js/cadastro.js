// /src/static/js/cadastro.js
import { conectaApi } from "./conectaApi.js";

const steps = document.querySelectorAll(".step");
const nextBtn = document.querySelector(".next");
const backBtn = document.querySelector(".btn-back");
const form = document.getElementById("cadastroForm");
const errorBox = document.getElementById("error");
let currentStep = 0;

// ========== CÁLCULO AUTOMÁTICO DA IDADE ==========
const dataInput = document.getElementById("dataNascimento");
const idadeInput = document.getElementById("idadeCrianca");

// Função que calcula idade com base na data de nascimento
function calcularIdade(dataNascimento) {
  const hoje = new Date();
  const nascimento = new Date(dataNascimento);

  let idade = hoje.getFullYear() - nascimento.getFullYear();
  const mes = hoje.getMonth() - nascimento.getMonth();

  if (mes < 0 || (mes === 0 && hoje.getDate() < nascimento.getDate())) {
    idade--;
  }
  return idade;
}

// Evento que calcula e valida idade automaticamente
if (dataInput) {
  dataInput.addEventListener("change", function () {
    const idade = calcularIdade(this.value);
    idadeInput.value = idade;

    if (idade < 4 || idade > 6) {
      Swal.fire({
        icon: "warning",
        title: "Idade fora do limite",
        text: "A criança deve ter entre 4 e 6 anos para o cadastro.",
        confirmButtonColor: "#ff5f6d"
      });
      idadeInput.value = "";
    }
  });
}
// ========== ETAPA 1 ==========
nextBtn.addEventListener("click", () => {
  const nome = document.getElementById("nome").value.trim();
  const email = document.getElementById("email").value.trim();
  const senha = document.getElementById("senha").value.trim();
  const confirmarSenha = document.getElementById("confirmarSenha").value.trim();

  if (!nome || !email || !senha || !confirmarSenha) {
    showError("Preencha todos os campos antes de continuar.");
    return;
  }
  if (senha.length < 8) {
    showError("A senha deve ter no mínimo 8 caracteres.");
    return;
  }
  if (senha !== confirmarSenha) {
    showError("As senhas não coincidem!");
    return;
  }

  hideError();
  goToStep(1);
});

// ========== ETAPA 2 ==========
backBtn.addEventListener("click", () => goToStep(0));

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const nome = document.getElementById("nome").value.trim();
  const email = document.getElementById("email").value.trim().toLowerCase();
  const senha = document.getElementById("senha").value.trim();
  const nomeCrianca = document.getElementById("nomeCrianca").value.trim();
  const idadeCrianca = document.getElementById("idadeCrianca").value.trim();

  if (!nomeCrianca || !idadeCrianca) {
    showError("Preencha as informações da criança.");
    return;
  }

  const usuario = { nome, email, senha, nomeCrianca, idadeCrianca };

  try {
    await conectaApi.cadastrarUsuario(usuario);
    Swal.fire({
      icon: "success",
      title: "Conta criada com sucesso!",
      confirmButtonColor: "#28a745"
    }).then(() => {
      window.location.href = "/src/template/login.html";
    });
  } catch (erro) {
    showError(erro.message);
  }
  
});

// ========== Funções auxiliares ==========
function goToStep(index) {
  steps[currentStep].classList.remove("active");
  steps[index].classList.add("active");
  currentStep = index;
}

function showError(msg) {
  errorBox.textContent = msg;
  errorBox.style.display = "block";
}

function hideError() {
  errorBox.style.display = "none";
}

