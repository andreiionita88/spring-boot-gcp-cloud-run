package com.example.demo.users;

import com.example.demo.posts.PostController;
import com.example.demo.users.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository repository;

    @Autowired
    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("")
    public void createUser(@RequestBody UserDTO dto) {
        User userModel = new User();
        userModel.setFirstName(dto.getFirstName());
        userModel.setLastName(dto.getLastName());
        userModel.setBirthDate(Instant.parse(dto.getBirthDate()));

        User savedUser = repository.save(userModel);
        logger.info("Created a new user with ID {}", savedUser.getId());
    }


}
