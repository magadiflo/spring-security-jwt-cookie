package dev.magadiflo.springsecurityjwtcookie.services;

import dev.magadiflo.springsecurityjwtcookie.helpers.CustomUserDetails;
import dev.magadiflo.springsecurityjwtcookie.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Ingresando al método loadUserByUsername({})...", username);
        return this.userRepository.findByUsername(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario %s no encontrado".formatted(username)));
    }
}
