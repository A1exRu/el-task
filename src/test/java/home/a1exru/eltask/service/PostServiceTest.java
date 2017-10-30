package home.a1exru.eltask.service;

import home.a1exru.eltask.dto.Keyword;
import home.a1exru.eltask.dto.Post;
import home.a1exru.eltask.repository.PostRepository;
import org.elasticsearch.common.collect.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void search() throws Exception {
        Post post_1 = post(1, "phrase_one", "phrase_two", "phrase_three");
        Post post_2 = post(2, "phrase_two", "phrase_three");
        Post post_3 = post(3, "phrase_three");
        List<Post> posts = Arrays.asList(post_1, post_2, post_3);

        when(postRepository.search("q", "s", 0, 10)).thenReturn(posts);

        Tuple<List<Post>, List<Keyword>> tuple = postService.search("q", "s", 0, 10);
        assertNotNull(tuple);

        List<Post> actualPosts = tuple.v1();
        assertNotNull(actualPosts);
        assertEquals(3, actualPosts.size());

        List<Keyword> actualKeywords = tuple.v2();
        assertNotNull(actualKeywords);
        assertEquals(3, actualKeywords.size());
        assertEquals("phrase_three", actualKeywords.get(0).getKeyword());
        assertEquals(Integer.valueOf(3), actualKeywords.get(0).getRelevance());
        assertEquals("phrase_two", actualKeywords.get(1).getKeyword());
        assertEquals(Integer.valueOf(2), actualKeywords.get(1).getRelevance());
        assertEquals("phrase_one", actualKeywords.get(2).getKeyword());
        assertEquals(Integer.valueOf(1), actualKeywords.get(2).getRelevance());
    }

    @Test
    public void search_moreThan100() {
        when(postRepository.search("q", "s", 0, 199)).thenReturn(Collections.emptyList());
        postService.search("q", "s", 0, 199);

        ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(postRepository).search(eq("q"), eq("s"), eq(0), sizeCaptor.capture());
        Integer size = sizeCaptor.getValue();
        assertEquals(Integer.valueOf(100), size);
    }

    private Post post(int number, String... keyPhrases) {
        List<String> phrases = Arrays.asList(keyPhrases);
        return new Post("test_title_" + number, "test_body_" + number, "n", phrases);
    }

}