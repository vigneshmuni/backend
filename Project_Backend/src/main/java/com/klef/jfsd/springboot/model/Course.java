package com.klef.jfsd.springboot.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false, updatable = false)
    private Long courseId;

    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @Column(name = "description", nullable = false, length = 500)
    private String description;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments = new HashSet<>();
    
    
    @Column(name = "duration", nullable = false, length = 50)
    private String duration; // Duration of the course (e.g., '4 weeks', '30 hours')

    @Column(name = "enrollment_count", nullable = false)
    private int enrollmentCount;

    @Column(name = "image", length = 255)
    private String image; // Image URL for the course

    @Column(name = "price", nullable = false)
    private double price; // Price of the course

    @Column(name = "rating", nullable = false)
    private float rating; // Average rating of the course

    @Column(name = "category", nullable = false, length = 100)
    private String category; // Category of the course (e.g., "Programming", "Data Science")

    // Constructors, getters, and setters

    public Course() {}

    public Course(String courseName, String description, User instructor, String duration, int enrollmentCount,
            String image, double price, float rating, String category) {
			  this.courseName = courseName;
			  this.description = description;
			  this.duration = duration;
			  this.enrollmentCount = enrollmentCount;
			  this.image = image;
			  this.price = price;
			  this.rating = rating;
			  this.category = category;
	}
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getEnrollmentCount() {
        return enrollmentCount;
    }

    public void setEnrollmentCount(int enrollmentCount) {
        this.enrollmentCount = enrollmentCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
