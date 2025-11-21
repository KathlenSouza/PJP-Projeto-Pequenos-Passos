package com.api.pjpPequenosPassos.controller;

import com.api.pjpPequenosPassos.dto.CadastroCompletoDTO;
import com.api.pjpPequenosPassos.dto.CriancaDTO;
import com.api.pjpPequenosPassos.dto.UsuarioDTO;
import com.api.pjpPequenosPassos.mapper.UsuarioMapper;
import com.api.pjpPequenosPassos.model.Crianca;
import com.api.pjpPequenosPassos.model.Usuario;
import com.api.pjpPequenosPassos.repository.CriancaRepository;
import com.api.pjpPequenosPassos.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CriancaRepository criancaRepository;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder,
                             CriancaRepository criancaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.criancaRepository = criancaRepository;
    }

    // ==================== LISTAR TODOS ====================
    @GetMapping
    public ResponseEntity<?> listar() {
        List<UsuarioDTO> lista = usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toDTO)
                .toList();

        return ResponseEntity.ok(lista);
    }

    // ==================== BUSCAR POR ID ====================
    @GetMapping("/{id}")
    public ResponseEntity<?> obter(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> ResponseEntity.ok(UsuarioMapper.toDTO(usuario)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ==================== CRIAR USUÁRIO SIMPLES ====================
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody Usuario usuario) {
        try {
            usuario.setId(null);
            usuario.setEmail(usuario.getEmail().toLowerCase());
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

            if (usuario.getCriancas() != null) {
                usuario.getCriancas().forEach(c -> c.setUsuario(usuario));
            }

            Usuario salvo = usuarioRepository.save(usuario);

            UsuarioDTO dto = UsuarioMapper.toDTO(salvo);

            return ResponseEntity
                    .created(URI.create("/usuarios/" + salvo.getId()))
                    .body(dto);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409)
                    .body(Map.of("erro", "E-mail já cadastrado."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("erro", "Erro ao criar usuário."));
        }
    }

    // ==================== ATUALIZAR ====================
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario dados) {
        return usuarioRepository.findById(id).map(existing -> {

            existing.setNome(dados.getNome());
            existing.setEmail(dados.getEmail().toLowerCase());
            existing.setTelefone(dados.getTelefone());

            if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
                existing.setSenha(passwordEncoder.encode(dados.getSenha()));
            }

            try {
                Usuario atualizado = usuarioRepository.save(existing);
                return ResponseEntity.ok(UsuarioMapper.toDTO(atualizado));
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity.status(409).body(Map.of("erro", "E-mail já cadastrado."));
            }

        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ==================== EXCLUIR ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== LOGIN ====================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email") != null ? loginRequest.get("email").toLowerCase() : null;
            String senha = loginRequest.get("senha");

            if (email == null || senha == null || email.isBlank() || senha.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Email e senha são obrigatórios."));
            }

            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("E-mail não encontrado."));

            if (!passwordEncoder.matches(senha, usuario.getSenha())) {
                return ResponseEntity.status(401).body(Map.of("erro", "Senha incorreta."));
            }

            if (!usuario.isAtivo()) {
                return ResponseEntity.status(403).body(Map.of("erro", "Conta desativada."));
            }

            UsuarioDTO dto = UsuarioMapper.toDTO(usuario);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Login bem-sucedido!");
            resposta.put("usuario", dto);

            return ResponseEntity.ok(resposta);

        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("erro", "Erro interno no servidor: " + e.getMessage()));
        }
    }


    @PostMapping("/cadastro-completo")
    public ResponseEntity<?> cadastroCompleto(@RequestBody CadastroCompletoDTO dto) {

        try {
            UsuarioDTO usuarioDTO = dto.getUsuario();
            CriancaDTO criancaDTO = dto.getCrianca();

            if (usuarioDTO == null || criancaDTO == null) {
                return ResponseEntity.badRequest().body("Dados incompletos no DTO.");
            }

            Usuario usuario;

            // ---------- SE FOR ATUALIZAÇÃO ----------
            if (usuarioDTO.getId() != null) {
                usuario = usuarioRepository.findById(usuarioDTO.getId())
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

                usuario.setNome(usuarioDTO.getNome());
                usuario.setEmail(usuarioDTO.getEmail().toLowerCase());
                usuario.setTelefone(usuarioDTO.getTelefone());

                // mantém a senha atual
                // (só troca se o DTO enviar uma)
                if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isBlank()) {
                    usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
                }

            } 
            // ---------- SE FOR CADASTRO NOVO ----------
            else {
                usuario = new Usuario();

                usuario.setNome(usuarioDTO.getNome());
                usuario.setEmail(usuarioDTO.getEmail().toLowerCase());

                // senha obrigatória para novo cadastro
             // Se estiver criando
                if (usuarioDTO.getId() == null) {
                    usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
                }
                // Se estiver atualizando
                else if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isBlank()) {
                    usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
                }
                // Se senha veio null → mantém a senha atual
                else {
                    usuario.setSenha(usuarioRepository.findById(usuarioDTO.getId()).get().getSenha());
                }


                usuario.setTelefone(usuarioDTO.getTelefone());
            }

            // Salva usuário
            Usuario usuarioSalvo = usuarioRepository.save(usuario);

            // ---------- CRIANÇA ----------
            Crianca crianca = new Crianca();
            crianca.setNome(criancaDTO.getNome());
            crianca.setDataNascimento(criancaDTO.getDataNascimento());
            crianca.setGenero(criancaDTO.getSexo());
            crianca.setUsuario(usuarioSalvo);

            criancaRepository.save(crianca);

            UsuarioDTO usuarioResponse = UsuarioMapper.toDTO(usuarioSalvo);

            return ResponseEntity.ok(usuarioResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao realizar cadastro completo.");
        }
    }
}

