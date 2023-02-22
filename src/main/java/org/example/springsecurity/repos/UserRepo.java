package org.example.springsecurity.repos;

import org.example.springsecurity.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepo extends PagingAndSortingRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(String username);

    User findById(Long id);

    boolean existsByUsername(String username);
    User save(User user);
}


