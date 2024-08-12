package kr.ac.dankook.cultureApplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendPlaceResponse {
    List<ItemBasedRecommendResponse> itemBasedList;
    List<AroundPlaceResponse> distBasedList;
}
