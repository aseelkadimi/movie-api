package com.akd.app.repository;

import com.akd.app.model.User;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    public void testCreateAndGetById() {
        // Given
        User expectedUser = easyRandom.nextObject(User.class);
        expectedUser.setRoles(Collections.singletonList("ADMIN"));
        // When
        User savedUser = userRepository.save(expectedUser);
        User found = userRepository.findById(savedUser.getId()).orElse(null);

        // Then
        Assertions.assertNotNull(found);
        Assertions.assertEquals(savedUser.getId(), found.getId());
        Assertions.assertEquals(savedUser.getUsername(), found.getUsername());
        Assertions.assertArrayEquals(savedUser.getRoles().toArray(), found.getRoles().toArray());
        Assertions.assertEquals(savedUser.getPassword(), found.getPassword());
    }

}