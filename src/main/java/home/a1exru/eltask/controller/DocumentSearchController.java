package home.a1exru.eltask.controller;

import home.a1exru.eltask.dto.Document;
import home.a1exru.eltask.service.DocumentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentSearchController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public SearchResponse search(SearchCmd command) {
        List<Document> data = documentService.search(command.query, command.sentiment);
        return new SearchResponse(command, data);
    }

    @Data
    public static class SearchCmd {
        String query;
        String sentiment;
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class SearchResponse {
        SearchCmd criteria;
        List<Document> data;
    }

}
