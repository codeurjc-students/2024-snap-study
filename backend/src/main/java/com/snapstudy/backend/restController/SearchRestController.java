package com.snapstudy.backend.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.snapstudy.backend.model.SearchResult;
import com.snapstudy.backend.opensearch.OpenSearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/search")
public class SearchRestController {

    @Autowired
    private OpenSearchService openSearchService;

    @Operation(summary = "Execute a search")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correct search", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SearchResult.class)) }) })
    @GetMapping("")
    public ResponseEntity<List<SearchResult>> search(@RequestParam("query") String query,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        try {
            List<SearchResult> searchResults = searchIndex(query);
            if (searchResults != null) {
                return new ResponseEntity<>(searchResults, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error ejecutando búsqueda ", e);
        }
    }

    public List<SearchResult> searchIndex(String query) {
        return openSearchService.search(query);
    }
}
