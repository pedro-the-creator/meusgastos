package com.example.meusgastoscaldas.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.meusgastoscaldas.domain.model.Usuario;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{
    private JwtUtil jwtUtil;
    private UserDetailsSecurityServer userDetailsSecurityServer;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsSecurityServer userDetailsSecurityServer) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsSecurityServer = userDetailsSecurityServer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException, java.io.IOException{
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            UsernamePasswordAuthenticationToken auth = getAuthenticationToken(header.substring(7));
            if(auth != null && auth.isAuthenticated()){
                SecurityContextHolder.getContext().setAuthentication(auth);
            } 
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token){
        if(jwtUtil.isValidToken(token)){
            String email = jwtUtil.getUserName(token);
           // UsuarioResponseDTO usuarioResponseDTO = usuarioService.obterPorEmail(email);
           // Usuario usuario = mapper.map(usuarioResponseDTO, Usuario.class);
           Usuario usuario = (Usuario) userDetailsSecurityServer.loadUserByUsername(email);
            return new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        }
        return null;
    }
}
