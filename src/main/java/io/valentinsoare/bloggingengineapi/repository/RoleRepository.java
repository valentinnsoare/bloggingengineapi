package io.valentinsoare.bloggingengineapi.repository;

import io.valentinsoare.bloggingengineapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
