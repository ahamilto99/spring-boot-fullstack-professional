package com.example.demo.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface StudentRepository extends JpaRepository<Student, Long>, QueryByExampleExecutor<Student> { // @formatter:off

    @Query("SELECT"
            + " CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END"
            + " FROM Student s"
            + " WHERE s.email = ?1")
    boolean selectExistsEmail(String email);
    
    
    Student findFirstByOrderByIdDesc();
    
} // @formatter:on
