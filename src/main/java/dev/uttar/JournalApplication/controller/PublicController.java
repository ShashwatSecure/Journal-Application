package dev.uttar.JournalApplication.controller;

import dev.uttar.JournalApplication.entities.User;
import dev.uttar.JournalApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homePage()
    {
        return "homepage";
    }

    @GetMapping("/login")
    public String loginPage()
    {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage()
    {
        return "signup";
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
