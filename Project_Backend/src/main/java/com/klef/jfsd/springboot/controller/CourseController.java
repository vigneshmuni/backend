package com.klef.jfsd.springboot.controller;

import com.klef.jfsd.springboot.model.Course;
import com.klef.jfsd.springboot.model.User;
import com.klef.jfsd.springboot.services.CourseService;
import com.klef.jfsd.springboot.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/courses")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")  // Enable CORS for the frontend React app
public class CourseController {

    @Autowired
    private CourseService courseService;
    
    @Autowired
    private UserService userService;

    // Create a new course
    @PostMapping("/create-course")
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    // Get all courses
    @GetMapping("/course-list")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    // Get courses by faculty/instructor
    // Update a course
    @PutMapping("/update/{courseId}")
    public Course updateCourse(@PathVariable Long courseId, @RequestBody Course updatedCourse) {
        return courseService.updateCourse(courseId, updatedCourse);
    }

    // Delete a course
    @DeleteMapping("/delete/{courseId}")
    public String deleteCourse(@PathVariable Long courseId) {
        return courseService.deleteCourse(courseId);
    }

    @GetMapping("/course-count")
    public ResponseEntity<Map<String, Long>> getCourseCount() {
        long count = courseService.countCourses();
        Map<String, Long> countMap = Map.of("courses", count);
        return ResponseEntity.ok(countMap);
    }

    @GetMapping("/{id}/{courseId}")
    public ResponseEntity<Course> getCourseDetails(@PathVariable("courseId") Long courseId) {
        Course course = courseService.getCourseById(courseId);

        // Handle case where course is not found
        if (course == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Return course details with OK status
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
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
    @GetMapping("/{id}/{courseId}/is-enrolled")
    public ResponseEntity<Boolean> isUserEnrolledInCourse(@PathVariable Long id, @PathVariable Long courseId) {
        boolean isEnrolled = courseService.isUserEnrolledInCourse(id, courseId);
        return ResponseEntity.ok(isEnrolled);
    }
    
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<User>> getEnrolledStudents(@PathVariable Long courseId) {
        List<User> students = courseService.getEnrolledStudents(courseId);
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return no content if no students are enrolled
        }
        return ResponseEntity.ok(students);
    }

}
