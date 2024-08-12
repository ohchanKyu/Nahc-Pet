package kr.ac.dankook.cultureApplication.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String placeName;
    private String category;
    private String regionCode;
    private double latitude;
    private double longitude;
    private String address;
    private String phoneNumber;
    private String placeURL;
    private String holidayInfo;
    private String operatingInfo;
    private String feeInfo;
    private String maxSizeInfo;
    private String restrictionInfo;
    private String isInside;
    private String isOutside;
    private String isParking;
    private String addFeeInfo;
    private double rating = 0.0;
    private long reviewCount = 0;
}
