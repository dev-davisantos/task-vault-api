package dev.davisantos.TaskVaultApi.mapper;

import dev.davisantos.TaskVaultApi.database.model.TaskEntity;
import dev.davisantos.TaskVaultApi.dto.TaskRequestDTO;
import dev.davisantos.TaskVaultApi.dto.TaskResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskMapper {

    TaskResponseDTO toDto(TaskEntity task);

    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "assignedBy", ignore = true)
    TaskEntity toEntity(TaskRequestDTO dto);
}
