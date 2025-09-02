package br.com.eduardoalmeida.user_crud.service;

import br.com.eduardoalmeida.user_crud.dto.UsuarioDTO;
import br.com.eduardoalmeida.user_crud.exception.ResourceNotFoundException;
import br.com.eduardoalmeida.user_crud.mapper.UsuarioMapper;
import br.com.eduardoalmeida.user_crud.model.UsuarioModel;
import br.com.eduardoalmeida.user_crud.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Criar um novo usuário com sucesso")
    @Test
    void criarUsuarioComSucesso() {
        UsuarioDTO usuarioDTO = new UsuarioDTO(1l, "João", "joao@email.com", LocalDateTime.now());
        UsuarioModel usuarioModel = new UsuarioModel(1L, "João", "joao@email.com", LocalDateTime.now());
        when(usuarioRepository.existsByEmail(usuarioDTO.email())).thenReturn(false);
        when(usuarioMapper.toEntity(usuarioDTO)).thenReturn(usuarioModel);
        when(usuarioRepository.save(any(UsuarioModel.class))).thenReturn(usuarioModel);
        when(usuarioMapper.toDTO(usuarioModel)).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.criarUsuario(usuarioDTO);

        assertNotNull(resultado);
        assertEquals("João", resultado.nome());
        assertEquals("joao@email.com", resultado.email());
        verify(usuarioRepository).existsByEmail(usuarioDTO.email());
        verify(usuarioRepository).save(any(UsuarioModel.class));
    }

    @DisplayName("Falha ao criar usuário com email já cadastrado")
    @Test
    void falhaAoCriarUsuarioComEmailJaCadastrado() {
        UsuarioDTO usuarioDTO = new UsuarioDTO(1l,"João", "joao@email.com", LocalDateTime.now());
        when(usuarioRepository.existsByEmail(usuarioDTO.email())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.criarUsuario(usuarioDTO));

        assertEquals("Email já cadastrado: joao@email.com", exception.getMessage());
        verify(usuarioRepository).existsByEmail(usuarioDTO.email());
        verify(usuarioRepository, never()).save(any(UsuarioModel.class));
    }

    @DisplayName("Listar usuários com sucesso")
    @Test
    void listarUsuariosComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        UsuarioModel usuarioModel = new UsuarioModel(1L, "João", "joao@email.com", LocalDateTime.now());
        UsuarioDTO usuarioDTO = new UsuarioDTO(1l,"João", "joao@email.com",LocalDateTime.now());
        Page<UsuarioModel> page = new PageImpl<>(List.of(usuarioModel));
        when(usuarioRepository.findAll(pageable)).thenReturn(page);
        when(usuarioMapper.toDTO(usuarioModel)).thenReturn(usuarioDTO);

        Page<UsuarioDTO> resultado = usuarioService.listarUsuarios(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("João", resultado.getContent().get(0).nome());
        verify(usuarioRepository).findAll(pageable);
    }

    @DisplayName("Buscar usuário por ID com sucesso")
    @Test
    void buscarPorIdComSucesso() {
        Long id = 1L;
        UsuarioModel usuarioModel = new UsuarioModel(id, "João", "joao@email.com", LocalDateTime.now());
        UsuarioDTO usuarioDTO = new UsuarioDTO(1l,"João", "joao@email.com", LocalDateTime.now());
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioModel));
        when(usuarioMapper.toDTO(usuarioModel)).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals("João", resultado.nome());
        assertEquals("joao@email.com", resultado.email());
        verify(usuarioRepository).findById(id);
    }

    @DisplayName("Falha ao buscar usuário por ID inexistente")
    @Test
    void falhaAoBuscarPorIdInexistente() {
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscarPorId(id));

        assertEquals("Usuário não encontrado com id 1", exception.getMessage());
        verify(usuarioRepository).findById(id);
    }

    @DisplayName("Atualizar usuário com sucesso")
    @Test
    void atualizarUsuarioComSucesso() {
        Long id = 1L;
        UsuarioDTO usuarioDTO = new UsuarioDTO(1l,"João Atualizado", "joao.atualizado@email.com",LocalDateTime.now());
        UsuarioModel usuarioModel = new UsuarioModel(id, "João", "joao@email.com", LocalDateTime.now());
        UsuarioModel atualizadoModel = new UsuarioModel(id, "João Atualizado", "joao.atualizado@email.com", LocalDateTime.now());
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioModel));
        when(usuarioRepository.save(any(UsuarioModel.class))).thenReturn(atualizadoModel);
        when(usuarioMapper.toDTO(atualizadoModel)).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.atualizarUsuario(id, usuarioDTO);

        assertNotNull(resultado);
        assertEquals("João Atualizado", resultado.nome());
        assertEquals("joao.atualizado@email.com", resultado.email());
        verify(usuarioRepository).findById(id);
        verify(usuarioRepository).save(any(UsuarioModel.class));
    }

    @DisplayName("Falha ao atualizar usuário inexistente")
    @Test
    void falhaAoAtualizarUsuarioInexistente() {
        Long id = 1L;
        UsuarioDTO usuarioDTO = new UsuarioDTO(1L, "João Atualizado", "joao.atualizado@email.com",LocalDateTime.now());
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> usuarioService.atualizarUsuario(id, usuarioDTO));

        assertEquals("Usuário não encontrado com id 1", exception.getMessage());
        verify(usuarioRepository).findById(id);
        verify(usuarioRepository, never()).save(any(UsuarioModel.class));
    }

    @DisplayName("Deletar usuário com sucesso")
    @Test
    void deletarUsuarioComSucesso() {
        Long id = 1L;
        UsuarioModel usuarioModel = new UsuarioModel(id, "João", "joao@email.com", LocalDateTime.now());
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioModel));

        assertDoesNotThrow(() -> usuarioService.deletarUsuario(id));

        verify(usuarioRepository).findById(id);
        verify(usuarioRepository).delete(usuarioModel);
    }

    @DisplayName("Falha ao deletar usuário inexistente")
    @Test
    void falhaAoDeletarUsuarioInexistente() {
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> usuarioService.deletarUsuario(id));

        assertEquals("Usuário não encontrado com id 1", exception.getMessage());
        verify(usuarioRepository).findById(id);
        verify(usuarioRepository, never()).delete(any(UsuarioModel.class));
    }
}