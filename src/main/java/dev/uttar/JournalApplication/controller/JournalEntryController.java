package dev.uttar.JournalApplication.controller;

import dev.uttar.JournalApplication.entities.JournalEntry;
import dev.uttar.JournalApplication.entities.User;
import dev.uttar.JournalApplication.service.JournalEntryService;
import dev.uttar.JournalApplication.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public ResponseEntity<String> homePage()
    {
        try {
            return new ResponseEntity<>("Welcome to The Journal Entry Application", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not load Page",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/entries/{userName}")
    public ResponseEntity<List<JournalEntry>> allEntriesOfUser(@PathVariable String userName)
    {
        try {
            return new ResponseEntity<>(journalEntryService.getAll(userName), HttpStatus.OK);
        }catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/entries/{userName}/add")
    public ResponseEntity<String> addEntry(@RequestBody JournalEntry myEntry,@PathVariable String userName)
    {
        try {
            myEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntry,userName);
            return new ResponseEntity<>("Entry added", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Couldn't add entry",HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/entries/id/{entryId}")
    public ResponseEntity<?> getEntry(@PathVariable ObjectId entryId)
    {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(entryId);
        if(journalEntry.isPresent())
        {
            return new ResponseEntity<>(journalEntryService.findById(entryId),HttpStatus.OK);
        }
        return new ResponseEntity<>("Entry Not found!",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/entries/{userName}/id/{entryId}")
    public ResponseEntity<String> deleteEntry(@PathVariable ObjectId entryId,@PathVariable String userName)
    {
        if(journalEntryService.findById(entryId).isPresent())
        {
            journalEntryService.deleteById(entryId,userName);
            return new ResponseEntity<>("Deleted",HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Entry not found!",HttpStatus.NOT_FOUND);
    }

    @PutMapping("/entries/{userName}/update/id/{entryId}")
    public ResponseEntity<String> updateEntry(@PathVariable ObjectId entryId,@PathVariable String userName,@RequestBody JournalEntry myEntry)
    {

        JournalEntry old = journalEntryService.findById(entryId).orElse(null);
        User user = userService.findByUsername(userName);

        boolean present = false;
        for(JournalEntry j : user.getJournalEntries()) {
            if (j.getId().equals(entryId))
            {
                present = true;
                break;
            }
        }

        if(old!=null && present)
        {
            old.setTitle(myEntry.getTitle()!=null && !myEntry.equals("")? myEntry.getTitle() : old.getTitle());
            old.setContent(myEntry.getContent()!=null && !myEntry.equals("")? myEntry.getContent():old.getContent());
            journalEntryService.saveEntry(old,userName);
            return new ResponseEntity<>("Entry Updated",HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity<>("No Entry Found!",HttpStatus.NOT_FOUND);
        }
    }
}
