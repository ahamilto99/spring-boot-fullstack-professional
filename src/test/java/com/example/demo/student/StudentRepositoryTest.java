package com.example.demo.student;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;
    
    @Test
    void itShouldCheckIfStudentEmailExists() {
        // given
    	String email = "jamila@gmail.com";
		Student student = new Student("Jamila", email, Gender.FEMALE);
    	underTest.save(student);
    	
        // when
    	Boolean expected = underTest.selectExistsEmail(email);
    	
        // then
    	assertThat(expected).isTrue();
    }
    
    @Test
    void itShouldCheckIfStudentEmailDoesNotExist() {
    	// given
    	String email = "jamila@gmail.com";
    	
    	// when
    	Boolean expected = underTest.selectExistsEmail(email);
    	
    	// then
    	assertThat(expected).isFalse();
    }
    
}
