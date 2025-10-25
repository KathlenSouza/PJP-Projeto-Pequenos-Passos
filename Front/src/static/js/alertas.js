function mostrarAviso(mensagem, tipo = "sucesso") {
  // Verifica se o contêiner existe; se não, cria
  let container = document.getElementById("toastContainer");
  if (!container) {
    container = document.createElement("div");
    container.id = "toastContainer";
    container.className = "toast-container";
    document.body.appendChild(container);
  }

  // Cria o aviso
  const aviso = document.createElement("div");
  aviso.className = `aviso-toast ${tipo}`;
  aviso.textContent = mensagem;

  // Adiciona no topo (último aparece primeiro)
  container.prepend(aviso);

  // Animação de entrada
  setTimeout(() => aviso.style.transform = "translateY(0)", 50);

  // Remove após 3 segundos
  setTimeout(() => {
    aviso.style.opacity = "0";
    aviso.style.transform = "translateY(-20px)";
    setTimeout(() => aviso.remove(), 3000);
  }, 3000);
}

// Exemplo de uso
mostrarAviso("Lembre-se de preencher o nome da criança!", "aviso");
