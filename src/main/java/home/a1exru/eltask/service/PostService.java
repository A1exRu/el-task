package home.a1exru.eltask.service;

import home.a1exru.eltask.dto.Post;
import home.a1exru.eltask.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> search(String query, String sentiment, int from, int size) {
        return postRepository.search(query, sentiment, from, size);
    }

}
