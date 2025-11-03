// script.js
document.addEventListener('DOMContentLoaded', () => {
  // Update year in footer
  document.getElementById('year').textContent = new Date().getFullYear();

  const toggle = document.getElementById('billingToggle');
  const pricingGrid = document.getElementById('pricingGrid');

  // Helper: set mode (monthly/yearly)
  function setMode(yearly) {
    // Update toggle aria
    toggle.setAttribute('aria-checked', yearly ? 'true' : 'false');

    // Update label styles
    const monthlyLabel = document.querySelector('.toggle-labels .monthly');
    const yearlyLabel = document.querySelector('.toggle-labels .yearly');
    if (yearly) {
      monthlyLabel.classList.remove('active');
      yearlyLabel.classList.add('active');
    } else {
      yearlyLabel.classList.remove('active');
      monthlyLabel.classList.add('active');
    }

    // Update all price amounts
    const amounts = document.querySelectorAll('.amount');
    amounts.forEach(el => {
      // data-monthly / data-yearly values set in HTML
      const monthly = Number(el.getAttribute('data-monthly') || 0);
      const yearly = Number(el.getAttribute('data-yearly') || monthly * 12);
      if (yearly) {
        if (yearly === monthly) {
          // same price for free plan or equal
          el.textContent = String(monthly);
        } else {
          el.textContent = String(yearly && yearly !== monthly ? Math.round(yearly / 12) : monthly);
        }
      } else {
        el.textContent = String(monthly);
      }
    });

    // Update duration text
    const durations = document.querySelectorAll('.duration');
    durations.forEach(d => d.textContent = yearly ? '/mês (faturado anualmente)' : '/mês');
  }

  // initial mode = mensal
  let yearly = false;
  setMode(yearly);

  // Toggle on click
  toggle.addEventListener('click', () => {
    yearly = !yearly;
    setMode(yearly);
  });

  // keyboard accessibility
  toggle.addEventListener('keydown', (ev) => {
    if (ev.key === 'Enter' || ev.key === ' ') {
      ev.preventDefault();
      yearly = !yearly;
      setMode(yearly);
    }
  });

  // Optional: handlers for CTA buttons (placeholder)
  document.querySelectorAll('.plan-actions .btn-primary, .plan-actions .btn-outline').forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      const plan = e.target.closest('.plan').querySelector('h2').textContent;
      alert(`Você escolheu o plano: ${plan}. Implementar checkout aqui.`);
    });
  });
});
