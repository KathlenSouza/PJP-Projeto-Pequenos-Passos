package com.api.pjpPequenosPassos.service;

import com.api.pjpPequenosPassos.model.RecursoPedagogico;
import com.api.pjpPequenosPassos.repository.RecursoPedagogicoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecursoPedagogicoService {

    private final RecursoPedagogicoRepository repository;

    public RecursoPedagogicoService(RecursoPedagogicoRepository repository) {
        this.repository = repository;
    }

    // ===============================
    // POPULAR DADOS INICIAIS
    // ===============================
    @PostConstruct
    @Transactional
    public void popularBase() {

        // Evita duplicação
        if (repository.count() > 0) {
            return;
        }

        // LIVROS
        salvar("livro", "A Lagarta Comilona", "Eric Carle",
                "https://www.google.com/search?q=A+Lagarta+Comilona");

        salvar("livro", "O Pequeno Príncipe", "Antoine de Saint-Exupéry",
                "https://www.google.com/search?q=O+Pequeno+Príncipe");

        salvar("livro", "A Árvore Generosa", "Shel Silverstein",
                "https://www.google.com/search?q=A+Árvore+Generosa");

        // VÍDEOS
        salvar("video", "Como ajudar a criança a ler (dicas práticas)", null,
                "https://www.youtube.com/results?search_query=como+ajudar+criança+a+ler");

        salvar("video", "Desenvolvimento infantil 4–6 anos", null,
                "https://www.youtube.com/results?search_query=desenvolvimento+infantil+4+6+anos");

        // DICAS
        salvar("dica", "Leitura compartilhada diária (10–15 min).", null, null);
        salvar("dica", "Brincar com rimas para consciência fonológica.", null, null);
        salvar("dica", "Atividades motoras finas (recortar, modelar, rasgar).", null, null);
        salvar("dica", "Converse sobre as emoções do dia.", null, null);
    }


 // Método auxiliar
    private void salvar(String tipo, String titulo, String autor, String link) {
        RecursoPedagogico r = new RecursoPedagogico();
        r.setTipo(tipo);
        r.setTitulo(titulo);
        r.setAutor(autor);
        r.setLink(link);

        repository.save(r);
    }
}
