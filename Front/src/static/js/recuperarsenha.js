 <script>
    const form = document.getElementById("recuperarForm");
    const msg = document.getElementById("msg");

    form.addEventListener("submit", (e) => {
      e.preventDefault();
      const email = document.getElementById("email").value.trim();

      if (!email) {
        msg.textContent = "Por favor, digite seu e-mail.";
        msg.className = "msg erro";
        return;
      }

      // Simula envio (você pode depois integrar com back-end)
      msg.textContent = "Se o e-mail existir, enviaremos o link de recuperação.";
      msg.className = "msg sucesso";
      form.reset();
    });
  </script>