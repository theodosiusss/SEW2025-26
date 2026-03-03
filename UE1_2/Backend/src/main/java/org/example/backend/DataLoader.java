package org.example.backend;

import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile({"default"})
public class DataLoader implements CommandLineRunner {

    private final UserRepository benutzerRepository;
    private final PasswordEncoder encoder;

    public DataLoader(UserRepository benutzerRepository, PasswordEncoder encoder) {
        this.benutzerRepository = benutzerRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        String username = "hugo";
        String hash = encoder.encode("password");
        benutzerRepository.findByUsername(username)
                .ifPresentOrElse(user -> {
                    user.setPassword(hash); // reset to known demo password
                    benutzerRepository.save(user);
                }, () -> benutzerRepository.save(new User(username, hash)));
     //   Optional<User> sigma = benutzerRepository.findByUsername("sigma");
    //    sigma.ifPresent(benutzerRepository::delete);
      //  benutzerRepository.save(new User("sigma",encoder.encode("sigma")));
    }
}