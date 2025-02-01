package com.klef.jfsd.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.klef.jfsd.springboot.model.User;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllByRole(String role);
    long countByRole(String role);
    Optional<User> findById(Long id);
    
    @Query("SELECT u FROM User u JOIN Enrollment e ON u.id = e.user.id WHERE e.course.id = :courseId")
    List<User> findStudentsByCourseId(Long courseId);
}
