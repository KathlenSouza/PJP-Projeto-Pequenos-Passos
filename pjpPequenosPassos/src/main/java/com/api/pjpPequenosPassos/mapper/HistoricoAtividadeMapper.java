package com.api.pjpPequenosPassos.mapper;

import java.util.List;

import com.api.pjpPequenosPassos.dto.HistoricoAtividadeDTO;
import com.api.pjpPequenosPassos.model.HistoricoAtividade;


public class HistoricoAtividadeMapper {

    public static HistoricoAtividadeDTO toDTO(HistoricoAtividade h) {
        if (h == null) return null;

        HistoricoAtividadeDTO dto = new HistoricoAtividadeDTO();

        dto.setId(h.getId()); // UUID
        dto.setCriancaId(h.getCriancaId());
        dto.setDataConclusao(h.getDataConclusao());

        // NOVO: observaçao
        dto.setObservacao(h.getObservacao());

        // Dados da tarefa
        if (h.getTarefa() != null) {
            dto.setTarefaTitulo(h.getTarefa().getTitulo());
            dto.setDescricao(h.getTarefa().getDescricao());
            dto.setCategoria(h.getTarefa().getCategoria());

            if (h.getTarefa().getAreaDesenvolvimento() != null) {
                dto.setAreaDesenvolvimento(
                        h.getTarefa().getAreaDesenvolvimento().name()
                );
            }
        }

        // Status fixo
        dto.setStatus("Concluída");

        return dto;
    }

    public static List<HistoricoAtividadeDTO> toDTOList(List<HistoricoAtividade> lista) {
        return lista.stream()
                .map(HistoricoAtividadeMapper::toDTO)
                .toList();
    }
}
