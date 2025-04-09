package dev.uttar.JournalApplication.service;

import dev.uttar.JournalApplication.entities.JournalEntry;
import dev.uttar.JournalApplication.entities.User;
import dev.uttar.JournalApplication.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public void saveEntry(JournalEntry journalEntry,String userName)
    {
        try {
            User user = userService.findByUsername(userName);
            user.getJournalEntries().add(journalEntry);
            journalEntryRepository.save(journalEntry);
            userService.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<JournalEntry> getAll(String userName)
    {
        User user = userService.findByUsername(userName);
        return user.getJournalEntries();
    }

    public Optional<JournalEntry> findById(ObjectId id)
    {
        return journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id,String userName)
    {
        User user = userService.findByUsername(userName);
        for(JournalEntry j : user.getJournalEntries())
        {
            if(j.getId().equals(id)){
                user.getJournalEntries().remove(j);
                break;
            }
        }
        userService.saveUser(user);
        journalEntryRepository.deleteById(id);
    }
}
