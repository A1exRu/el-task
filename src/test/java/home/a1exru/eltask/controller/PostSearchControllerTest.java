package home.a1exru.eltask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import home.a1exru.eltask.controller.PostSearchController.SearchResponse;
import home.a1exru.eltask.dto.Post;
import home.a1exru.eltask.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostSearchControllerTest {

    @MockBean
    private PostService postService;

    @Autowired
    private TestRestTemplate restTemplate;
    private JacksonTester<SearchResponse> jsonTester;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void search() throws IOException {
        List<Post> posts = Arrays.asList(
                new Post("test_title", "test_body", "n"),
                new Post("test_title_second", "test_body_second", "n"),
                new Post("test_title_third", "test_body_third", "n")
        );
        given(this.postService.search("test", "n", 40, 20)).willReturn(posts);

        SearchResponse result = this.restTemplate.getForObject("/posts?query=test&sentiment=n&from=40&size=20", SearchResponse.class);
        assertThat(jsonTester.write(result)).isEqualToJson("/resp/search.json");
    }

    @Test
    public void emptyResponse() throws IOException {
        List<Post> posts = Collections.emptyList();
        given(this.postService.search("test_empty", "y", 0, 10)).willReturn(posts);
        SearchResponse result = this.restTemplate.getForObject("/posts?query=test_empty&sentiment=y&from=0&size=10", SearchResponse.class);
        assertThat(jsonTester.write(result)).isEqualToJson("/resp/search_empty.json");
    }

}
