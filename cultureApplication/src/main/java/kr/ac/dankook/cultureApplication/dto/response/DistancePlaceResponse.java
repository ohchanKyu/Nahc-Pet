package kr.ac.dankook.cultureApplication.dto.response;

import kr.ac.dankook.cultureApplication.dto.request.LocationRequest;
import kr.ac.dankook.cultureApplication.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class DistancePlaceResponse {
    private Place place;
    private double distance;
    private LocationRequest locationRequest;
}
