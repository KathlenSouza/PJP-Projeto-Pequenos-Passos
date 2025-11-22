package com.api.pjpPequenosPassos.mapper;

import com.api.pjpPequenosPassos.dto.VacinaDTO;
import com.api.pjpPequenosPassos.model.Vacina;

public class VacinaMapper {

    public static VacinaDTO toDTO(Vacina v) {
        VacinaDTO dto = new VacinaDTO();
        dto.setId(v.getId());
        dto.setCriancaId(v.getCriancaId());
        dto.setNome(v.getNome());
        dto.setDataAplicacao(v.getDataAplicacao());
        return dto;
    }
}
