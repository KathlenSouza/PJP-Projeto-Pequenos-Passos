package com.api.pjpPequenosPassos.mapper;

import com.api.pjpPequenosPassos.dto.CriancaDTO;
import com.api.pjpPequenosPassos.dto.UsuarioDTO;
import com.api.pjpPequenosPassos.model.Usuario;
import java.util.stream.Collectors;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioDTO dto = new UsuarioDTO();

        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());

        if (usuario.getCriancas() != null) {
            dto.setCriancas(
                usuario.getCriancas()
                       .stream()
                       .map(cri -> {
                           CriancaDTO c = new CriancaDTO();
                           c.setId(cri.getId());
                           c.setNome(cri.getNome());
                           c.setDataNascimento(cri.getDataNascimento());
                           c.setSexo(cri.getGenero());
                           c.setIdade(cri.getIdade());
                           return c;
                       })
                       .collect(Collectors.toList())
            );
        }

        return dto;
    }


}

