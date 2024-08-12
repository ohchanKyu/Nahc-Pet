package kr.ac.dankook.cultureApplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeightByDistResponse {
    private DistancePlaceResponse distPlace;
    private double weight;
}
