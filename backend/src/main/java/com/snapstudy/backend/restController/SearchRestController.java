package com.snapstudy.backend.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.snapstudy.backend.model.Document;
import com.snapstudy.backend.model.SearchResult;
import com.snapstudy.backend.service.SearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/search")
public class SearchRestController {

    @Autowired
    private SearchService searchService;

    @Operation(summary = "Execute a search")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correct search", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SearchResult.class)) }) })
    @GetMapping("")
    public SearchResult search(@RequestParam("query") String query, 
                               @RequestParam("page") int page, 
                               @RequestParam("size") int size) {
        return searchService.search(query, page, size);
    }
}
