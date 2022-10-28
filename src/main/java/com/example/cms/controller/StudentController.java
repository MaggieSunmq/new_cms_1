package com.example.cms.controller;

import com.example.cms.controller.dto.StudentDto;
import com.example.cms.controller.exceptions.DepartmentNotFoundException;
import com.example.cms.controller.exceptions.ProfessorNotFoundException;
import com.example.cms.controller.exceptions.StudentNotFoundException;
import com.example.cms.model.entity.Course;
import com.example.cms.model.entity.Department;
import com.example.cms.model.entity.Professor;
import com.example.cms.model.entity.Student;
import com.example.cms.model.repository.DepartmentRepository;
import com.example.cms.model.repository.ProfessorRepository;
import com.example.cms.model.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class StudentController {
    @Autowired
    private final StudentRepository repository;
    @Autowired
    private DepartmentRepository departmentRepository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/students")
    List<Student> retrieveAllStudents() {
        return repository.findAll();
    }

    @PostMapping("/students")
    Student createStudent(@RequestBody StudentDto StudentDto) {
        Student newStudent = new Student();
        newStudent.setId(StudentDto.getID());
        newStudent.setFirstName(StudentDto.getFirstname());
        newStudent.setLastName(StudentDto.getLastname());
        newStudent.setEmail(StudentDto.getEmail());

        Department department = departmentRepository.findById(StudentDto.getDeptId()).orElseThrow(
                () -> new DepartmentNotFoundException(StudentDto.getDeptId()));
        newStudent.setDepartment(department);
        return repository.save(newStudent);

    }

    @GetMapping("/students/{id}")
    Student retrieveStudent(@PathVariable("id") Long studentId) {
        return repository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    @PutMapping("/students/{id}")
    Student updateStudent(@RequestBody Student newStudent, @PathVariable("id") Long studentId) {
        return repository.findById(studentId)
                .map(student -> {
                    student.setFirstName(newStudent.getFirstName());
                    student.setLastName(newStudent.getLastName());
                    return repository.save(student);
                })
                .orElseGet(() -> {
                    newStudent.setId(studentId);
                    return repository.save(newStudent);
                });
    }

    @DeleteMapping("/students/{id}")
    void deleteStudent(@PathVariable("id") Long studentId) {
        repository.deleteById(studentId);
    }

    @GetMapping("/students/search/{searchstring}")
    List<Student> searchStudent(@PathVariable("searchstring") String searchString) {
        return repository.search(searchString);
    }

    @GetMapping("/students/top")
    List<Student> retrieveTopStudents() {
        return repository.findTopStudents();
    }
}
