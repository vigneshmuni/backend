package com.klef.jfsd.springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.klef.jfsd.springboot.model.User;
import com.klef.jfsd.springboot.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email is already registered!");
        }
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user.get();
        } else {
            throw new RuntimeException("Invalid email or password!");
        }
    }

    public Map<String, Long> countUsersByRole() {
        Map<String, Long> roleCounts = new HashMap<>();
        roleCounts.put("students", userRepository.countByRole("STUDENT"));
        roleCounts.put("faculties", userRepository.countByRole("FACULTY"));
        roleCounts.put("admins", userRepository.countByRole("ADMIN"));
        return roleCounts;
    }
    public List<User> findAllByRole(String role) {
        return userRepository.findAllByRole(role);
    }
 // Update user
    public User updateUser(Long id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setFirstName(user.getFirstName());
            updatedUser.setLastName(user.getLastName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setRole(user.getRole());
            return userRepository.save(updatedUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Delete user
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found");
        }
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

   
}
