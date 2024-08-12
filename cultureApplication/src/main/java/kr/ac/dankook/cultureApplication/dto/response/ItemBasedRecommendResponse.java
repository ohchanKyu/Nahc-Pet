package kr.ac.dankook.cultureApplication.dto.response;

import kr.ac.dankook.cultureApplication.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemBasedRecommendResponse {
    private Place place;
    private double score;
}
