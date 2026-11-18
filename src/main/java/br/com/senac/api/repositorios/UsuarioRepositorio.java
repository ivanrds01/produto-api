package br.com.senac.api.repositorios;

import br.com.senac.api.entidades.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuarios, Long> {
    Optional<Usuarios> findByEmail(String email);
}
