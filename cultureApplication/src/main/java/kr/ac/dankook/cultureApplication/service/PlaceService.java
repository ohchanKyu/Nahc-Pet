package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.dto.request.CoordinateRequest;
import kr.ac.dankook.cultureApplication.dto.request.KeywordRequest;
import kr.ac.dankook.cultureApplication.dto.request.LocationRequest;
import kr.ac.dankook.cultureApplication.dto.response.AroundPlaceResponse;
import kr.ac.dankook.cultureApplication.dto.response.DistancePlaceResponse;
import kr.ac.dankook.cultureApplication.entity.Place;
import kr.ac.dankook.cultureApplication.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final LocationService locationService;

    @Transactional(readOnly = true)
    public Place getPlaceByIdProcess(Long placeId){
        Optional<Place> targetPlace = placeRepository.findById(placeId);
        return targetPlace.orElse(null);
    }
    @Transactional(readOnly = true)
    public List<Place> getPlaceByKeywordProcess(KeywordRequest keywordRequest){
        List<Place> filteredList;
        if (keywordRequest.getPlaceName() != null && !keywordRequest.getPlaceName().isEmpty()){
            filteredList = placeRepository.findByCategoryAndPlaceNameContaining(
                    keywordRequest.getCategory(),keywordRequest.getPlaceName()
            );
        }else{
            filteredList = placeRepository.findByCategory(keywordRequest.getCategory());
        }
        if (!keywordRequest.getRegionCode().equals("지역 선택")){
            filteredList =  filteredList.stream()
                    .filter(place -> place.getRegionCode().equals(keywordRequest.getRegionCode()))
                    .collect(Collectors.toList());
        }
        if (keywordRequest.isParking()){
            filteredList =  filteredList.stream()
                    .filter(place -> place.getIsParking().equals("주차가능"))
                    .collect(Collectors.toList());
        }
        if (keywordRequest.isInside()){
            filteredList =  filteredList.stream()
                    .filter(place -> place.getIsInside().equals("실내 동반가능"))
                    .collect(Collectors.toList());
        }
        if (keywordRequest.isOutside()){
            filteredList =  filteredList.stream()
                    .filter(place -> place.getIsOutside().equals("실외 동반가능"))
                    .collect(Collectors.toList());
        }
        return filteredList;
    }

    @Transactional(readOnly = true)
    public List<Place> getPlaceByCategoryProcess(String category){
        return placeRepository.findByCategory(category);
    }
    @Transactional(readOnly = true)
    public List<DistancePlaceResponse> getAllCalculateAndSortByDistProcess(CoordinateRequest coordinateRequest,String category){
        List<Place> allCategoryPlaceList;
        if (category.equals("all")){
            allCategoryPlaceList = placeRepository.findAll();
        }else{
            allCategoryPlaceList = getPlaceByCategoryProcess(category);
        }
        List<DistancePlaceResponse> distancePlaceList = new ArrayList<>();
        for(Place place : allCategoryPlaceList){
            LocationRequest locationRequest = new LocationRequest(
                    coordinateRequest.getLatitude(),
                    coordinateRequest.getLongitude(),
                    place.getLatitude(),
                    place.getLongitude()
            );
            distancePlaceList.add(
                    new DistancePlaceResponse(
                            place,getDistanceProcess(locationRequest),locationRequest
                    )
            );
        }
        distancePlaceList.sort(Comparator.comparingDouble(DistancePlaceResponse::getDistance));
        return distancePlaceList;
    }
    @Transactional(readOnly = true)
    public List<AroundPlaceResponse> getAroundPlaceLotProcess(
            CoordinateRequest coordinateRequest, String category) {
        List<DistancePlaceResponse> distancePlaceList = getAllCalculateAndSortByDistProcess(
                coordinateRequest, category
        );
        return getRouteInfoProcess(distancePlaceList.subList(0, Math.min(5, distancePlaceList.size())));
    }

    public List<AroundPlaceResponse> getRouteInfoProcess(List<DistancePlaceResponse> sortPlaceLists){
        List<AroundPlaceResponse> resultList = new ArrayList<>();
        for(DistancePlaceResponse distancePlaceResponse : sortPlaceLists){
            resultList.add(
                    new AroundPlaceResponse(
                            distancePlaceResponse.getPlace(),
                            locationService.getRouteInfoProcess(distancePlaceResponse.getLocationRequest())
                    )
            );
        }
        return resultList;
    }
    private double getDistanceProcess(LocationRequest locationRequest) {
        double theta = locationRequest.getEndLongitude() - locationRequest.getStartLongitude();
        double dist = Math.sin(deg2rad(locationRequest.getStartLatitude())) *
                Math.sin(deg2rad(locationRequest.getEndLatitude())) +
                Math.cos(deg2rad(locationRequest.getStartLatitude())) *
                        Math.cos(deg2rad(locationRequest.getEndLatitude())) *
                        Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1609.344;
        return dist / 1000;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
