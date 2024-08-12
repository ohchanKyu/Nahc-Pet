package kr.ac.dankook.cultureApplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BlogResponse {
    private String title;
    private String content;
    private String blogURL;
    private String blogName;
    private String blogImageURL;
    private String datetime;
}
