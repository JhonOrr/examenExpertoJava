package com.codigo.examen.service.impl;

import com.codigo.examen.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JwtServiceImplTest {

    @Mock
    private Claims claims;
    @Mock
    private UserDetails userDetails;
    @InjectMocks
    private JwtService jwtService = new JwtServiceImpl();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void generateTokenSuccess() {
        UserDetails userDetails = getUser("username", "password", "User");
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertNotEquals(0, token.length());
    }


    @Test
    void validateTokenSuccess() {
        UserDetails userDetails = getUser("username", "password", "User");
        JwtServiceImpl jwtService = new JwtServiceImpl();
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.validateToken(token, userDetails);
        assertTrue(isValid);
    }

//    @Test
//    void validateTokenExpires(){
//        String expiredToken = generateExpiredToken();
//        when(userDetails.getUsername()).thenReturn("testUser");
//        boolean isValid = jwtService.validateToken(expiredToken, userDetails);
//        assertFalse(isValid);
//    }

    @Test
    void extractUserName() {
        UserDetails userDetails = getUser("username", "password", "User");
        JwtServiceImpl jwtService = new JwtServiceImpl();
        String token = jwtService.generateToken(userDetails);

        String extractedUsername = jwtService.extractUserName(token);

        assertEquals(userDetails.getUsername(), extractedUsername);
    }

    private UserDetails getUser(String username, String password, String rol) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(rol));
        return new User(username, password, authorities);
    }

    private String generateExpiredToken() {
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() - 120000))
                .compact();
    }

    private Key getSignKey(){
        byte[] key = Decoders.BASE64.decode("85732b878c0f544da4a863804775ef3914e8ccb82b08820a278302c5b826e291");
        return Keys.hmacShaKeyFor(key);
    }
}
