package dev.uttar.JournalApplication.controller;

import dev.uttar.JournalApplication.entities.User;
import dev.uttar.JournalApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<String> homePage()
    {
        try {
            return new ResponseEntity<>("Welcome to The Journal Entry Application", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not load Page",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add-user")
    public ResponseEntity<String> addUser(@RequestBody User user)
    {
        userService.saveUser(user);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @PostMapping("/add-admin")
    public ResponseEntity<String> addAdmin(@RequestBody User user)
    {
        userService.saveAdmin(user);
        return new ResponseEntity<>("Admin created", HttpStatus.CREATED);
    }
}
