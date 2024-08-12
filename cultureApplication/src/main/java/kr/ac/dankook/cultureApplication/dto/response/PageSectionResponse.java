package kr.ac.dankook.cultureApplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PageSectionResponse {
    private int currentSection;
    private int endSection;
}
