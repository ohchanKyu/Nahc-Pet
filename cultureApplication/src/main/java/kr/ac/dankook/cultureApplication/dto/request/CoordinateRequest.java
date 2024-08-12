package kr.ac.dankook.cultureApplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CoordinateRequest {
    private double latitude;
    private double longitude;
}
