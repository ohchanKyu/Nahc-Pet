package kr.ac.dankook.cultureApplication.restController;

import kr.ac.dankook.cultureApplication.dto.request.CoordinateRequest;
import kr.ac.dankook.cultureApplication.dto.request.LocationRequest;
import kr.ac.dankook.cultureApplication.dto.response.*;
import kr.ac.dankook.cultureApplication.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/location")
@RequiredArgsConstructor
public class LocationRestController {

    private final LocationService locationService;

    @GetMapping("/{address}")
    public ResponseEntity<CoordinateResponse> getCoordinateByAddress(@PathVariable String address) {
        return ResponseEntity.ok(locationService.getCoordinateProcess(address));
    }

    @PostMapping("/around/cafe")
    public ResponseEntity<List<KakaoPlaceResponse>> getAroundPetCafeList(@RequestBody CoordinateRequest coordinateRequest){
        return ResponseEntity.ok(locationService.getAroundPlaceProcess(coordinateRequest,"애견카페"));
    }
    @PostMapping("/around/education")
    public ResponseEntity<List<KakaoPlaceResponse>> getAroundPetEducationList(@RequestBody CoordinateRequest coordinateRequest){
        return ResponseEntity.ok(locationService.getAroundPlaceProcess(coordinateRequest,"애견 훈련"));
    }

    @PostMapping("/traffic")
    public ResponseEntity<RouteResponse> getTrafficInformation(@RequestBody LocationRequest locationRequest){
        return ResponseEntity.ok(locationService.getRouteInfoProcess(locationRequest));
    }
    @PostMapping("/blog")
    public ResponseEntity<List<BlogResponse>> getPetBlogInformation(){
        return ResponseEntity.ok(locationService.getPetBlogProcess("애견 훈련 관련 유용한 팁"));
    }
//    @PostMapping("/image")
//    public ResponseEntity<List<ImageResponse>> getPlaceImageInformation(){
//        return ResponseEntity.ok(locationService.getPlaceImage("댕그랑땡 장소"));
//    }
}
