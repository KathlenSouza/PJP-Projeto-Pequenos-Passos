package com.api.pjpPequenosPassos.service;

import com.api.pjpPequenosPassos.model.Tarefa;
import com.api.pjpPequenosPassos.repository.TarefaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    // ==================== CRIAR TAREFA ====================
    @Transactional
    public Tarefa criarTarefa(Tarefa tarefa) {
        if (tarefa.getFaixaEtariaMin() > tarefa.getFaixaEtariaMax()) {
            throw new RuntimeException("Faixa etária mínima não pode ser maior que a máxima");
        }

        if (tarefa.getFaixaEtariaMin() < 4 || tarefa.getFaixaEtariaMax() > 6) {
            throw new RuntimeException("Faixa etária deve estar entre 4 e 6 anos");
        }

        return tarefaRepository.save(tarefa);
    }

    // ==================== BUSCAR POR ID ====================
    @Transactional(readOnly = true)
    public Tarefa buscarPorId(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    }

    // ==================== LISTAR TODAS ATIVAS ====================
    @Transactional(readOnly = true)
    public List<Tarefa> listarAtivas() {
        return tarefaRepository.findByAtivoTrue();
    }

    // ==================== LISTAR TODAS (ATIVAS + DESATIVADAS) ====================
    @Transactional(readOnly = true)
    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    // ==================== BUSCAR POR ÁREA ====================
    @Transactional(readOnly = true)
    public List<Tarefa> buscarPorArea(Tarefa.AreaDesenvolvimento area) {
        return tarefaRepository.findByAreaDesenvolvimento(area);
    }

    // ==================== BUSCAR PERSONALIZADAS POR IDADE ====================
    @Transactional(readOnly = true)
    public List<Tarefa> buscarTarefasPersonalizadas(int idade) {
        if (idade < 4 || idade > 6) {
            throw new RuntimeException("Idade deve estar entre 4 e 6 anos");
        }
        return tarefaRepository.findByFaixaEtaria(idade);
    }

    // ==================== BUSCAR POR ÁREA E IDADE ====================
    @Transactional(readOnly = true)
    public List<Tarefa> buscarPorAreaEIdade(Tarefa.AreaDesenvolvimento area, int idade) {
        if (idade < 4 || idade > 6) {
            throw new RuntimeException("Idade deve estar entre 4 e 6 anos");
        }
        return tarefaRepository.findByAreaAndFaixaEtaria(area, idade);
    }

    // ==================== ATUALIZAR TAREFA ====================
    @Transactional
    public Tarefa atualizarTarefa(Long id, Tarefa tarefaAtualizada) {
        Tarefa tarefa = buscarPorId(id);

        tarefa.setTitulo(tarefaAtualizada.getTitulo());
        tarefa.setDescricao(tarefaAtualizada.getDescricao());
        tarefa.setAreaDesenvolvimento(tarefaAtualizada.getAreaDesenvolvimento());
        tarefa.setCategoria(tarefaAtualizada.getCategoria());
        tarefa.setFaixaEtariaMin(tarefaAtualizada.getFaixaEtariaMin());
        tarefa.setFaixaEtariaMax(tarefaAtualizada.getFaixaEtariaMax());
        tarefa.setNivelDificuldade(tarefaAtualizada.getNivelDificuldade());
        tarefa.setDuracaoEstimadaMinutos(tarefaAtualizada.getDuracaoEstimadaMinutos());
        tarefa.setMateriaisNecessarios(tarefaAtualizada.getMateriaisNecessarios());
        tarefa.setBeneficios(tarefaAtualizada.getBeneficios());

        return tarefaRepository.save(tarefa);
    }

    // ==================== DESATIVAR TAREFA ====================
    @Transactional
    public void desativarTarefa(Long id) {
        Tarefa tarefa = buscarPorId(id);
        tarefa.setAtivo(false);
        tarefaRepository.save(tarefa);
    }

    // ==================== DELETAR TAREFA ====================
    @Transactional
    public void deletarTarefa(Long id) {
        Tarefa tarefa = buscarPorId(id);
        tarefaRepository.delete(tarefa);
    }

    // ==================== SUGESTÕES ====================
    @Transactional(readOnly = true)
    public List<Tarefa> obterSugestoes(int idade, Tarefa.AreaDesenvolvimento areaFoco) {
        if (areaFoco != null) {
            return buscarPorAreaEIdade(areaFoco, idade);
        }
        return buscarTarefasPersonalizadas(idade);
    }

    // ==================== POPULAR TAREFAS BASE ====================
    @Transactional
    public void popularTarefasBase() {
        if (tarefaRepository.count() > 0) {
            return;
        }

        // MOTOR GROSSO
        criarTarefa(Tarefa.builder()
                .titulo("Correr e Pular")
                .descricao("Atividade de correr e pular obstáculos leves.")
                .areaDesenvolvimento(Tarefa.AreaDesenvolvimento.MOTOR_GROSSO)
                .categoria("Coordenação")
                .faixaEtariaMin(4)
                .faixaEtariaMax(6)
                .nivelDificuldade("Fácil")
                .duracaoEstimadaMinutos(10)
                .materiaisNecessarios("Cones, cordas")
                .beneficios("Melhora coordenação, equilíbrio e força")
                .ativo(true)
                .build());

        // MOTOR FINO
        criarTarefa(Tarefa.builder()
                .titulo("Recortar figuras")
                .descricao("Usar tesoura sem ponta para recortar figuras simples.")
                .areaDesenvolvimento(Tarefa.AreaDesenvolvimento.MOTOR_FINO)
                .categoria("Coordenação motora fina")
                .faixaEtariaMin(4)
                .faixaEtariaMax(6)
                .nivelDificuldade("Médio")
                .duracaoEstimadaMinutos(15)
                .materiaisNecessarios("Tesoura sem ponta, papel colorido")
                .beneficios("Aprimora destreza manual e coordenação olho-mão")
                .ativo(true)
                .build());

        // LINGUAGEM
        criarTarefa(Tarefa.builder()
                .titulo("História compartilhada")
                .descricao("Ler uma história e pedir que a criança reconte com suas palavras.")
                .areaDesenvolvimento(Tarefa.AreaDesenvolvimento.LINGUAGEM)
                .categoria("Comunicação")
                .faixaEtariaMin(4)
                .faixaEtariaMax(6)
                .nivelDificuldade("Fácil")
                .duracaoEstimadaMinutos(20)
                .materiaisNecessarios("Livro infantil")
                .beneficios("Estimula vocabulário e expressão verbal")
                .ativo(true)
                .build());

        // COGNITIVO
        criarTarefa(Tarefa.builder()
                .titulo("Sequência de cores")
                .descricao("Ordenar objetos coloridos seguindo uma sequência.")
                .areaDesenvolvimento(Tarefa.AreaDesenvolvimento.COGNITIVO)
                .categoria("Raciocínio lógico")
                .faixaEtariaMin(4)
                .faixaEtariaMax(6)
                .nivelDificuldade("Fácil")
                .duracaoEstimadaMinutos(10)
                .materiaisNecessarios("Blocos coloridos")
                .beneficios("Desenvolve atenção e lógica")
                .ativo(true)
                .build());

        // SOCIAL
        criarTarefa(Tarefa.builder()
                .titulo("Brincadeira em grupo")
                .descricao("Participar de brincadeiras com outras crianças.")
                .areaDesenvolvimento(Tarefa.AreaDesenvolvimento.SOCIAL)
                .categoria("Interação social")
                .faixaEtariaMin(4)
                .faixaEtariaMax(6)
                .nivelDificuldade("Fácil")
                .duracaoEstimadaMinutos(30)
                .materiaisNecessarios("Brinquedos variados")
                .beneficios("Estimula cooperação, empatia e respeito às regras")
                .ativo(true)
                .build());
    }
    
    public List<Tarefa> listarPendentesPorCrianca(Long criancaId) {
        return tarefaRepository.findByCriancaIdAndAtivoTrue(criancaId);
    }
 
}


