package br.com.eduardoalmeida.user_crud.repository;

import br.com.eduardoalmeida.user_crud.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
}
