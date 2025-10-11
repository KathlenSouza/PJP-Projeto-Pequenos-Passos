document.addEventListener('DOMContentLoaded', () => {
  const toggle = document.getElementById('navToggle');
  const links = document.getElementById('navLinks');
  toggle.addEventListener('click', () => links.classList.toggle('show'));
  links.querySelectorAll('a').forEach(a =>
    a.addEventListener('click', () => links.classList.remove('show'))
  );
});
