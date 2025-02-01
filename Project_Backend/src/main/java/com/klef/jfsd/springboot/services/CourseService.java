package com.klef.jfsd.springboot.services;

import com.klef.jfsd.springboot.model.Course;
import com.klef.jfsd.springboot.model.Enrollment;
import com.klef.jfsd.springboot.model.User;
import com.klef.jfsd.springboot.repository.CourseRepository;
import com.klef.jfsd.springboot.repository.EnrollmentRepository;
import com.klef.jfsd.springboot.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // Create a new course
    public Course createCourse(Course course) {
        // Perform any validation or transformation logic if needed
        return courseRepository.save(course);  // Saves the course in the database and returns the saved course
    }

    // Get all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();  // Retrieve all courses from the repository
    }

    public long countCourses() {
        return courseRepository.count();  // Count the total number of courses in the database
    }

    // Update an existing course
    public Course updateCourse(Long courseId, Course updatedCourse) {
        Optional<Course> existingCourse = courseRepository.findById(courseId);
        if (existingCourse.isPresent()) {
            Course course = existingCourse.get();
            course.setCourseName(updatedCourse.getCourseName());
            course.setDescription(updatedCourse.getDescription());
            course.setDuration(updatedCourse.getDuration());
            course.setEnrollmentCount(updatedCourse.getEnrollmentCount());
            course.setImage(updatedCourse.getImage());
            course.setPrice(updatedCourse.getPrice());
            course.setRating(updatedCourse.getRating());
            course.setCategory(updatedCourse.getCategory());
            return courseRepository.save(course);  // Save updated course
        } else {
            throw new RuntimeException("Course not found with ID: " + courseId);
        }
    }

    // Delete a course
    public String deleteCourse(Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            courseRepository.delete(course.get());  // Delete course from the database
            return "Course deleted successfully!";
        } else {
            throw new RuntimeException("Course not found with ID: " + courseId);
        }
    }

    // Get course by ID
    public Course getCourseById(Long courseId) {
        return courseRepository.findByCourseId(courseId);  // Assuming CourseRepository has this method
    }


    public CourseService(EnrollmentRepository enrollmentRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public boolean enrollUserInCourse(Long id, Long courseId) {
        Optional<User> userOpt = userRepository.findById(id);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (userOpt.isPresent() && courseOpt.isPresent()) {
            User user = userOpt.get();
            Course course = courseOpt.get();

            Enrollment enrollment = new Enrollment(user, course, LocalDate.now());
            enrollmentRepository.save(enrollment);

            return true;
        }
        return false;
    }
    
    public boolean isUserEnrolledInCourse(Long id, Long courseId) {
        return enrollmentRepository.existsByUserIdAndCourseId(id, courseId);
    }
    
    public List<Course> getEnrolledCourses(Long id) {
        // Get all enrollments for the user
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(id);

        // Extract the courses from the enrollments
        List<Course> enrolledCourses = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            enrolledCourses.add(enrollment.getCourse());
        }
        return enrolledCourses;
    }
    
    public List<User> getEnrolledStudents(Long courseId) {
        return userRepository.findStudentsByCourseId(courseId);
    }
    
}
