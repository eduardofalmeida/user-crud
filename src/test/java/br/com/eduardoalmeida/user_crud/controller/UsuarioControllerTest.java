package br.com.eduardoalmeida.user_crud.controller;

import br.com.eduardoalmeida.user_crud.dto.UsuarioDTO;
import br.com.eduardoalmeida.user_crud.service.UsuarioService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Criar usuário retorna status 201 e corpo correto")
    @Test
    void criarUsuarioRetornaStatus201ECorpoCorreto() {
        UsuarioDTO usuarioDTO = new UsuarioDTO(1l, "João", "joao@email.com", LocalDateTime.now());
        when(usuarioService.criarUsuario(any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.criarUsuario(usuarioDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João", response.getBody().nome());
        assertEquals("joao@email.com", response.getBody().email());
        verify(usuarioService).criarUsuario(usuarioDTO);
    }

    @DisplayName("Listar usuários retorna status 200 e página de usuários")
    @Test
    void listarUsuariosRetornaStatus200EPaginaDeUsuarios() {
        Pageable pageable = PageRequest.of(0, 10);
        UsuarioDTO usuarioDTO = new UsuarioDTO(1l, "João", "joao@email.com", LocalDateTime.now());
        Page<UsuarioDTO> page = new PageImpl<>(List.of(usuarioDTO));
        when(usuarioService.listarUsuarios(pageable)).thenReturn(page);

        ResponseEntity<Page<UsuarioDTO>> response = usuarioController.listarUsuarios(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("João", response.getBody().getContent().get(0).nome());
        verify(usuarioService).listarUsuarios(pageable);
    }

    @DisplayName("Buscar usuário por ID retorna status 200 e usuário correto")
    @Test
    void buscarPorIdRetornaStatus200EUsuarioCorreto() {
        Long id = 1L;
        UsuarioDTO usuarioDTO = new UsuarioDTO(1l, "João", "joao@email.com", LocalDateTime.now());
        when(usuarioService.buscarPorId(id)).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.buscarPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João", response.getBody().nome());
        assertEquals("joao@email.com", response.getBody().email());
        verify(usuarioService).buscarPorId(id);
    }

    @DisplayName("Atualizar usuário retorna status 200 e usuário atualizado")
    @Test
    void atualizarUsuarioRetornaStatus200EUsuarioAtualizado() {
        Long id = 1L;
        UsuarioDTO usuarioDTO = new UsuarioDTO(1l, "João Atualizado", "joao.atualizado@email.com", LocalDateTime.now());
        when(usuarioService.atualizarUsuario(eq(id), any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.atualizarUsuario(id, usuarioDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João Atualizado", response.getBody().nome());
        assertEquals("joao.atualizado@email.com", response.getBody().email());
        verify(usuarioService).atualizarUsuario(id, usuarioDTO);
    }

}