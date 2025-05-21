package com.snapstudy.backend.service;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.snapstudy.backend.model.Document;
import com.snapstudy.backend.model.SearchResult;
import com.snapstudy.backend.opensearch.OpenSearchService;
import com.snapstudy.backend.repository.DocumentRepository;

@Service
public class DocumentService {
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    private OpenSearchService openSearchService;

    public Page<Document> findDocumentsBySubjectId(Long subjectId, Pageable pageable) {
        return documentRepository.findDocumentsBySubjectId(subjectId, pageable);
    }

    public Document getDocumentById(Long id) {
        Optional<Document> document = documentRepository.findById(id);

        if (document.isPresent()) {
            return document.get();
        } else {
            return null;
        }
    }

    public Document getDocumentByName(String name) {
        Optional<Document> document = documentRepository.findByName(name);

        if (document.isPresent()) {
            return document.get();
        } else {
            return null;
        }
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

    public static boolean noDocumentsFound(String jsonResponse) {
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject hits = root.getJSONObject("hits");
            JSONObject total = hits.getJSONObject("total");
            int value = total.getInt("value");

            return value == 0;
        } catch (Exception e) {
            return true;
        }
    }

    public int deleteDocmentFromOpensearch(Long documentId) throws Exception {
        String dbindex = documentId.toString();
        try {
            String queryJson = openSearchService.buildSearchQueryByIndex(dbindex);
            String response = openSearchService.search(queryJson);

            if (noDocumentsFound(response)) {
                return 0;
            }

            List<SearchResult> opensearchDocs = openSearchService.transformQueryOpssIndex(response);

            if (opensearchDocs.size() == 1) {
                String opssIndex = opensearchDocs.get(0).getTitle();
                String deleteResponse = openSearchService.delete(opssIndex);
                System.out.println(deleteResponse);
                return 0;
            } else {
                return 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

}