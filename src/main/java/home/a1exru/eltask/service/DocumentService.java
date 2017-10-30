package home.a1exru.eltask.service;

import home.a1exru.eltask.dto.Document;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DocumentService {

    public List<Document> search(String query, String sentiment) {
        return Collections.emptyList();
    }

}
