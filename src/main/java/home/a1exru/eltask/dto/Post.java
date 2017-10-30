package home.a1exru.eltask.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
public class Post {

    String title;
    String body;
    String sentiment;

    @JsonIgnore
    List<String> keyPhrases;

}
