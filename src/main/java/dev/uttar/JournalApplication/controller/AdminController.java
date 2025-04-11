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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers()
    {
        List<User> users = userService.getAll();
        if(users!=null)
        {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete-user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username)
    {
        User obtained = userService.findByUsername(username);
        if(obtained!=null)
        {
            for(JournalEntry j : obtained.getJournalEntries())
            {
                journalEntryService.deleteById(j.getId(),username);
            }
            userService.deleteByUsername(username);
            return new ResponseEntity<>("User deleted successfully!",HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
