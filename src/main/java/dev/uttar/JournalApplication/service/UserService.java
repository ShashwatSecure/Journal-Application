package dev.uttar.JournalApplication.service;

import dev.uttar.JournalApplication.entities.JournalEntry;
import dev.uttar.JournalApplication.entities.User;
import dev.uttar.JournalApplication.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveUser(User user)
    {
        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }

    public void saveAdmin(User user)
    {
        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }

    public void updateUser(User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getId()).orElseThrow();

        if (!updatedUser.getPassword().equals(existingUser.getPassword())) {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(updatedUser);
    }

    public List<User> getAll()
    {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id)
    {
        return userRepository.findById(id);
    }

    public User findByUsername(String userName)
    {
        return userRepository.findByUsername(userName);
    }

    public void deleteById(ObjectId id)
    {
        userRepository.deleteById(id);
    }

    public void deleteByUsername(String username)
    {
        User user = findByUsername(username);
        if(user != null)
        {
            userRepository.delete(user);
        }
    }
}
