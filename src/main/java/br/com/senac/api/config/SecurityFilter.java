package br.com.senac.api.config;

import br.com.senac.api.entidades.Usuarios;
import br.com.senac.api.jwt.JwtService;
import br.com.senac.api.repositorios.UsuarioRepositorio;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.recuperarToken(request);
        String login = jwtService.validarToken(token);

        if (login != null){
            Optional<Usuarios> usuario = usuarioRepositorio.findByEmail(login);

            //validar usuario
            if (usuario.isEmpty()){
                throw new RemoteException("Usuario nao encontrado");
            }

            UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken
                    (usuario.get(), null,null);

            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        }

        filterChain.doFilter(request,response);
    }


    private String recuperarToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");

        if (header == null) return null;

        return header.replace("Bearer ","");
    }
}
