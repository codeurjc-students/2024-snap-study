package com.snapstudy.backend;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapstudy.backend.model.Admin;
import com.snapstudy.backend.model.Degree;
import com.snapstudy.backend.model.Document;
import com.snapstudy.backend.model.RepositoryDocument;
import com.snapstudy.backend.model.Student;
import com.snapstudy.backend.model.Subject;
import com.snapstudy.backend.repository.AdminRepository;
import com.snapstudy.backend.repository.DegreeRepository;
import com.snapstudy.backend.repository.DocumentRepository;
import com.snapstudy.backend.repository.RepositoryDocumentsRepository;
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
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RepositoryDocumentsRepository repodocumentRepository;

    @PostConstruct
    public void init() throws Exception {
        Student user1 = new Student("Javier", "Salas", "javiisalaas97@gmail.com", "hola");
        Student user2 = new Student("Sandra", "García", "sandra.garcia2093@gmail.com", "hola");
        Admin ad = new Admin("Administrador", "Admin", "admin@admin.com", "admin12345admin");
        adminRepository.save(ad);
        studenRepository.save(user1);
        studenRepository.save(user2);

        Degree d1 = new Degree("Software Engineering", "Engineering and Architecture");
        Degree d2 = new Degree("Law", "Social Sciences and Law");
        Degree d3 = new Degree("Medicine", "Health Sciences");
        Degree d4 = new Degree("Electrical Engineering", "Engineering and Architecture");
        Degree d5 = new Degree("Mechanical Engineering", "Engineering and Architecture");

        degreeRepository.save(d1);
        degreeRepository.save(d2);
        degreeRepository.save(d3);
        degreeRepository.save(d4);
        degreeRepository.save(d5);

        Subject s1 = new Subject("Math", d1);
        Subject s2 = new Subject("Programming", d2);
        Subject s3 = new Subject("Physics", d3);
        Subject s4 = new Subject("Data Bases", d1);
        Subject s5 = new Subject("AWS Cloud", d1);

        subjectRepository.save(s1);
        subjectRepository.save(s2);
        subjectRepository.save(s3);
        subjectRepository.save(s4);
        subjectRepository.save(s5);

        RepositoryDocument newRepo = new RepositoryDocument(d1.getId(), s1.getId());
        repodocumentRepository.save(newRepo);
        RepositoryDocument newRepo2 = new RepositoryDocument(d1.getId(), s5.getId());
        repodocumentRepository.save(newRepo2);

    }
}