package br.com.relojoaria.service.impl;

import br.com.relojoaria.entity.User;
import br.com.relojoaria.error.exception.NotFoundException;
import br.com.relojoaria.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws NotFoundException {
        User user = userRepository.findByName(username);
        if (user == null) {
            throw new NotFoundException("usuário "+username+" não encontrado");
        }
        return user;
    }

//    public UserInfoDto getCurrentUser(Authentication authentication) {
//        String username = authentication.getName();
//        User currentUser = userRepository.findByUsernameWithProfiles(username)
//                .or(() -> userRepository.findByEmailWithProfiles(username))
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Set<String> profiles = currentUser.getActiveProfileCodes();
//
//        return UserInfoDto.builder()
//                .id(currentUser.getId())
//                .username(currentUser.getUsername())
//                .email(currentUser.getEmail())
//                .name(currentUser.getName())
//                .roles(profiles)
//                .build();
//    }
}
