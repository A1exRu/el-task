package home.a1exru.eltask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class Post {

    String title;
    String body;
    String sentiment;

}
