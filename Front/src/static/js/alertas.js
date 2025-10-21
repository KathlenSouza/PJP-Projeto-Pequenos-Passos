function mostrarAviso(mensagem) {
  const aviso = document.createElement("div");
  aviso.className = "aviso-toast";
  aviso.textContent = mensagem;

  document.body.appendChild(aviso);

  // Animação de desaparecimento
  setTimeout(() => {
    aviso.style.opacity = "0";
    setTimeout(() => aviso.remove(), 400);
  }, 2500);
}
// falta ajustar na tela inicial ..............