package com.example.demo.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.student.Gender;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-it.properties")
@AutoConfigureMockMvc
public class StudentIT {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private StudentRepository studentRepository;
	
	private final Faker faker = new Faker();
	
	@Test
	void canDeleteStudent() throws Exception {
		// given
		var student = new Student(String.format("%s %s", faker.name().firstName(), faker.name().lastName()),
				String.format("%s@gmail.com", faker.name().lastName()), Gender.MALE);
		mvc.perform(MockMvcRequestBuilders.post("/api/v1/students").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(student))).andExpect(MockMvcResultMatchers.status().isOk());
		Long id = studentRepository.findFirstByOrderByIdDesc().getId();

		// when
		ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.delete("/api/v1/students/" + String.valueOf(id)));
		
		// then
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		Assertions.assertThat(studentRepository.existsById(id)).isFalse();
	}

	@Test
	void canRegisterNewStudent() throws Exception {
		// given
		var student = new Student(String.format("%s %s", faker.name().firstName(), faker.name().lastName()), 
				String.format("%s@gmail.com", faker.name().lastName()), Gender.FEMALE); 
		
		// when
		ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/api/v1/students")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(student)));
		
		// then
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		List<Student> students = studentRepository.findAll();
		Assertions.assertThat(students).usingElementComparatorIgnoringFields("id").contains(student);
	}

}
