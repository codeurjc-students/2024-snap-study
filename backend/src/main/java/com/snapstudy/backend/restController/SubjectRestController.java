package com.snapstudy.backend.restController;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snapstudy.backend.model.Degree;
import com.snapstudy.backend.model.RepositoryDocument;
import com.snapstudy.backend.model.Subject;
import com.snapstudy.backend.repository.RepositoryDocumentsRepository;
import com.snapstudy.backend.s3.S3Service;
import com.snapstudy.backend.service.DegreeService;
import com.snapstudy.backend.service.SubjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/subjects")
public class SubjectRestController {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private DegreeService degreeService;
    @Autowired
    private S3Service awsS3;
    @Autowired
    private RepositoryDocumentsRepository repositoryDocument;

    @Operation(summary = "Get a page of Subjects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subjects found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Subject.class)) }),
            @ApiResponse(responseCode = "204", description = "No content found", content = @Content),
            @ApiResponse(responseCode = "404", description = "Subjects not found", content = @Content) })
    @GetMapping("/degrees/{degreeId}")
    public ResponseEntity<Page<Subject>> getSubjects(@PathVariable Long degreeId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Degree degree = degreeService.getDegreeById(degreeId);

        if (degree != null) {
            Page<Subject> findSubjects = subjectService.findSubjectsByDegreeId(degreeId,
                    PageRequest.of(page, size));
            if (findSubjects.getNumberOfElements() > 0) {
                return new ResponseEntity<>(findSubjects, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Operation(summary = "Get a subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the subject", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Subject.class)) }),
            @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content) })
    @GetMapping("/{subjectId}")
    public ResponseEntity<Subject> getSubject(@PathVariable Long subjectId, HttpServletRequest request) {

        Subject subject = subjectService.getSubjectById(subjectId);
        if (subject != null) {
            return new ResponseEntity<>(subject, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Subject.class)) }),
            @ApiResponse(responseCode = "404", description = "Degree not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Subject already created", content = @Content) })
    @PostMapping("/{degreeId}")
    public ResponseEntity<Subject> createSubject(@RequestBody String name, @PathVariable Long degreeId) {
        Degree degree = degreeService.getDegreeById(degreeId);

        if (degree == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Subject> subject = subjectService.findByNameAndDegree(name, degree);
        if (subject.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            Subject newSubject = new Subject(name, degree);
            subjectService.save(newSubject);
            return new ResponseEntity<>(newSubject, HttpStatus.OK);
        }
    }

    @Operation(summary = "Delete subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Subject.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content) })
    @DeleteMapping("/{degreeId}/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long degreeId, @PathVariable Long id) {
        Subject sub = subjectService.getSubjectById(id);

        if (sub != null) {

            if (degreeId == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Degree degree = degreeService.getDegreeById(degreeId);
            if (degree == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            String path = "RepositoryDocuments/" + degree.getName() + "/" + sub.getName();
            int result = awsS3.deleteFolder(path); // Delete the folder from S3

            if (result == 0) {

                Optional<RepositoryDocument> repository = repositoryDocument
                        .findByDegreeIdAndSubjectId(degreeId, id);

                if (repository.isPresent()) {
                    repositoryDocument.delete(repository.get()); // Remove it from the repository
                } else {
                    // During the testing phase, not all repositories are in S3
                }

                subjectService.deleteSubject(id);

                return ResponseEntity.noContent().build();
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get degree by subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Degree found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Degree.class)) }),
            @ApiResponse(responseCode = "404", description = "Subject/Degree not found", content = @Content) })
    @GetMapping("/degreesby/{subjectId}")
    public ResponseEntity<Degree> getDegreeBySubject(@PathVariable Long subjectId) {
        Subject subject = subjectService.getSubjectById(subjectId);

        if (subject != null) {
            Degree degree = subject.getDegree();
            if (degree != null) {
                return new ResponseEntity<>(degree, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}