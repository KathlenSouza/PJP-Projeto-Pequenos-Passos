package com.api.pjpPequenosPassos.mapper;

import com.api.pjpPequenosPassos.dto.TarefaDTO;
import com.api.pjpPequenosPassos.model.Tarefa;
import java.util.List;
import java.util.stream.Collectors;

public class TarefaMapper {

	public static TarefaDTO toDTO(Tarefa tarefa) {
	    if (tarefa == null) return null;

	    TarefaDTO dto = new TarefaDTO();
	    dto.setId(tarefa.getId());
	    dto.setTitulo(tarefa.getTitulo());
	    dto.setDescricao(tarefa.getDescricao());
	    dto.setAreaDesenvolvimento(tarefa.getAreaDesenvolvimento());
	    dto.setCategoria(tarefa.getCategoria());
	    dto.setFaixaEtariaMin(tarefa.getFaixaEtariaMin());
	    dto.setFaixaEtariaMax(tarefa.getFaixaEtariaMax());
	    dto.setNivelDificuldade(tarefa.getNivelDificuldade());
	    dto.setDuracaoEstimadaMinutos(tarefa.getDuracaoEstimadaMinutos());
	    dto.setMateriaisNecessarios(tarefa.getMateriaisNecessarios());
	    dto.setBeneficios(tarefa.getBeneficios());
	    dto.setAtivo(tarefa.isAtivo());
	    return dto;
	}


    public static List<TarefaDTO> toDTOList(List<Tarefa> tarefas) {
        return tarefas.stream().map(TarefaMapper::toDTO).collect(Collectors.toList());
    }
}
