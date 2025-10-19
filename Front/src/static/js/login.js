// login.js — valida credenciais e simula autenticação local

document.addEventListener('DOMContentLoaded', () => {
  const btnLogin = document.getElementById('botaoLogin');
  const campoEmail = document.getElementById('email');
  const campoSenha = document.getElementById('senha');
  const mensagemErro = document.getElementById('mensagemErro');

  // simulação simples: dados de usuário já "cadastrado"
  const usuarioSalvo = JSON.parse(localStorage.getItem('pp_usuario')) || {
    email: 'teste@exemplo.com',
    senha: '12345678',
  };

  function validarLogin() {
    const email = campoEmail.value.trim();
    const senha = campoSenha.value.trim();

    if (!email || !senha) {
      mostrarErro('Preencha todos os campos.');
      return;
    }

    if (email === usuarioSalvo.email && senha === usuarioSalvo.senha) {
      localStorage.setItem('pp_logado', JSON.stringify({ email, dataLogin: new Date().toISOString() }));
      mensagemErro.textContent = '';
      window.location.href = '/SRC/TEMPLATE/index.html'; // redireciona para a home
    } else {
      mostrarErro('E-mail ou senha incorretos.');
    }
  }

  function mostrarErro(texto) {
    mensagemErro.textContent = texto;
    mensagemErro.classList.add('visivel');
    setTimeout(() => mensagemErro.classList.remove('visivel'), 4000);
  }

  btnLogin.addEventListener('click', validarLogin);

  // permite apertar Enter para enviar
  campoSenha.addEventListener('keyup', e => {
    if (e.key === 'Enter') validarLogin();
  });
});
