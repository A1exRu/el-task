package home.a1exru.eltask.repository;

import home.a1exru.eltask.dto.Post;

import java.util.List;

public interface PostRepository {

    List<Post> search(String query, String sentiment, int from, int size);

}
