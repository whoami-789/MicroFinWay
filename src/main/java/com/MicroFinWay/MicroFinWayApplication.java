package com.MicroFinWay;

import com.MicroFinWay.model.AppUser;
import com.MicroFinWay.model.enums.Role;
import com.MicroFinWay.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MicroFinWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroFinWayApplication.class, args);
    }

    @Bean
    CommandLineRunner init(AppUserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin"));
                admin.setRole(Role.ADMIN);
                repo.save(admin);
            }
        };
    }

}
