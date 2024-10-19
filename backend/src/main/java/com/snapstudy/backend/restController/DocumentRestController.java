package com.snapstudy.backend.restController;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snapstudy.backend.model.Degree;
import com.snapstudy.backend.model.Document;
import com.snapstudy.backend.model.RepositoryDocument;
import com.snapstudy.backend.model.Subject;
import com.snapstudy.backend.model.DTO.DocumentDTO;
import com.snapstudy.backend.repository.DocumentRepository;
import com.snapstudy.backend.repository.RepositoryDocumentsRepository;
import com.snapstudy.backend.service.DocumentService;
import com.snapstudy.backend.service.SubjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/documents")
public class DocumentRestController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private RepositoryDocumentsRepository repositoryDocument;
    @Autowired
    private DocumentRepository documentRepository;

    @Operation(summary = "Get a page of documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Documents", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Document.class)) }),
            @ApiResponse(responseCode = "404", description = "Documents not found", content = @Content) })
    @GetMapping("/{subjectId}")
    public ResponseEntity<Page<Document>> getDocuments(@PathVariable Long subjectId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Subject subject = subjectService.getSubjectById(subjectId);

        if (subject != null) {
            Page<Document> findDocuments = documentService.findDocumentsBySubjectId(subjectId,
                    PageRequest.of(page, size));
            if (findDocuments.getNumberOfElements() > 0) {
                System.out.println("----------------------------------------------");
                return new ResponseEntity<>(findDocuments, HttpStatus.OK);
            } else {
                System.out.println("----------------------------------------------*");
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        System.out.println("----------------------------------------------**");
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/{degreeId}/{subjectId}")
    public ResponseEntity<Document> saveDocument(@RequestBody MultipartFile file, @PathVariable Long degreeId, @PathVariable Long subjectId) {

        Subject subject = subjectService.getSubjectById(subjectId);

        // Check if file, degree, and subject are valid
        if (file == null || degreeId == null || subject == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        RepositoryDocument repository = getRepository(degreeId, subjectId);
        if (repository == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Document document = new Document(file.getOriginalFilename(), "", subject, repository.getId());
        documentRepository.save(document);

        // TODO: Call your method to upload to S3 here

        return new ResponseEntity<>(document, HttpStatus.OK); // Return the document
    }

    private RepositoryDocument getRepository(Long degreeId, Long subjectId) {
        Optional<RepositoryDocument> repository = repositoryDocument.findByDegreeIdAndSubjectId(degreeId, subjectId);

        if (repository.isPresent()) {
            return repository.get();
        } else {
            RepositoryDocument newRepo = new RepositoryDocument(degreeId, subjectId);
            // crear en s3
            return newRepo;
        }
    }
}
