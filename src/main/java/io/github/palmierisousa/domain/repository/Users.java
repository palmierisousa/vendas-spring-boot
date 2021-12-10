package io.github.palmierisousa.domain.repository;

import io.github.palmierisousa.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Users extends JpaRepository<User, Integer> {
    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);
}
