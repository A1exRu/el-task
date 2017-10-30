package home.a1exru.eltask.controller;

import home.a1exru.eltask.dto.Post;
import home.a1exru.eltask.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostSearchController {

    @Autowired
    private PostService postService;

    @GetMapping
    public SearchResponse search(SearchCmd command) {
        List<Post> data = postService.search(command.query, command.sentiment, command.from, command.size);
        return new SearchResponse(command, data);
    }

    @Data
    public static class SearchCmd {
        String query;
        String sentiment;
        int from;
        int size = 10;
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class SearchResponse {
        SearchCmd criteria;
        List<Post> data;
    }

}
