package kr.ac.dankook.cultureApplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LocationRequest {
    private double startLatitude;
    private  double startLongitude;
    private double endLatitude;
    private double endLongitude;
}
