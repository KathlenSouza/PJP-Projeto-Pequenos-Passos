// Máscara e validação simples de CPF
function mascaraCPF(valor) {
  valor = valor.replace(/\D/g, "");
  valor = valor.replace(/(\d{3})(\d)/, "$1.$2");
  valor = valor.replace(/(\d{3})(\d)/, "$1.$2");
  valor = valor.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
  return valor;
}

function validarCPF(cpf) {
  cpf = cpf.replace(/\D/g, "");
  if (cpf.length !== 11 || /^([0-9])\1+$/.test(cpf)) return false;
  let soma = 0, resto;
  for (let i = 1; i <= 9; i++) soma += parseInt(cpf.substring(i-1, i)) * (11 - i);
  resto = (soma * 10) % 11;
  if (resto === 10 || resto === 11) resto = 0;
  if (resto !== parseInt(cpf.substring(9, 10))) return false;
  soma = 0;
  for (let i = 1; i <= 10; i++) soma += parseInt(cpf.substring(i-1, i)) * (12 - i);
  resto = (soma * 10) % 11;
  if (resto === 10 || resto === 11) resto = 0;
  if (resto !== parseInt(cpf.substring(10, 11))) return false;
  return true;
}

document.addEventListener("DOMContentLoaded", function() {
  const cpfInput = document.getElementById("cpf");
  if (cpfInput) {
    cpfInput.addEventListener("input", function(e) {
      this.value = mascaraCPF(this.value);
    });
    cpfInput.addEventListener("blur", function() {
      if (this.value && !validarCPF(this.value)) {
        this.setCustomValidity("CPF inválido");
        this.reportValidity();
      } else {
        this.setCustomValidity("");
      }
    });
  }

  // Máscara e validação para número do cartão
  const cartaoInput = document.getElementById("cartao");
  if (cartaoInput) {
    cartaoInput.addEventListener("input", function() {
      let v = this.value.replace(/\D/g, "").slice(0,16);
      v = v.replace(/(\d{4})(?=\d)/g, "$1 ");
      this.value = v;
    });
    cartaoInput.addEventListener("blur", function() {
      if (this.value.replace(/\D/g, "").length !== 16) {
        this.setCustomValidity("Número de cartão inválido");
        this.reportValidity();
      } else {
        this.setCustomValidity("");
      }
    });
  }

  // Máscara e validação para validade MM/AA
  const validadeInput = document.getElementById("validade");
  if (validadeInput) {
    validadeInput.addEventListener("input", function() {
      let v = this.value.replace(/\D/g, "").slice(0,4);
      if (v.length >= 3) v = v.replace(/(\d{2})(\d{1,2})/, "$1/$2");
      this.value = v;
    });
    validadeInput.addEventListener("blur", function() {
      const match = this.value.match(/^(0[1-9]|1[0-2])\/(\d{2})$/);
      if (!match) {
        this.setCustomValidity("Validade inválida (MM/AA)");
        this.reportValidity();
      } else {
        this.setCustomValidity("");
      }
    });
  }

  // Máscara e validação para CVV
  const cvvInput = document.getElementById("cvv");
  if (cvvInput) {
    cvvInput.addEventListener("input", function() {
      this.value = this.value.replace(/\D/g, "").slice(0,3);
    });
    cvvInput.addEventListener("blur", function() {
      if (this.value.length !== 3) {
        this.setCustomValidity("CVV inválido");
        this.reportValidity();
      } else {
        this.setCustomValidity("");
      }
    });
  }

  // Validação simples para nome
  const nomeInput = document.getElementById("nome");
  if (nomeInput) {
    nomeInput.addEventListener("blur", function() {
      if (this.value.trim().length < 5) {
        this.setCustomValidity("Digite o nome completo");
        this.reportValidity();
      } else {
        this.setCustomValidity("");
      }
    });
  }

  // Validação simples para e-mail
  const emailInput = document.getElementById("email");
  if (emailInput) {
    emailInput.addEventListener("blur", function() {
      const re = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;
      if (!re.test(this.value)) {
        this.setCustomValidity("E-mail inválido");
        this.reportValidity();
      } else {
        this.setCustomValidity("");
      }
    });
  }
});
