package dev.magadiflo.springsecurityjwtcookie.repositories;

import dev.magadiflo.springsecurityjwtcookie.models.UserInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUserRepository extends CrudRepository<UserInfo, Long> {
    Optional<UserInfo> findByUsername(String username);

    Optional<UserInfo> findFirstById(Long id);
}
