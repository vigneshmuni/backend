package com.klef.jfsd.springboot.controller;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.klef.jfsd.springboot.model.User;
import com.klef.jfsd.springboot.services.CourseService;
import com.klef.jfsd.springboot.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CourseService courseService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletRequest request) {
        User loggedInUser = userService.loginUser(user.getEmail(), user.getPassword());
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        
        HttpSession session = request.getSession();
        session.setAttribute("user", loggedInUser);
        return ResponseEntity.ok(loggedInUser);
    }

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Long>> getStatistics() {
        Map<String, Long> stats = userService.countUsersByRole();
        return ResponseEntity.ok(stats);
    }
    @GetMapping("/students")
    public ResponseEntity<List<User>> getAllStudents() {
        List<User> students = userService.findAllByRole("STUDENT");
        return ResponseEntity.ok(students);
    }
    @GetMapping("/faculties")
    public ResponseEntity<List<User>> getAllFaculties() {
        List<User> faculties = userService.findAllByRole("FACULTY");
        return ResponseEntity.ok(faculties);
    }
    @GetMapping("/admins")
    public ResponseEntity<List<User>> getAllAdmin() {
        List<User> admins = userService.findAllByRole("ADMIN");
        return ResponseEntity.ok(admins);
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{id}/{courseId}/enroll")
    public ResponseEntity<String> enrollInCourse(
            @PathVariable Long id, 
            @PathVariable Long courseId) {
        try {
            boolean success = courseService.enrollUserInCourse(id, courseId);
            if (success) {
                return ResponseEntity.ok("Successfully enrolled in the course!");
            } else {
                return ResponseEntity.badRequest().body("Enrollment failed. Check user or course details.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
}
