package dev.davisantos.TaskVaultApi.mapper;

import dev.davisantos.TaskVaultApi.database.model.RoleEntity;
import dev.davisantos.TaskVaultApi.dto.RoleResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponseDTO toDto(RoleEntity entity);
}
