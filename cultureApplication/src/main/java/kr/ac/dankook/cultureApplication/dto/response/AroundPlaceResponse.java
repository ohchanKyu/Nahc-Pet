package kr.ac.dankook.cultureApplication.dto.response;

import kr.ac.dankook.cultureApplication.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AroundPlaceResponse {
    private Place place;
    private RouteResponse routeResponse;
}
