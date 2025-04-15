package dev.uttar.JournalApplication.controller;

import dev.uttar.JournalApplication.entities.JournalEntry;
import dev.uttar.JournalApplication.entities.User;
import dev.uttar.JournalApplication.service.JournalEntryService;
import dev.uttar.JournalApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/user/{username}")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("/dashboard")
    public String dashboard(@PathVariable String username, Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        if(!loggedInUsername.equals(username))
            return "error/403";

        List<JournalEntry> entries = journalEntryService.getAll(username);
        model.addAttribute("username",username);
        model.addAttribute("entries",entries);
        return "user-homepage";
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User userObtained = userService.findByUsername(username);
        userService.deleteByUsername(username);

        User newUser = new User(user.getUsername(),user.getPassword());
        userService.saveUser(newUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
