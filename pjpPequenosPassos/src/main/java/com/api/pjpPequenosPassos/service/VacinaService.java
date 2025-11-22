package com.api.pjpPequenosPassos.service;

import com.api.pjpPequenosPassos.model.Vacina;
import com.api.pjpPequenosPassos.repository.VacinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacinaService {

    private final VacinaRepository repo;

    public VacinaService(VacinaRepository repo) {
        this.repo = repo;
    }

    public Vacina salvar(Vacina vacina) {
        return repo.save(vacina);
    }

    public List<Vacina> listarPorCrianca(Long criancaId) {
        return repo.findByCriancaId(criancaId);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}
