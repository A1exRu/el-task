package home.a1exru.eltask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import home.a1exru.eltask.controller.DocumentSearchController.SearchResponse;
import home.a1exru.eltask.dto.Document;
import home.a1exru.eltask.service.DocumentService;
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
public class DocumentSearchControllerTest {

    @MockBean
    private DocumentService documentService;

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
        List<Document> documents = Arrays.asList(
                new Document("test_title", "test_body", "n"),
                new Document("test_title_second", "test_body_second", "n"),
                new Document("test_title_third", "test_body_third", "n")
        );
        given(this.documentService.search("test", "n")).willReturn(documents);

        SearchResponse result = this.restTemplate.getForObject("/documents?query=test&sentiment=n", SearchResponse.class);
        assertThat(jsonTester.write(result)).isEqualToJson("/resp/search.json");
    }

    @Test
    public void emptyResponse() throws IOException {
        List<Document> documents = Collections.emptyList();
        given(this.documentService.search("test_empty", "y")).willReturn(documents);
        SearchResponse result = this.restTemplate.getForObject("/documents?query=test_empty&sentiment=y", SearchResponse.class);
        assertThat(jsonTester.write(result)).isEqualToJson("/resp/search_empty.json");
    }

}
