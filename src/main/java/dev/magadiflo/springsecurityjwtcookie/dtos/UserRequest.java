package dev.magadiflo.springsecurityjwtcookie.dtos;

import dev.magadiflo.springsecurityjwtcookie.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequest {
    private Long id;
    private String username;
    private String password;
    private Set<UserRole> roles;
}
