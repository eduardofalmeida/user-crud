package br.com.eduardoalmeida.user_crud.mapper;

import br.com.eduardoalmeida.user_crud.dto.UsuarioDTO;
import br.com.eduardoalmeida.user_crud.model.UsuarioModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toDTO(UsuarioModel usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    UsuarioModel toEntity(UsuarioDTO usuarioDTO);

}
