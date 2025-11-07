import { post } from "./conectaApi.js";

const form = document.getElementById("formLogin");
const errorDiv = document.getElementById("error");

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  errorDiv.style.display = "none";

  const email = document.getElementById("email").value.trim();
  const senha = document.getElementById("senha").value.trim();

  // 🔹 Validação simples
  if (!email || !senha) {
    errorDiv.textContent = "Por favor, preencha todos os campos.";
    errorDiv.style.display = "block";
    return;
  }

  try {
    // 🔹 Faz POST para o backend via proxy (→ http://localhost:8080/api/usuarios/login)
    const resposta = await post("/usuarios/login", { email, senha });

    console.log("✅ Login bem-sucedido:", resposta);

    // 🔹 Armazena o usuário logado no localStorage
    localStorage.setItem("usuario", JSON.stringify(resposta.usuario));

    // 🔹 Redireciona para o dashboard
    window.location.href = "/SRC/TEMPLATE/dashboarder.html";
  } catch (erro) {
    console.error("❌ Erro no login:", erro);
    errorDiv.textContent = "Erro ao realizar login. Verifique email e senha.";
    errorDiv.style.display = "block";
  }
});
