package com.maids.librarysystem.config;


import com.maids.librarysystem.model.AppUser;
import com.maids.librarysystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            AppUser appUser = new AppUser();
            appUser.setUsername("admin@maids.com");
            appUser.setPassword(passwordEncoder.encode("password"));
            userRepository.save(appUser);
        }
    }
}
