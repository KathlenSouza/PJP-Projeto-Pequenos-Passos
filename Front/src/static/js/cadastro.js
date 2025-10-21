document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("cadastroForm");
  const nextBtn = document.querySelector('.next');
  const backBtn = document.querySelector('.btn-back');
  const step1 = document.querySelector('.step-1');
  const step2 = document.querySelector('.step-2');
  const image = document.getElementById('leftImage');
  const errorDiv = document.getElementById('error');

  const imgPasso1 = '/img/imgfundo2.jpg';
  const imgPasso2 = '/img/imgfundo3.jpg';

  // Função auxiliar para mostrar mensagem de erro no div
  function showError(msg) {
    if (!errorDiv) {
      alert(msg); // fallback
      return;
    }
    errorDiv.textContent = msg;
    errorDiv.classList.add('show');
    errorDiv.style.color = '#e74c3c';
  }

  function clearError() {
    if (!errorDiv) return;
    errorDiv.textContent = '';
    errorDiv.classList.remove('show');
  }

  // Ao clicar em "Próximo"
  nextBtn.addEventListener('click', () => {
    clearError();

    const nome = (document.getElementById('nome') || {}).value?.trim() || '';
    const email = (document.getElementById('email') || {}).value?.trim() || '';
    const senha = (document.getElementById('senha') || {}).value || '';
    const confirmar = (document.getElementById('confirmarSenha') || {}).value || '';

    // Verifica campos vazios
    if (!nome || !email || !senha || !confirmar) {
      showError('Preencha todos os campos do passo 1.');
      return;
    }

    // Verifica tamanho mínimo => usar alert conforme pedido
    if (senha.length < 8) {
      alert('A senha deve ter pelo menos 8 caracteres.');
      return;
    }

    // Verifica confirmação igual
    if (senha !== confirmar) {
      showError('As senhas são diferentes.');
      return;
    }

    // Se tudo OK, avança para o passo 2
    step1.classList.remove('active');
    step2.classList.add('active');
    clearError();
    if (image) image.src = imgPasso2;
  });

  // Voltar
  backBtn.addEventListener('click', () => {
    clearError();
    step2.classList.remove('active');
    step1.classList.add('active');
    if (image) image.src = imgPasso1;
  });

  // Limpa mensagem de erro ao digitar nos inputs da etapa 1
  ['nome','email','senha','confirmarSenha'].forEach(id => {
    const el = document.getElementById(id);
    if (el) {
      el.addEventListener('input', () => {
        clearError();
      });
    }
  });

  // Ao submeter (botão Cadastrar)
  form.addEventListener('submit', e => {
    e.preventDefault();
    clearError();

    const nomeCrianca = (document.getElementById('nomeCrianca') || {}).value?.trim() || '';
    const idade = (document.getElementById('idadeCrianca') || {}).value || '';

    if (!nomeCrianca || !idade) {
      showError('Preencha os dados da criança.');
      return;
    }

    const idadeNum = Number(idade);
    if (isNaN(idadeNum) || idadeNum < 4 || idadeNum > 6) {
      showError('A idade da criança deve ser entre 4 e 6 anos.');
      return;
    }

    // Recupera dados do passo 1 novamente (por segurança)
    const nome = (document.getElementById('nome') || {}).value?.trim() || '';
    const email = (document.getElementById('email') || {}).value?.trim() || '';
    const senha = (document.getElementById('senha') || {}).value || '';
    const confirmar = (document.getElementById('confirmarSenha') || {}).value || '';

    if (!nome || !email || !senha || !confirmar) {
      showError('Dados do passo 1 incompletos.');
      // volta para o passo 1 para corrigir
      step2.classList.remove('active');
      step1.classList.add('active');
      if (image) image.src = imgPasso1;
      return;
    }

    if (senha.length < 8) {
      alert('A senha deve ter pelo menos 8 caracteres.');
      step2.classList.remove('active');
      step1.classList.add('active');
      if (image) image.src = imgPasso1;
      return;
    }

    if (senha !== confirmar) {
      showError('As senhas são diferentes.');
      step2.classList.remove('active');
      step1.classList.add('active');
      if (image) image.src = imgPasso1;
      return;
    }

    // Tudo OK: cria usuário (simulação)
    const usuario = {
      nome, email, senha,
      crianca: { nome: nomeCrianca, idade: idadeNum }
    };

    localStorage.setItem('pp_usuario', JSON.stringify(usuario));
    if (errorDiv) {
      errorDiv.style.color = 'green';
      errorDiv.textContent = 'Conta criada com sucesso! Redirecionando...';
      errorDiv.classList.add('show');
    } else {
      alert('Conta criada com sucesso! Redirecionando...');
    }

    setTimeout(() => {
      window.location.href = "/SRC/TEMPLATE/login.html";
    }, 1200);
  });
});
