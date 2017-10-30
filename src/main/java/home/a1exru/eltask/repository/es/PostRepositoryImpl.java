package home.a1exru.eltask.repository.es;

import home.a1exru.eltask.dto.Post;
import home.a1exru.eltask.repository.PostRepository;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Repository
public class PostRepositoryImpl implements PostRepository {

    @Autowired
    private Client client;

    @Override
    public List<Post> search(String query, String sentiment, int from, int size) {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        queryBuilder.should();

        applyQuery(query, queryBuilder);
        applySentiment(sentiment, queryBuilder);


        SearchRequestBuilder criteria = client.prepareSearch("documents")
                .setQuery(queryBuilder)
                .setFrom(from)
                .setSize(size);
        SearchResponse response = client.search(criteria.request()).actionGet();

        return Stream.of(response.getHits().getHits())
                .map(h -> toPost(h.getSource()))
                .collect(toList());
    }

    private void applyQuery(String query, BoolQueryBuilder queryBuilder) {
        if (!Objects.equals("", query)) {
            QueryStringQueryBuilder searchQuery = QueryBuilders.queryStringQuery(query)
                    .field("title")
                    .field("body");
            queryBuilder.must(searchQuery);
        }
    }

    private void applySentiment(String sentiment, BoolQueryBuilder queryBuilder) {
        if (!Objects.equals("", sentiment)) {
            TermQueryBuilder sentimentFilter = QueryBuilders.termQuery("sentiment", sentiment);
            queryBuilder.must(sentimentFilter);
        }
    }

    private Post toPost(Map<String, Object> source) {
        String title = Objects.toString(source.get("title"));
        String body = Objects.toString(source.get("body"));
        String sentiment = Objects.toString(source.get("sentiment"));
        List<String> keyPhrases = (List<String>) source.get("keyPhrases");
        return new Post(title, body, sentiment, keyPhrases);
    }

}
