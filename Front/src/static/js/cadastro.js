// cadastro.js — validação básica do formulário de criação de conta

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("formCadastro");
  const erro = document.getElementById("mensagemErro");

  form.addEventListener("submit", e => {
    e.preventDefault();

    const nome = document.getElementById("nome").value.trim();
    const email = document.getElementById("email").value.trim();
    const senha = document.getElementById("senha").value;
    const confirmar = document.getElementById("confirmarSenha").value;

    if (!nome || !email || !senha || !confirmar) {
      erro.textContent = "Por favor, preencha todos os campos.";
      return;
    }

    if (senha.length < 8) {
      erro.textContent = "A senha deve ter pelo menos 8 caracteres.";
      return;
    }

    if (senha !== confirmar) {
      erro.textContent = "As senhas não coincidem.";
      return;
    }

    // Simula criação de conta (poderia ser um POST futuramente)
    const usuario = { nome, email, senha };
    localStorage.setItem("pp_usuario", JSON.stringify(usuario));

    erro.style.color = "green";
    erro.textContent = "Conta criada com sucesso! Redirecionando...";

    setTimeout(() => {
      window.location.href = "/SRC/TEMPLATE/login.html";
    }, 1500);
  });
});
