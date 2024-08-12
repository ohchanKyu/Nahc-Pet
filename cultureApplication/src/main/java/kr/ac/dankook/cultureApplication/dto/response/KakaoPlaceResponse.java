package kr.ac.dankook.cultureApplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class KakaoPlaceResponse {
    private double latitude;
    private double longitude;
    private String placeName;
    private String address;
    private String phoneNumber;
    private String category;
    private String placeURL;
    private RouteResponse routeResponse;
}
