package com.metabug.persistence.dao;

import com.metabug.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(final String email);

    User findByLogin(final String login);

    List<User> findAll();
}
