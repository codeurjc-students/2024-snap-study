package com.snapstudy.backend.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.snapstudy.backend.model.SearchResult;
import com.snapstudy.backend.service.SearchService;

@RestController
@RequestMapping("/api/search")
public class SearchRestController {

    @Autowired
    private SearchService searchService;

    @GetMapping("")
    public SearchResult search(@RequestParam("query") String query, 
                               @RequestParam("page") int page, 
                               @RequestParam("size") int size) {
        return searchService.search(query, page, size);
    }
}
