package com.akd.app;

import com.akd.app.model.User;
import com.akd.app.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LogManager.getLogger(DataInitializer.class);

    @Autowired
    UserRepository users;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${user.password}")
    private String userPassword;
    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        User user = new User("user", this.passwordEncoder.encode(userPassword), Collections.singletonList("ROLE_USER"));
        this.users.save(user);

        User admin = new User("admin", this.passwordEncoder.encode(adminPassword), Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
        this.users.save(admin);

        logger.debug("printing all users...");
        this.users.findAll().forEach(v -> logger.debug(" User :" + v.toString()));
    }
}
