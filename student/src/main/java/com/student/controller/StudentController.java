package com.student.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.entity.Student;
import com.student.exception.ResourceNotFoundException;
import com.student.repo.StudentRepository;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private static final Logger LOG = Logger.getLogger(StudentController.class.getName());

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/getAll")
    public List<Student> getAllStudents() {
        LOG.info("Fetching all students.");
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable(value = "id") Long studentId) {
        LOG.info("Fetching student with id: " + studentId);
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            return ResponseEntity.ok().body(student);
        } else {
            throw new ResourceNotFoundException("Student not found with id " + studentId);
        }
    }

    @PostMapping("/create")
    public Student createStudent(@RequestBody Student student) {
        LOG.info("Creating a new student.");
        return studentRepository.save(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable(value = "id") Long studentId,
                                                 @RequestBody Student studentDetails) {
        LOG.info("Updating student with id: " + studentId);
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setName(studentDetails.getName());
            student.setAge(studentDetails.getAge());
            student.setGrade(studentDetails.getGrade());
            final Student updatedStudent = studentRepository.save(student);
            return ResponseEntity.ok(updatedStudent);
        } else {
            throw new ResourceNotFoundException("Student not found with id " + studentId);
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteStudent(@PathVariable(value = "id") Long studentId) {
        LOG.info("Deleting student with id: " + studentId);
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            studentRepository.delete(student);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } else {
            throw new ResourceNotFoundException("Student not found with id " + studentId);
        }
    }
}
