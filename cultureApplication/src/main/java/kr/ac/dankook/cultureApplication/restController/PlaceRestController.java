package kr.ac.dankook.cultureApplication.restController;

import kr.ac.dankook.cultureApplication.entity.Place;
import kr.ac.dankook.cultureApplication.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceRestController {

    private final PlaceService placeService;

    @GetMapping("/{placeId}")
    public ResponseEntity<Place> getPlaceById(@PathVariable("placeId") Long placeId) {
        return ResponseEntity.ok(placeService.getPlaceByIdProcess(placeId));
    }
}
