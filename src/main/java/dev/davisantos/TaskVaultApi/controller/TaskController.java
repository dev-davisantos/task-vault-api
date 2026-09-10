package dev.davisantos.TaskVaultApi.controller;

import dev.davisantos.TaskVaultApi.dto.TaskActionDTO;
import dev.davisantos.TaskVaultApi.dto.TaskRequestDTO;
import dev.davisantos.TaskVaultApi.dto.TaskResponseDTO;
import dev.davisantos.TaskVaultApi.service.TaskService;
import dev.davisantos.TaskVaultApi.utils.GenericController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController implements GenericController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createUser(@RequestBody TaskRequestDTO dto) {
        TaskResponseDTO response =  taskService.createTask(dto);

        URI uri = buildUri(response.id());

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateUser(@PathVariable Long id, @RequestBody TaskRequestDTO dto) {
        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<TaskResponseDTO> assignTask(@PathVariable Long id, @RequestBody TaskActionDTO dto, Authentication authentication) {
        return ResponseEntity.ok(taskService.assignTask(id,dto.ownerId(),authentication));
    }

    @PostMapping("/{id}/take")
    public ResponseEntity<TaskResponseDTO> takeTask(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(taskService.takeTask(id, authentication));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<TaskResponseDTO> completeTask(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(taskService.completeTask(id, authentication));
    }
}
