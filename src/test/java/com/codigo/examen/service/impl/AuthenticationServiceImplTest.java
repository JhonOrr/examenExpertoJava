package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.request.SignUpRequest;
import com.codigo.examen.service.JwtService;
import com.codigo.examen.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class AuthenticationServiceImplTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private RolRepository rolRepository;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void signUpUserSuccess() {
        SignUpRequest signUpRequest = getSignUpRequest();
        Rol rol = getRol("User");
        Usuario usuario = getUsuario(signUpRequest, rol);

        when(rolRepository.findByNombreRol("User")).thenReturn(Optional.of(rol));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario userCreated = authenticationService.signUpUser(signUpRequest);

        assertNotNull(userCreated);
        assertEquals(signUpRequest.getUsername(), userCreated.getUsername());
        assertEquals(signUpRequest.getEmail(), userCreated.getEmail());
        assertEquals(signUpRequest.getTelefono(), userCreated.getTelefono());
        assertTrue(userCreated.getRoles().contains(rol));
    }


    @Test
    void testSignUpAdmin() {
        SignUpRequest signUpRequest = getSignUpRequest();
        Rol rol = getRol("Admin");
        Usuario usuario = getUsuario(signUpRequest, rol);

        when(rolRepository.findByNombreRol("Admin")).thenReturn(Optional.of(rol));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario createdUsuario = authenticationService.signUpAdmin(signUpRequest);

        assertNotNull(createdUsuario);
        assertEquals(signUpRequest.getUsername(), createdUsuario.getUsername());
        assertEquals(signUpRequest.getEmail(), createdUsuario.getEmail());
        assertEquals(signUpRequest.getTelefono(), createdUsuario.getTelefono());
        assertTrue(createdUsuario.getRoles().contains(rol));
    }

    //metodos de apoyo
    private SignUpRequest getSignUpRequest(){
        return new SignUpRequest(
                "test_user",
                "12442344",
                "example@gmail.com",
                "48169363"
        );
    }

    public Rol getRol(String rol){
        Rol rolUsuario = new Rol();
        rolUsuario.setNombreRol("Admin");
        return rolUsuario;
    }

    private Usuario getUsuario( SignUpRequest signUpRequest, Rol rol){
        Usuario usuario = new Usuario();
        usuario.setUsername(signUpRequest.getUsername());
        usuario.setEmail(signUpRequest.getEmail());
        usuario.setTelefono(signUpRequest.getTelefono());
        usuario.getRoles().add(rol);
        return usuario;
    }




}
