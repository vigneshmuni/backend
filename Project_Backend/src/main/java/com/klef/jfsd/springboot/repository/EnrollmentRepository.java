package com.klef.jfsd.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.klef.jfsd.springboot.model.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    // Additional query methods can be added as needed
	 @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
	           "FROM Enrollment e WHERE e.user.id = :id AND e.course.id = :courseId")
	    boolean existsByUserIdAndCourseId(Long id, Long courseId);
	 List<Enrollment> findByUserId(Long id);
}