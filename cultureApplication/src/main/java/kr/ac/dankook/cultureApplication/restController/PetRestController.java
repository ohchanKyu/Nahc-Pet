package kr.ac.dankook.cultureApplication.restController;

import kr.ac.dankook.cultureApplication.dto.request.CoordinateRequest;
import kr.ac.dankook.cultureApplication.dto.request.KeywordRequest;
import kr.ac.dankook.cultureApplication.dto.response.AroundPlaceResponse;
import kr.ac.dankook.cultureApplication.entity.Place;
import kr.ac.dankook.cultureApplication.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/pet")
@RequiredArgsConstructor
public class PetRestController {

    private final PlaceService placeService;

    @PostMapping("/around/{category}")
    public ResponseEntity<List<AroundPlaceResponse>> getAroundPlace(
            @RequestBody CoordinateRequest coordinatesRequest,
            @PathVariable String category
    ) {
        return ResponseEntity.ok(placeService.getAroundPlaceLotProcess(coordinatesRequest,category));
    }

    @PostMapping("/keyword")
    public ResponseEntity<List<Place>> getSearchPlace(
            @RequestBody KeywordRequest keywordRequest){
        return ResponseEntity.ok(placeService.getPlaceByKeywordProcess(
                keywordRequest));
    }
}
