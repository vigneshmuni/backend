package com.klef.jfsd.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klef.jfsd.springboot.model.Course;
import com.klef.jfsd.springboot.services.CourseService;

@RestController
@RequestMapping("/api/v1/enrollments")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") 
public class EnrollmentController {

	@Autowired
    private CourseService courseService;

    // Get the list of enrolled courses for a user
    @GetMapping("/{id}/enrolled-courses")
    public ResponseEntity<List<Course>> getEnrolledCourses(@PathVariable Long id) {
        List<Course> enrolledCourses = courseService.getEnrolledCourses(id);
        return ResponseEntity.ok(enrolledCourses);
    }
}
