//  menu de navegação responsivo (mobile e desktop) menu hambuguer 

document.addEventListener('DOMContentLoaded', () => {
  const botaoMenu = document.getElementById('alternarMenu'); // igual ao HTML
  const listaLinks = document.getElementById('navLinks'); // igual ao HTML

  if (botaoMenu && listaLinks) {
    // Alterna o menu ao clicar no botão ☰
    botaoMenu.addEventListener('click', () => {
      listaLinks.classList.toggle('show'); // classe igual ao CSS (.nav-links.show)
    });

    // Fecha o menu ao clicar em um link
    listaLinks.querySelectorAll('a').forEach(link => {
      link.addEventListener('click', () => {
        listaLinks.classList.remove('show');
      });
    });
  }
});
