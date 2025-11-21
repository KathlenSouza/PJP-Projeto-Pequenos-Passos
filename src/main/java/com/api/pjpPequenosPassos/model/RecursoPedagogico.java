package com.api.pjpPequenosPassos.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_recurso_pedagogico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoPedagogico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String tipo; // livro, vídeo, dica

    private String titulo;

    private String url;
}
