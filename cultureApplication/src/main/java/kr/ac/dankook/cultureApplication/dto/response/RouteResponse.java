package kr.ac.dankook.cultureApplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RouteResponse {
    private String distance;
    private String time;
    private String taxiFare;
    private String tollFare;
}
