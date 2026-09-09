package dev.davisantos.TaskVaultApi.mapper;

import dev.davisantos.TaskVaultApi.database.model.UserEntity;
import dev.davisantos.TaskVaultApi.dto.UserRequestDTO;
import dev.davisantos.TaskVaultApi.dto.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = RoleMapper.class
)
public interface UserMapper {

    UserResponseDTO toDto(UserEntity user);

    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(UserRequestDTO dto);
}
