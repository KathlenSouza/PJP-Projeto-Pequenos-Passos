// =====================================
// 1. SALVAR (Cadastrar ou Atualizar)
// =====================================
async function salvarConfiguracoes() {
  const usuarioId = localStorage.getItem("usuarioId");

  const payload = {
    usuario: {
      id: usuarioId ? Number(usuarioId) : null,
      nome: document.getElementById('nomePais').value.trim(),
      email: document.getElementById('emailPais').value.trim(),
     senha: document.getElementById('senhaPais').value.trim() || null,
      telefone: null
    },
    crianca: {
      nome: document.getElementById('childName').value.trim(),
      dataNascimento: document.getElementById('nascimentoCrianca').value,
      sexo: document.getElementById('sexoCrianca').value,
      idade: document.getElementById('idadeCrianca').value
    }
  };

  try {
    const response = await fetch("http://localhost:8080/usuarios/cadastro-completo", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (!response.ok) throw new Error("Erro ao salvar dados.");

    const resultado = await response.json();

    // Salvar ID se for novo usuário
    if (resultado.usuario && resultado.usuario.id) {
      localStorage.setItem("usuarioId", resultado.usuario.id);
    }

    Swal.fire({
      icon: "success",
      title: "Configurações salvas com sucesso!",
      confirmButtonColor: "#28a745"
    });

  } catch (erro) {
    Swal.fire({
      icon: "error",
      title: "Erro ao salvar",
      text: erro.message
    });
  }
}



// =====================================
// 2. CARREGAR DADOS DO BACKEND
// =====================================
async function carregarConfiguracoes() {
  const usuarioId = localStorage.getItem("usuarioId");

  if (!usuarioId) {
    console.warn("Nenhum usuário encontrado. Campos ficarão vazios.");
    return;
  }

  try {
    // Buscar usuário no backend
    const respUsuario = await fetch(`http://localhost:8080/usuarios/${usuarioId}`);
    if (!respUsuario.ok) throw new Error("Erro ao carregar usuário.");

    const usuario = await respUsuario.json();

    // Preencher campos do responsável
    document.getElementById('nomePais').value = usuario.nome || '';
    document.getElementById('emailPais').value = usuario.email || '';
    document.getElementById('senhaPais').value = "";

    // Preencher dados da criança (se houver)
    if (usuario.criancas && usuario.criancas.length > 0) {
      const c = usuario.criancas[0];

      document.getElementById('childName').value = c.nome || '';
      document.getElementById('nascimentoCrianca').value = c.dataNascimento || '';
      document.getElementById('sexoCrianca').value = c.sexo || '';
      document.getElementById('idadeCrianca').value = c.idade || '';

      localStorage.setItem("criancaId", c.id);
    }

  } catch (e) {
    console.error(e);
    Swal.fire({
      icon: "error",
      title: "Erro ao carregar dados",
      text: e.message
    });
  }
}



// =====================================
// 3. LIMPAR CAMPOS
// =====================================
function limparConfiguracoes() {
  Swal.fire({
    title: 'Tem certeza?',
    text: 'Isso limpará todos os campos preenchidos.',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#6c757d',
    confirmButtonText: 'Sim, limpar',
    cancelButtonText: 'Cancelar'
  }).then((result) => {
    if (result.isConfirmed) {
      document.getElementById('nomePais').value = '';
      document.getElementById('emailPais').value = '';
      document.getElementById('senhaPais').value = '';
      document.getElementById('childName').value = '';
      document.getElementById('nascimentoCrianca').value = '';
      document.getElementById('sexoCrianca').value = '';
      document.getElementById('idadeCrianca').value = '';

      Swal.fire({
        icon: 'success',
        title: 'Campos limpos',
        timer: 1200,
        showConfirmButton: false
      });
    }
  });
}



// =====================================
// 4. INICIALIZAR
// =====================================
document.addEventListener('DOMContentLoaded', () => {
  carregarConfiguracoes();
  document.getElementById('botaoSalvarCfg').addEventListener('click', salvarConfiguracoes);
  document.getElementById('botaoLimparCfg').addEventListener('click', limparConfiguracoes);
});

