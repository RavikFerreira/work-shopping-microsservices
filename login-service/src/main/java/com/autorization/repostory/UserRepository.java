package com.autorization.repostory;

import com.autorization.models.entity.User;
import com.autorization.models.enums.Role;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByRole(Role role);
}
