package com.example.cms;

import com.example.cms.controller.exceptions.StudentNotFoundException;
import com.example.cms.model.entity.Student;
import com.example.cms.model.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StudentTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private StudentRepository studentRepository;



	@Test
	void getStudent() throws Exception{
		Student s = studentRepository.findById(1111L).orElseThrow(() -> new StudentNotFoundException(1111L));

		ObjectNode expectedJson = objectMapper.createObjectNode();
		expectedJson.put("firstName", "Tyrion");
		expectedJson.put("lastName", "Lannister");
		mockMvc.perform(get("/students/1111"))
				.andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andExpect(content().json(expectedJson.toString()));
	}


	@Test
	void addStudent() throws Exception{

		ObjectNode studentJson = objectMapper.createObjectNode();
		studentJson.put("firstName", "first");
		studentJson.put("lastName", "last");
		studentJson.put("email", "first@last.com");

		mockMvc.perform(
				post("/students").
						contentType("application/json").
						content(studentJson.toString()))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	void deleteStudent() throws Exception{
		Student s = new Student();
		s.setId(123456L);
		s.setFirstName("first");
		s.setLastName("last");
		s.setEmail("first@last.com");
		studentRepository.save(s);

		mockMvc.perform(
				delete("/students/123456").
						contentType("application/json"))
				.andExpect(status().is2xxSuccessful());

		assertTrue(studentRepository.findById(123456L).isEmpty());
	}

}
