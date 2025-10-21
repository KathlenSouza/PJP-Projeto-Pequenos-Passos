package com.api.pjpPequenosPassos.service;

import com.api.pjpPequenosPassos.model.*;
import com.api.pjpPequenosPassos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository = null;
    private final PasswordEncoder passwordEncoder = null;

    // ==================== CRIAR USUÁRIO ====================
    @Transactional
    public Usuario criarUsuario(Usuario usuario) {
        // Verificar se email já existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email já cadastrado no sistema");
        }

        // Criptografar senha
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        
        // Salvar usuário
        return usuarioRepository.save(usuario);
    }

    // ==================== BUSCAR POR ID ====================
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // ==================== BUSCAR POR EMAIL ====================
    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // ==================== LISTAR TODOS ====================
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // ==================== LISTAR ATIVOS ====================
    @Transactional(readOnly = true)
    public List<Usuario> listarAtivos() {
        return usuarioRepository.findByAtivoTrue();
    }

    // ==================== ATUALIZAR PERFIL ====================
    @Transactional
    public Usuario atualizarPerfil(Long id, Usuario usuarioAtualizado) {
        Usuario usuario = buscarPorId(id);

        // Atualizar campos (exceto senha e email)
        usuario.setNomeCompleto(usuarioAtualizado.getNomeCompleto());
        usuario.setDataNascimento(usuarioAtualizado.getDataNascimento());
        usuario.setTelefone(usuarioAtualizado.getTelefone());
        usuario.setCidade(usuarioAtualizado.getCidade());
        usuario.setEstado(usuarioAtualizado.getEstado());

        return usuarioRepository.save(usuario);
    }

    // ==================== ATUALIZAR SENHA ====================
    @Transactional
    public void atualizarSenha(Long id, String senhaAntiga, String senhaNova) {
        Usuario usuario = buscarPorId(id);

        // Verificar senha antiga
        if (!passwordEncoder.matches(senhaAntiga, (String) usuario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        // Atualizar senha
        usuario.setSenha(passwordEncoder.encode(senhaNova));
        usuarioRepository.save(usuario);
    }

    // ==================== DESATIVAR CONTA ====================
    @Transactional
    public void desativarConta(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    // ==================== REATIVAR CONTA ====================
    @Transactional
    public void reativarConta(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    // ==================== DELETAR USUÁRIO ====================
    @Transactional
    public void deletarUsuario(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }

    // ==================== BUSCAR POR REGIÃO ====================
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorRegiao(String cidade, String estado) {
        return usuarioRepository.findByCidadeAndEstado(cidade, estado);
    }

    // ==================== VALIDAR LOGIN ====================
    @Transactional(readOnly = true)
    public boolean validarLogin(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email ou senha incorretos"));

        if (!usuario.getAtivo()) {
            throw new RuntimeException("Conta desativada");
        }

        return passwordEncoder.matches(senha, (String) usuario.getSenha());
    }
}