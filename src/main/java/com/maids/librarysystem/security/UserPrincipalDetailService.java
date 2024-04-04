package com.maids.librarysystem.security;


import com.maids.librarysystem.exception.LibraryApplicationException;
import com.maids.librarysystem.model.AppUser;
import com.maids.librarysystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPrincipalDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> byUsername = userRepository.findUserByUsername(username);
        return byUsername.map(UserPrincipal::new).orElseThrow(() -> new LibraryApplicationException(HttpStatus.UNAUTHORIZED, "Username/Password is incorrect!"));
    }
}
