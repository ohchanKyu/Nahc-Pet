package kr.ac.dankook.cultureApplication.restController;

import kr.ac.dankook.cultureApplication.dto.request.CoordinateRequest;
import kr.ac.dankook.cultureApplication.dto.response.AroundPlaceResponse;
import kr.ac.dankook.cultureApplication.dto.response.ItemBasedRecommendResponse;
import kr.ac.dankook.cultureApplication.dto.response.RecommendPlaceResponse;
import kr.ac.dankook.cultureApplication.entity.PlaceReview;
import kr.ac.dankook.cultureApplication.service.ClusteringService;
import kr.ac.dankook.cultureApplication.service.PlaceReviewService;
import kr.ac.dankook.cultureApplication.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendRestController {

    private final RecommendService recommendService;
    private final ClusteringService clusteringService;
    private final PlaceReviewService placeReviewService;

    @PostMapping("/place")
    public ResponseEntity<RecommendPlaceResponse> recommendPlaceList(@RequestBody CoordinateRequest request){
        // 아이템 기반 협업 필터링 추천 
        // 클러스터링된 사용자 그룹 가져옴 O
        Set<Long> userCluster = clusteringService.clusterByFavoritesProcess();
        List<PlaceReview> allReviews = placeReviewService.getAllReviewProcess();
        // 클러스터 된 리뷰만 가져옴 O
        List<PlaceReview> clusterReviews = allReviews.stream()
                .filter(review -> userCluster.contains(review.getMember().getId()))
                .toList();
        List<ItemBasedRecommendResponse> itemBasedList = recommendService.getRecommendBasedItemProcess(clusterReviews);
        // 현재 가까운 거리 및 리뷰 점수를 통해 측정
        List<AroundPlaceResponse> distBasedList = recommendService.getRecommendBasedDistProcess(request);
        return ResponseEntity.ok(new RecommendPlaceResponse(itemBasedList,distBasedList));
    }
}
