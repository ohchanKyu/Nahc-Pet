package kr.ac.dankook.cultureApplication.dto.response;

import kr.ac.dankook.cultureApplication.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostResponse {
    private Post post;
    private String author;
}
