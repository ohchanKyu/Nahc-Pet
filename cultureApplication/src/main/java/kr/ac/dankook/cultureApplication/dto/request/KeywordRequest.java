package kr.ac.dankook.cultureApplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KeywordRequest {
    private String placeName;
    private String category;
    private String regionCode;
    private boolean isParking;
    private boolean isInside;
    private boolean isOutside;
}
