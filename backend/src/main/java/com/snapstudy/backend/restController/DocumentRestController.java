package com.snapstudy.backend.restController;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.snapstudy.backend.model.Degree;
import com.snapstudy.backend.model.Document;
import com.snapstudy.backend.model.RepositoryDocument;
import com.snapstudy.backend.model.Subject;
import com.snapstudy.backend.repository.DocumentRepository;
import com.snapstudy.backend.repository.RepositoryDocumentsRepository;
import com.snapstudy.backend.s3.S3Service;
import com.snapstudy.backend.service.DegreeService;
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
    @Autowired
    private S3Service awsS3;
    @Autowired
    private DegreeService degreeService;

    @Operation(summary = "Get a page of documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documents found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Document.class)) }),
            @ApiResponse(responseCode = "204", description = "No content found", content = @Content),
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
                return new ResponseEntity<>(findDocuments, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Operation(summary = "Save document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document saved", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Document.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Document already saved", content = @Content),
            @ApiResponse(responseCode = "404", description = "Document not saved", content = @Content) })
    @PostMapping("/{degreeId}/{subjectId}")
    public ResponseEntity<Document> saveDocument(@RequestBody MultipartFile file, @PathVariable Long degreeId,
            @PathVariable Long subjectId) {

        Subject subject = subjectService.getSubjectById(subjectId);

        if (file == null || degreeId == null || subject == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Degree degree = degreeService.getDegreeById(degreeId);
        if (degree == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String path = "RepositoryDocuments/" + degree.getName() + "/" + subject.getName();

        RepositoryDocument repository = getRepository(degreeId, subjectId, path);
        if (repository == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf('.'));
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));

        // control de nombres de archivo repetidos
        Document docCheck = documentService.getDocumentByName(fileName);
        if (docCheck != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Ya existe un archivo con este nombre
        }

        Document document = new Document(fileName, "", subject, repository.getId(), ext);
        documentRepository.save(document);

        path = path + "/" + file.getOriginalFilename();
        awsS3.addFile(path, file); // subimos el fichero a s3

        return new ResponseEntity<>(document, HttpStatus.OK); // Return the document
    }

    private RepositoryDocument getRepository(Long degreeId, Long subjectId, String path) {

        // Crea la carpeta en el s3 si no existe
        awsS3.createFolder(path);

        Optional<RepositoryDocument> repository = repositoryDocument.findByDegreeIdAndSubjectId(degreeId, subjectId);

        if (repository.isPresent()) {
            return repository.get();
        } else {
            RepositoryDocument newRepo = new RepositoryDocument(degreeId, subjectId);
            repositoryDocument.save(newRepo);
            return newRepo;
        }
    }

    @Operation(summary = "Delete document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Document.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Document not deleted", content = @Content) })
    @DeleteMapping("/{degreeId}/{subjectId}/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id, @PathVariable Long degreeId,
            @PathVariable Long subjectId) {
        Document doc = documentService.getDocumentById(id);

        if (doc != null) {

            Subject subject = subjectService.getSubjectById(subjectId);

            if (degreeId == null || subject == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Degree degree = degreeService.getDegreeById(degreeId);
            if (degree == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            String path = "RepositoryDocuments/" + degree.getName() + "/" + subject.getName();
            String fileName = doc.getName() + doc.getExtension();

            int result = awsS3.deleteFile(path, fileName); // eliminar del s3 el documento

            if (result == 0) {
                documentService.deleteDocument(id);
                return ResponseEntity.noContent().build();
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Download document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Document.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Document not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "S3 error", content = @Content), })
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        try {
            Document doc = documentService.getDocumentById(id);

            if (doc == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Subject sub = doc.getSubject();
            Degree deg = sub.getDegree();
            if (deg == null || sub == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Construcción segura de la ruta
            String path = "RepositoryDocuments/" + deg.getName() + "/" + sub.getName();
            String docName = doc.getName() + doc.getExtension();
            Map<String, InputStream> downloadedFile = awsS3.downloadFile(path, docName);

            if (downloadedFile != null && !downloadedFile.isEmpty()) {
                InputStream inputStream = downloadedFile.get(docName);

                if (inputStream == null) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }

                InputStreamResource resource = new InputStreamResource(inputStream);

                // Configurar encabezados para la descarga del archivo
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + docName + "\"");
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Document.class)) }),
            @ApiResponse(responseCode = "404", description = "Document not found", content = @Content) })
    @GetMapping("/")
    public ResponseEntity<Document> getDocument(@RequestParam("id") Long id) {
        Document doc = documentService.getDocumentById(id);

        if (doc == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(doc);

    }

}