package com.snapstudy.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResult {
    @JsonProperty("index")
    private Long index;
    @JsonProperty("title")
    private String title;

    public SearchResult(Long index, String title) {
        this.index = index;
        this.title = title;
    }

    public Long getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

}
