package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UsuarioServiceImplTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;
    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createUsuarioSuccess(){
        Usuario usuario = new Usuario();
        usuario.setUsername("example");

        when(usuarioRepository.findByUsername("example")).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        ResponseEntity<Usuario> response = usuarioService.createUsuario(usuario);
        assertEquals(ResponseEntity.ok(usuario), response);
    }

    @Test
    public void createUsuarioFailure() {
        Usuario usuario = new Usuario();
        usuario.setUsername("example");
        when(usuarioRepository.findByUsername("example")).thenReturn(Optional.of(usuario));
        ResponseEntity<Usuario> response = usuarioService.createUsuario(usuario);
        assertEquals(ResponseEntity.badRequest().body(null), response);
    }

    @Test
    void getUsuarioByIdSuccess() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> responseEntity = usuarioService.getUsuarioById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(usuario, responseEntity.getBody());
    }

    @Test
    void getUsuarioByIdFailure() {
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Usuario> responseEntity = usuarioService.getUsuarioById(id);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    @Test
    public void updateUsuarioSuccess() {
        Usuario usuarioNew = new Usuario();
        usuarioNew.setUsername("newExample");

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsername("example");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByUsername("newExample")).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        ResponseEntity<Usuario> response = usuarioService.updateUsuario(1L, usuario);

        assertEquals(ResponseEntity.ok(usuario), response);
    }

    @Test
    public void updateUsuarioFailure() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsername("example");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Usuario> response = usuarioService.updateUsuario(1L, usuario);
        assertEquals(ResponseEntity.notFound().build(), response);

    }

    @Test
    public void deleteUsuarioSuccess() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsername("example");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> response = usuarioService.deleteUsuario(1L);

        assertEquals(ResponseEntity.noContent().build(), response);
    }

    @Test
    public void deleteUsuarioFailure() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Usuario> response = usuarioService.deleteUsuario(1L);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void userDetailServiceSuccess() {
        // Arrange
        String username = "example";
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario));

        // Act
        UserDetails userDetails = usuarioService.userDetailService().loadUserByUsername(username);

        // Assert
        assertEquals(username, userDetails.getUsername());
    }


    @Test
    void testGetUsuarioResponseEntityFaillureRol() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        Rol rol = new Rol();
        rol.setIdRol(1L);
        usuario.setRoles(Set.of(rol));

        when(rolRepository.findById(rol.getIdRol())).thenReturn(Optional.empty());

        ResponseEntity<Usuario> responseEntity = usuarioService.getUsuarioResponseEntity(usuario);

        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals(null, responseEntity.getBody());
        verify(usuarioRepository, never()).save(usuario);
    }
}
