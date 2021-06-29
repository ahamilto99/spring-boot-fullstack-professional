package com.example.demo.student;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

	@InjectMocks
	private StudentService underTest;

	@Mock
	private StudentRepository studentRepository;

	@Test
	void canDeleteStudent() {
		// given
		Long id = 1L;
		BDDMockito.given(studentRepository.existsById(id)).willReturn(true);

		// when
		underTest.deleteStudent(id);

		// then
		Mockito.verify(studentRepository).deleteById(id);
	}

	@Test
	void willThrowWhenStudentNotFound() {
		// given
		Long id = 1L;
		BDDMockito.given(studentRepository.existsById(id)).willReturn(false);
		
		// when
		// then
		Assertions.assertThatExceptionOfType(StudentNotFoundException.class)
				.isThrownBy(() -> underTest.deleteStudent(1L)).withMessage("No student has ID=1");
		Mockito.verify(studentRepository, Mockito.never()).deleteById(ArgumentMatchers.anyLong());
	}

	@Test
	void willThrowWhenEmailIsTaken() {
		// given
		Student student = new Student("Jamila", "jamila@gmail.com", Gender.FEMALE);
		BDDMockito.given(studentRepository.selectExistsEmail(ArgumentMatchers.anyString()))
				.willReturn(true);

		// when
		// then
		Assertions.assertThatExceptionOfType(BadRequestException.class)
				.isThrownBy(() -> underTest.addStudent(student))
				.withMessage("Email " + student.getEmail() + " is taken");
		// this is identical to the test above
		Assertions.assertThatThrownBy(() -> underTest.addStudent(student))
				.isInstanceOf(BadRequestException.class)
				.hasMessage("Email " + student.getEmail() + " is taken");
		Mockito.verify(studentRepository, Mockito.never()).save(ArgumentMatchers.any());
	}

	@Test
	void canAddStudent() {
		// given
		var student = new Student("Jamila", "jamila@gmail.com", Gender.FEMALE);

		// when
		underTest.addStudent(student);

		// then
		ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
		Mockito.verify(studentRepository).save(studentArgumentCaptor.capture());

		var capturedStudent = studentArgumentCaptor.getValue();
		Assertions.assertThat(capturedStudent).isEqualTo(student);
	}

	@Test
	void canGetAllStudents() {
		// when
		underTest.getAllStudents();

		// then
		Mockito.verify(studentRepository).findAll();
	}

}
