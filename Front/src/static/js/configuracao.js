// Gerencia e salva as configurações 

const CHAVE_PAIS = 'pp_pais';
const CHAVE_CRIANCA = 'pp_crianca';
const CHAVE_PLANO = 'pp_plano';


// Salvar as configurações

function salvarConfiguracoes() {
  const pais = {
    nome: document.getElementById('nomePais').value.trim(),
    email: document.getElementById('emailPais').value.trim(),
    senha: document.getElementById('senhaPais').value.trim(),
  };

  const crianca = {
    nome: document.getElementById('childName').value.trim(),
    nascimento: document.getElementById('nascimentoCrianca').value,
    sexo: document.getElementById('sexoCrianca').value,
    idade: document.getElementById('idadeCrianca').value,
  };

  const plano = document.querySelector('input[name="plano"]:checked')?.value || 'gratuito';

  localStorage.setItem(CHAVE_PAIS, JSON.stringify(pais));
  localStorage.setItem(CHAVE_CRIANCA, JSON.stringify(crianca));
  localStorage.setItem(CHAVE_PLANO, plano);

  alert('✅ Configurações salvas com sucesso!');
}


// Carregar as configurações

function carregarConfiguracoes() {
  const pais = JSON.parse(localStorage.getItem(CHAVE_PAIS) || '{}');
  document.getElementById('nomePais').value = pais.nome || '';
  document.getElementById('emailPais').value = pais.email || '';
  document.getElementById('senhaPais').value = pais.senha || '';

  const crianca = JSON.parse(localStorage.getItem(CHAVE_CRIANCA) || '{}');
  document.getElementById('childName').value = crianca.nome || '';
  document.getElementById('nascimentoCrianca').value = crianca.nascimento || '';
  document.getElementById('sexoCrianca').value = crianca.sexo || '';
  document.getElementById('idadeCrianca').value = crianca.idade || '';

  const plano = localStorage.getItem(CHAVE_PLANO) || 'gratuito';
  const radioSelecionado = document.querySelector(`input[name="plano"][value="${plano}"]`);
  if (radioSelecionado) radioSelecionado.checked = true;
}


// Limpar as configurações

function limparConfiguracoes() {
  if (confirm('Tem certeza que deseja limpar todas as configurações?')) {
    localStorage.removeItem(CHAVE_PAIS);
    localStorage.removeItem(CHAVE_CRIANCA);
    localStorage.removeItem(CHAVE_PLANO);
    carregarConfiguracoes();
    alert('Configurações apagadas.');
  }
}


// Inicialização

document.addEventListener('DOMContentLoaded', () => {
  carregarConfiguracoes();
  document.getElementById('botaoSalvarCfg').addEventListener('click', salvarConfiguracoes);
  document.getElementById('botaoLimparCfg').addEventListener('click', limparConfiguracoes);
});
