package home.a1exru.eltask.service;

import home.a1exru.eltask.dto.Keyword;
import home.a1exru.eltask.dto.Post;
import home.a1exru.eltask.repository.PostRepository;
import org.elasticsearch.common.collect.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Tuple<List<Post>, List<Keyword>> search(String query, String sentiment, int from, int size) {
        size = Math.min(100, size);
        List<Post> posts = postRepository.search(query, sentiment, from, size);

        Map<String, Integer> keyPhrases = new HashMap<>();
        posts.stream().flatMap(p -> p.getKeyPhrases().stream()).forEach(phrase -> {
            Integer count = keyPhrases.getOrDefault(phrase, 0);
            keyPhrases.put(phrase, ++count);
        });

        List<Keyword> keywords = keyPhrases.entrySet().stream()
                .map(e -> new Keyword(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingInt(Keyword::getRelevance).reversed())
                .collect(toList());

        return new Tuple<>(posts, keywords);
    }

}
