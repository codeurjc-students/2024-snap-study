package com.snapstudy.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResult {
    @JsonProperty("index")
    private String index;
    @JsonProperty("title")
    private String title;

    public SearchResult(String index, String title) {
        this.index = index;
        this.title = title;
    }
}
