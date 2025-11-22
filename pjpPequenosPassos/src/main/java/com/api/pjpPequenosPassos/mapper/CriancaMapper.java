package com.api.pjpPequenosPassos.mapper;

import com.api.pjpPequenosPassos.dto.CriancaDTO;
import com.api.pjpPequenosPassos.model.Crianca;

public class CriancaMapper {

    public static CriancaDTO toDTO(Crianca crianca) {
        if (crianca == null) return null;

        CriancaDTO dto = new CriancaDTO();
        dto.setId(crianca.getId());
        dto.setNome(crianca.getNome());
        dto.setDataNascimento(crianca.getDataNascimento());
        dto.setSexo(crianca.getGenero());
        dto.setIdade(crianca.getIdade());

        return dto;
    }
}
