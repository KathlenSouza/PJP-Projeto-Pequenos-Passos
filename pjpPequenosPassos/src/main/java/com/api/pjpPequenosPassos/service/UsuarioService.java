package com.api.pjpPequenosPassos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.pjpPequenosPassos.model.Usuario;
import com.api.pjpPequenosPassos.repository.UsuarioRepository;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==================== CRIAR USUÁRIO ====================
    @Transactional
    public Usuario criarUsuario(Usuario usuario) {
        usuario.setEmail(usuario.getEmail().toLowerCase());

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email já cadastrado no sistema");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
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
        return usuarioRepository.findByEmail(email.toLowerCase())
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

        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setTelefone(usuarioAtualizado.getTelefone());
   
        return usuarioRepository.save(usuario);
    }

    // ==================== ATUALIZAR SENHA ====================
    @Transactional
    public void atualizarSenha(Long id, String senhaAntiga, String senhaNova) {
        Usuario usuario = buscarPorId(id);

        if (!passwordEncoder.matches(senhaAntiga, usuario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(senhaNova));
        usuarioRepository.save(usuario);
    }

    // ==================== DESATIVAR / REATIVAR CONTA ====================
    @Transactional
    public void desativarConta(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

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

 
    // ==================== VALIDAR LOGIN ====================
    @Transactional(readOnly = true)
    public boolean validarLogin(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new RuntimeException("Email ou senha incorretos"));

        if (!usuario.isAtivo()) {
            throw new RuntimeException("Conta desativada");
        }

        return passwordEncoder.matches(senha, usuario.getSenha());
    }
}
