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
        Admin ad = new Admin("Administrador", "Admin", "admin@admin.com", "admin12345admin");
        adminRepository.save(ad);
        studenRepository.save(user1);

        Degree d1 = new Degree("Software Engineering", "Engineering and Architecture");
        Degree d2 = new Degree("Law", "Social Sciences and Law");
        Degree d3 = new Degree("Medicine", "Health Sciences");
        Degree d4 = new Degree("Electrical Engineering", "Engineering and Architecture");
        Degree d5 = new Degree("Mechanical Engineering", "Engineering and Architecture");
        Degree d6 = new Degree("Civil Engineering", "Engineering and Architecture");
        Degree d7 = new Degree("Business Administration", "Social Sciences and Law");
        Degree d8 = new Degree("Psychology", "Health Sciences");
        Degree d9 = new Degree("Architecture", "Engineering and Architecture");
        Degree d10 = new Degree("Chemistry", "Exact and Natural Sciences");
        Degree d11 = new Degree("Physics", "Exact and Natural Sciences");
        Degree d12 = new Degree("Mathematics", "Exact and Natural Sciences");
        Degree d13 = new Degree("Political Science", "Social Sciences and Law");
        Degree d14 = new Degree("Economics", "Social Sciences and Law");
        Degree d15 = new Degree("History", "Arts and Humanities");
        Degree d16 = new Degree("Philosophy", "Arts and Humanities");

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

        RepositoryDocument newRepo = new RepositoryDocument(d1.getId(), s1.getId());
        repodocumentRepository.save(newRepo);

        Document dt1 = new Document("Tema 1", "Prueba", s1, newRepo.getId(), ".pdf");
        Document dt2 = new Document("Tema 2", "Prueba", s1, newRepo.getId(), ".pdf");
        Document dt3 = new Document("Tema 3", "Prueba", s1, newRepo.getId(), ".pdf");
        Document dt4 = new Document("Tema 4", "Prueba", s1, newRepo.getId(), ".pdf");
        Document dt5 = new Document("Tema 5", "Prueba", s1, newRepo.getId(), ".pdf");
        Document dt6 = new Document("cubo", "Prueba", s1, newRepo.getId(), ".pdf");
        Document dt7 = new Document("mia", "Prueba", s1, newRepo.getId(), ".jpg");
        Document dt8 = new Document("PracticaColasPilas", "Prueba", s1, newRepo.getId(), ".pas");
        documentRepository.save(dt1);
        documentRepository.save(dt2);
        documentRepository.save(dt3);
        documentRepository.save(dt4);
        documentRepository.save(dt5);
        documentRepository.save(dt6);
        documentRepository.save(dt7);
        documentRepository.save(dt8);

    }
}
