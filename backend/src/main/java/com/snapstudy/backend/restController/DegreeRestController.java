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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snapstudy.backend.model.Degree;
import com.snapstudy.backend.model.Document;
import com.snapstudy.backend.service.DegreeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/degrees")
public class DegreeRestController {

        @Autowired
        private DegreeService degreeService;

        @Operation(summary = "Get a page of degrees")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found the degrees", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Degree.class)) }),
                        @ApiResponse(responseCode = "404", description = "degrees not found", content = @Content) })
        @GetMapping("/")
        public ResponseEntity<Page<Degree>> getdegrees(@RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {
                Page<Degree> findDegrees = degreeService.findDegrees(PageRequest.of(page, size));
                if (findDegrees.getNumberOfElements() > 0) {
                        return new ResponseEntity<>(findDegrees, HttpStatus.OK);
                } else {
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
        }

        @Operation(summary = "Get a degree")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found the degree", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Degree.class)) }),
                        @ApiResponse(responseCode = "404", description = "Dregee not found", content = @Content) })
        @GetMapping("/{degreeId}")
        public ResponseEntity<Degree> getSubject(@PathVariable Long degreeId, HttpServletRequest request) {

                Degree degree = degreeService.getDegreeById(degreeId);
                if (degree != null) {
                        return new ResponseEntity<>(degree, HttpStatus.OK);
                } else {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
        }

        @PostMapping("/")
        public ResponseEntity<Degree> createDegree(@RequestBody String name) {
                Optional<Degree> degree = degreeService.findByName(name);
                if (degree.isPresent()) {
                        return new ResponseEntity<>(HttpStatus.CONFLICT);
                } else {
                        Degree newDegree = new Degree(name);
                        degreeService.save(newDegree);
                        return new ResponseEntity<>(newDegree, HttpStatus.OK);
                }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteDegree(@PathVariable Long id) {
                Degree deg = degreeService.getDegreeById(id);

                if (deg != null) {
                        degreeService.deleteDegree(id);
                        return ResponseEntity.noContent().build();
                } else {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
        }

}
