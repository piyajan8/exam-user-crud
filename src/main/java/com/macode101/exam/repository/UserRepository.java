package com.macode101.exam.repository;

import com.macode101.exam.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    void deleteById(Long id);

    boolean existsById(Long id);
}
