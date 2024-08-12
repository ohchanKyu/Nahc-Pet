package kr.ac.dankook.cultureApplication.restController;

import kr.ac.dankook.cultureApplication.service.FavoriteService;
import kr.ac.dankook.cultureApplication.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteRestController {

    private final FavoriteService favoriteService;
    private final MemberService memberService;

    @PostMapping("/add/{placeId}")
    public ResponseEntity<Boolean> addPlaceFavorite(@PathVariable Long placeId){
        return ResponseEntity.ok(favoriteService.addFavoriteProcess(
                memberService.getLoginMemberProcess(),placeId
        ));
    }
    @DeleteMapping("/delete/{favoriteId}")
    public ResponseEntity<Boolean> deletePlaceFavorite(@PathVariable Long favoriteId){
        return ResponseEntity.ok(favoriteService.deleteFavoriteProcess(favoriteId));
    }
    @GetMapping("/is-favorite/{placeId}")
    public ResponseEntity<Long> checkingIsFavorite(@PathVariable Long placeId){
        return ResponseEntity.ok(favoriteService.checkingPlaceIsFavorite(
                memberService.getLoginMemberProcess(),placeId
        ));
    }
}
