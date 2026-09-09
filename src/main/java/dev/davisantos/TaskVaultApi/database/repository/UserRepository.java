package dev.davisantos.TaskVaultApi.database.repository;

import dev.davisantos.TaskVaultApi.database.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
