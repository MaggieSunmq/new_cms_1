package com.example.cms;

import com.example.cms.controller.exceptions.CourseNotFoundException;
import com.example.cms.model.entity.Course;
import com.example.cms.model.repository.CourseRepository;
import com.example.cms.model.repository.ProfessorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CourseProfessorTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Test
	void addProfessorAndCourse() throws Exception{

		ObjectNode profJson = objectMapper.createObjectNode();
		profJson.put("id", 10000);
		profJson.put("firstName", "first");
		profJson.put("lastName", "last");
		profJson.put("office", "BA0000");
		profJson.put("email", "first.last@prof.com");

		mockMvc.perform(post("/professors").
				contentType("application/json").
				content(profJson.toString())).
				andExpect(status().is2xxSuccessful());

		ObjectNode courseJson = objectMapper.createObjectNode();
		courseJson.put("name", "New Course");
		courseJson.put("code", "NEW100");
		courseJson.put("professorId", 10000);

		mockMvc.perform(post("/courses").
				contentType("application/json").
				content(courseJson.toString())).
				andExpect(status().is2xxSuccessful());

		Course addedCourse = courseRepository.findById("NEW100").orElseThrow(() -> new CourseNotFoundException("NEW100"));
		assertEquals("NEW100", addedCourse.getCode());
		assertEquals(10000L, addedCourse.getProfessor().getId());
	}

}
