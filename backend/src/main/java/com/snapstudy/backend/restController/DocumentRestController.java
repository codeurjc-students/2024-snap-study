package com.snapstudy.backend.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snapstudy.backend.model.Document;
import com.snapstudy.backend.model.Subject;
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

        @Operation(summary = "Get a page of documents")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found the Documents", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Document.class)) }),
                        @ApiResponse(responseCode = "404", description = "Documents not found", content = @Content) })
        @GetMapping("/{subjectId}")
        public ResponseEntity<Page<Document>> getDocuments(@PathVariable Long subjectId, @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {
                Subject subject = subjectService.getSubjectById(subjectId);
                
                if (subject != null){
                    Page<Document> findDocuments = documentService.findDocumentsBySubjectId(subjectId, PageRequest.of(page, size));
                    if (findDocuments.getNumberOfElements() > 0) {
                        return new ResponseEntity<>(findDocuments, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                
        } 
}
