package com.snapstudy.backend.restController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Operation(summary = "Execute a search")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correct search", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SearchResult.class)) }) })
    @GetMapping("")
    public List<SearchResult> search(@RequestParam("query") String query,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        try {
            String scriptPath = "./search.py"; // Ajusta la ruta si hace falta
            String indexName = "snapstudy-index";

            ProcessBuilder pb = new ProcessBuilder(
                    "python", scriptPath, query, indexName);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python script error: " + output);
            }

            // Ahora el contenido de output es un JSON, lo procesamos
            JSONObject jsonObject = new JSONObject(output.toString());

            // Extraemos el arreglo de resultados
            JSONArray results = jsonObject.getJSONArray("results");

            // Creamos la lista de SearchResult
            List<SearchResult> searchResults = new ArrayList<>();

            // Recorremos el arreglo de resultados y extraemos el índice y el título
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String title = result.getString("title");
                Long index = Long.parseLong(result.getString("index"));
                searchResults.add(new SearchResult(index, title)); // Agregamos a la lista con el índice
            }
            System.out.println(searchResults.toString());
            return searchResults;

        } catch (Exception e) {
            throw new RuntimeException("Error ejecutando búsqueda con Python", e);
        }
    }
}
