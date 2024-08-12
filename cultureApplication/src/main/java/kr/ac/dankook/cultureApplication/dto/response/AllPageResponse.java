package kr.ac.dankook.cultureApplication.dto.response;

import kr.ac.dankook.cultureApplication.entity.Post;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AllPageResponse {
    private int currentSection;
    private int endSection;
    private List<PostResponse> posts;
    private int currentPage;
    private int totalPage;
    private int totalPost;
    private String type;
}
