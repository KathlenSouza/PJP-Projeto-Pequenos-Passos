// loader-pezinhos.js — Remove a animação dos pezinhos após o carregamento completo da página.

window.addEventListener('load', () => {
  const carregando = document.getElementById('carregando');
  if (carregando) {
    // Aguarda o término da animação de saída (1s + 0.6s do CSS)
    setTimeout(() => carregando.remove(), 1700);
  }
});
