package com.api.pjpPequenosPassos.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.api.pjpPequenosPassos.model.Profissional;
import com.api.pjpPequenosPassos.repository.ProfissionalRepository;

@Service
public class ProfissionalService {
	
	private final ProfissionalRepository profissionalRepository;
	
	public ProfissionalService(ProfissionalRepository profissionalRepository) {
		this.profissionalRepository =  profissionalRepository;
	}

	    public Profissional criar(Profissional profissional) {
	        return profissionalRepository.save(profissional);
	    }

	    public List<Profissional> listar() {
	        return profissionalRepository.findAll();
	    }

	    public Profissional buscarPorId(UUID id) {
	        return profissionalRepository.findById(id).orElse(null);
	    }
	    
	    public void deletar(UUID id) {
	        Profissional profissional = profissionalRepository.findById(id).orElse(null);

	        if (profissional == null) {
	            throw new RuntimeException("Profissional não encontrado: " + id);
	        }

	        profissionalRepository.delete(profissional);
	    }

	    public Profissional atualizar(UUID id, Profissional novosDados) {
	        Profissional existente = profissionalRepository.findById(id).orElse(null);

	        if (existente == null) {
	            throw new RuntimeException("Profissional não encontrado: " + id);
	        }

	        // Atualiza os campos
	        existente.setNome(novosDados.getNome());
	        existente.setCidade(novosDados.getCidade());
	        existente.setArea(novosDados.getArea());
	        existente.setContato(novosDados.getContato());
	        existente.setComentario(novosDados.getComentario());
	        existente.setAvaliacao(novosDados.getAvaliacao());
	        existente.setIndicadoPorPais(novosDados.isIndicadoPorPais());

	        return profissionalRepository.save(existente);
	    }
	    
	    // FILTROS
	    public List<Profissional> listarIndicadosPorPais() {
	        return profissionalRepository.findByIndicadoPorPaisTrue();
	    }
	    public List<Profissional> buscarPorCidadeOuArea(String cidade, String area) {

	        // Evita erro se vier valores nulos
	        String filtroCidade = (cidade == null ? "" : cidade);
	        String filtroArea = (area == null ? "" : area);

	        return profissionalRepository
	                .findByCidadeContainingIgnoreCaseOrAreaContainingIgnoreCase(filtroCidade, filtroArea);
	    }
	    
	    
	    public List<Profissional> listarUltimos() {
	        return profissionalRepository.findTop5ByOrderByIdDesc();
	    }
	    
	    public List<Profissional> buscarPorAvaliacaoMinima(Integer nota) {

	        if (nota == null || nota < 0 || nota > 5) {
	            throw new RuntimeException("A avaliação mínima deve estar entre 0 e 5.");
	        }

	        return profissionalRepository.findByAvaliacaoGreaterThanEqual(nota);
	    }
// ordenação por nome, cidade e area
	    public List<Profissional> ordenarPorNome() {
	        return profissionalRepository.findAll(Sort.by("nome").ascending());
	    }

	    public List<Profissional> ordenarPorCidade() {
	        return profissionalRepository.findAll(Sort.by("cidade").ascending());
	    }

	    public List<Profissional> ordenarPorArea() {
	        return profissionalRepository.findAll(Sort.by("area").ascending());
	    }
	    
	    
	    //Busca por nome
	    public List<Profissional> buscarPorNome(String nome) {

	        if (nome == null || nome.trim().isEmpty()) {
	            return List.of(); // retorna vazio, sem erro
	        }

	        return profissionalRepository.findByNomeContainingIgnoreCase(nome);
	    }
	    
	    //Só atualiza o campo que veio preenchido ele NÃO mexe nos outros campos
	    
	    public Profissional atualizarParcial(UUID id, Profissional novosDados) {

	        Profissional existente = profissionalRepository.findById(id).orElse(null);

	        if (existente == null) {
	            throw new RuntimeException("Profissional não encontrado: " + id);
	        }

	        if (novosDados.getNome() != null)
	            existente.setNome(novosDados.getNome());

	        if (novosDados.getCidade() != null)
	            existente.setCidade(novosDados.getCidade());

	        if (novosDados.getArea() != null)
	            existente.setArea(novosDados.getArea());

	        if (novosDados.getContato() != null)
	            existente.setContato(novosDados.getContato());

	        if (novosDados.getComentario() != null)
	            existente.setComentario(novosDados.getComentario());

	        if (novosDados.getAvaliacao() != null)
	            existente.setAvaliacao(novosDados.getAvaliacao());

	        existente.setIndicadoPorPais(novosDados.isIndicadoPorPais());

	        return profissionalRepository.save(existente);
	    }

	    



}