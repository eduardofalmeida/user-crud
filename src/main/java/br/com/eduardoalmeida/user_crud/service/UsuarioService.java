package br.com.eduardoalmeida.user_crud.service;

import br.com.eduardoalmeida.user_crud.dto.UsuarioDTO;
import br.com.eduardoalmeida.user_crud.exception.ResourceNotFoundException;
import br.com.eduardoalmeida.user_crud.mapper.UsuarioMapper;
import br.com.eduardoalmeida.user_crud.model.UsuarioModel;
import br.com.eduardoalmeida.user_crud.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@RequiredArgsConstructor
@Service
public class UsuarioService {

    public static final String USUARIO_NAO_ENCONTRADO_COM_ID = "Usuário não encontrado com id ";
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;


    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        log.info("Criando um novo usuário com o nome: {}", usuarioDTO.nome());

        if (usuarioRepository.existsByEmail(usuarioDTO.email())) {
            throw new IllegalArgumentException("Email já cadastrado: " + usuarioDTO.email());
        }

        UsuarioModel usuario = usuarioMapper.toEntity(usuarioDTO);
        usuario.setDataCriacao(LocalDateTime.now());
        UsuarioModel salvo = usuarioRepository.save(usuario);

        return usuarioMapper.toDTO(salvo);
    }

    public Page<UsuarioDTO> listarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::toDTO);
    }

    public UsuarioDTO buscarPorId(Long id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO_COM_ID + id));
        return usuarioMapper.toDTO(usuario);
    }


    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO_COM_ID + id));
        usuario.setNome(usuarioDTO.nome());
        usuario.setEmail(usuarioDTO.email());
        UsuarioModel atualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(atualizado);
    }


    public void deletarUsuario(Long id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id " + id));
        usuarioRepository.delete(usuario);
    }

}
