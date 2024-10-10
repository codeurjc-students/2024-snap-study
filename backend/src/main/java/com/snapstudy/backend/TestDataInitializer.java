package com.snapstudy.backend;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapstudy.backend.model.Degree;
import com.snapstudy.backend.model.Student;
import com.snapstudy.backend.model.Subject;
import com.snapstudy.backend.repository.DegreeRepository;
import com.snapstudy.backend.repository.StudentRepository;
import com.snapstudy.backend.repository.SubjectRepository;

@Service
public class TestDataInitializer {

    @Autowired
    private StudentRepository studenRepository;
    @Autowired
    private DegreeRepository degreeRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @PostConstruct
    public void init() throws Exception {
        Student user1 = new Student("Javier", "Salas", "javiisalaas97@gmail.com", "hola");
        studenRepository.save(user1);

        Degree d1 = new Degree("Software Engineering");
        Degree d2 = new Degree("Law");
        Degree d3 = new Degree("Medicine");
        Degree d4 = new Degree("Electrical Engineering");
        Degree d5 = new Degree("Mechanical Engineering");
        Degree d6 = new Degree("Civil Engineering");
        Degree d7 = new Degree("Business Administration");
        Degree d8 = new Degree("Psychology");
        Degree d9 = new Degree("Architecture");
        Degree d10 = new Degree("Chemistry");
        Degree d11 = new Degree("Physics");
        Degree d12 = new Degree("Mathematics");
        Degree d13 = new Degree("Political Science");
        Degree d14 = new Degree("Economics");
        Degree d15 = new Degree("History");
        Degree d16 = new Degree("Philosophy");

        degreeRepository.save(d1);
        degreeRepository.save(d2);
        degreeRepository.save(d3);
        degreeRepository.save(d4);
        degreeRepository.save(d5);
        degreeRepository.save(d6);
        degreeRepository.save(d7);
        degreeRepository.save(d8);
        degreeRepository.save(d9);
        degreeRepository.save(d10);
        degreeRepository.save(d11);
        degreeRepository.save(d12);
        degreeRepository.save(d13);
        degreeRepository.save(d14);
        degreeRepository.save(d15);
        degreeRepository.save(d16);

        Subject s1 = new Subject("Math", d1);
        Subject s2 = new Subject("Programming", d2);
        Subject s3 = new Subject("Physics", d3);
        Subject s4 = new Subject("Data Bases", d1);
        Subject s5 = new Subject("AWS Cloud", d1);
        Subject s6 = new Subject("Cloud Computing", d1);
        Subject s7 = new Subject("IA", d1);
        Subject s8 = new Subject("Machine Learning", d1);
        Subject s9 = new Subject("Science", d1);

        subjectRepository.save(s1);
        subjectRepository.save(s2);
        subjectRepository.save(s3);
        subjectRepository.save(s4);
        subjectRepository.save(s5);
        subjectRepository.save(s6);
        subjectRepository.save(s7);
        subjectRepository.save(s8);
        subjectRepository.save(s9);

    }
}
