package dev.uttar.JournalApplication.controller;

import dev.uttar.JournalApplication.entities.JournalEntry;
import dev.uttar.JournalApplication.entities.User;
import dev.uttar.JournalApplication.service.JournalEntryService;
import dev.uttar.JournalApplication.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/journal-entries/{username}")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> allEntriesOfUser(@PathVariable String username, Model model)
    {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUserName = authentication.getName();
            if(!loggedInUserName.equals(username))
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(journalEntryService.getAll(username), HttpStatus.OK);
        }catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public String addEntry(@PathVariable String username,@RequestParam("title") String title,@RequestParam("content") String content)
    {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUser = authentication.getName();

            if(!loggedInUser.equals(username))
                return "error/403";
            JournalEntry newEntry = new JournalEntry();
            newEntry.setTitle(title);
            newEntry.setContent(content);
            newEntry.setDate(LocalDateTime.now());

            journalEntryService.saveEntry(newEntry, username);
            return "redirect:/user/" + username + "/dashboard";
        } catch (Exception e) {
            return "error/500";
        }
    }

    @GetMapping("/id/{entryId}")
    public ResponseEntity<?> getEntry(@PathVariable ObjectId entryId)
    {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(entryId);
        if(journalEntry.isPresent())
        {
            return new ResponseEntity<>(journalEntryService.findById(entryId),HttpStatus.OK);
        }
        return new ResponseEntity<>("Entry Not found!",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{entryId}")
    public ResponseEntity<String> deleteEntry(@PathVariable ObjectId entryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        if((journalEntryService.findById(entryId).isPresent()))
        {
            journalEntryService.deleteById(entryId,userName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Entry not found!",HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{entryId}")
    public String updateEntry(@PathVariable ObjectId entryId,@RequestParam("title") String title,@RequestParam("content") String content)
    {

        return "Updated";
    }
}
