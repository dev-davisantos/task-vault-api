package dev.davisantos.TaskVaultApi.service;

import dev.davisantos.TaskVaultApi.database.model.TaskEntity;
import dev.davisantos.TaskVaultApi.database.model.UserEntity;
import dev.davisantos.TaskVaultApi.database.repository.TaskRepository;
import dev.davisantos.TaskVaultApi.database.repository.UserRepository;
import dev.davisantos.TaskVaultApi.dto.TaskRequestDTO;
import dev.davisantos.TaskVaultApi.dto.TaskResponseDTO;
import dev.davisantos.TaskVaultApi.exception.NotFoundException;
import dev.davisantos.TaskVaultApi.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDTO createTask(TaskRequestDTO dto) {
        UserEntity requester = userRepository.findById(dto.requesterId())
                .orElseThrow(() -> new NotFoundException("This User could not be found"));

        TaskEntity task = TaskEntity.builder()
                .title(dto.title())
                .description(dto.description())
                .status("OPEN")
                .priority(dto.priority())
                .requester(requester)
                .createdAt(Instant.now())
                .build();

        return taskMapper.toDto(taskRepository.save(task));
    }

    public TaskResponseDTO getTaskById(Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("This Task could not be found"));

        return taskMapper.toDto(task);
    }

    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDto)
                .toList();
    }

    public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO dto) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("This Task could not be found"));

        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setPriority(dto.priority());

        return taskMapper.toDto(task);
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }

}
