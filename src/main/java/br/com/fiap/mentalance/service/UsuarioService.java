package br.com.fiap.mentalance.service;

import br.com.fiap.mentalance.dto.UsuarioRegistroRequest;
import br.com.fiap.mentalance.model.Usuario;
import br.com.fiap.mentalance.repository.UsuarioRepository;
import br.com.fiap.mentalance.exception.NegocioException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final OracleProcedureService oracleProcedureService;

    @Transactional
    public Usuario registrarUsuario(UsuarioRegistroRequest request, boolean usarProcedure) {
        validarRegistro(request);

        String senhaEncoded = passwordEncoder.encode(request.getSenha());
        String cargo = request.getCargo() != null && !request.getCargo().isEmpty() 
                ? request.getCargo() 
                : "Usuario";

        if (usarProcedure) {
            // Tenta usar sequence, se não existir usa MAX + 1
            Long nextId;
            try {
                nextId = oracleProcedureService.obterProximoIdSequence("USUARIO_SEQ");
            } catch (Exception e) {
                nextId = usuarioRepository.getNextId();
            }
            
            // Usa a procedure Oracle
            oracleProcedureService.inserirUsuario(nextId, request.getNome(), request.getEmail(), 
                    senhaEncoded, cargo);
            // Busca o usuário recém-criado
            return usuarioRepository.findById(nextId)
                    .orElseThrow(() -> new NegocioException("Erro ao criar usuário"));
        } else {
            // Usa JPA padrão (usa sequence automaticamente)
            Usuario usuario = new Usuario();
            usuario.setNome(request.getNome());
            usuario.setEmail(request.getEmail());
            usuario.setSenha(senhaEncoded);
            usuario.setCargo(cargo);
            return usuarioRepository.save(usuario);
        }
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public long contarUsuarios() {
        return usuarioRepository.count();
    }

    public java.util.List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    private void validarRegistro(UsuarioRegistroRequest request) {
        if (!request.getSenha().equals(request.getConfirmarSenha())) {
            throw new NegocioException("As senhas não conferem");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new NegocioException("E-mail já cadastrado");
        }
    }
}

