package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.dto.request.CoordinateRequest;
import kr.ac.dankook.cultureApplication.dto.response.AroundPlaceResponse;
import kr.ac.dankook.cultureApplication.dto.response.DistancePlaceResponse;
import kr.ac.dankook.cultureApplication.dto.response.ItemBasedRecommendResponse;
import kr.ac.dankook.cultureApplication.dto.response.WeightByDistResponse;
import kr.ac.dankook.cultureApplication.entity.Place;
import kr.ac.dankook.cultureApplication.entity.PlaceReview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendService {

    private final MemberService memberService;
    private final PlaceService placeService;

    private double calculatePearsonCorrelation(Map<Long, Double> firstUserRating, Map<Long, Double> secondUserRating){

        Set<Long> commonItems = new HashSet<>(firstUserRating.keySet());
        commonItems.retainAll(secondUserRating.keySet());
        if (commonItems.isEmpty()){
            return 0.0;
        }

        double sx = 0.0;
        double sy = 0.0;
        double sxx = 0.0;
        double syy = 0.0;
        double sxy = 0.0;
        int size = commonItems.size();

        for (Long item : commonItems) {
            double firstRating = firstUserRating.get(item);
            double secondRating = secondUserRating.get(item);

            sx += firstRating;
            sy += secondRating;
            sxx += Math.pow(firstRating, 2);
            syy += Math.pow(secondRating, 2);
            sxy += firstRating * secondRating;
        }

        double num = sxy - (sx * sy / size);
        double den = Math.sqrt((sxx - Math.pow(sx, 2) / size) * (syy - Math.pow(sy, 2) / size));
        return (den == 0) ? 0.0 : num / den;
    }
    public List<ItemBasedRecommendResponse> getRecommendBasedItemProcess(List<PlaceReview> clusterReview){

        // 클러스팅한 사용자별 리뷰 데이터 맵핑
        Map<Long,Map<Long,Double>> userRatings = clusterReview.stream()
                .collect(Collectors.groupingBy(
                        review -> review.getMember().getId(),
                        Collectors.toMap(
                                review -> review.getPlace().getId(),
                                PlaceReview::getRating
                        )
                ));
//        출력 형태
//        User ID: 17
//        Place ID: 5491, Rating: 5.0
//        Place ID: 15221, Rating: 1.5
//        Place ID: 12504, Rating: 3.0
//        Place ID: 1260, Rating: 4.0

//        User ID: 18
//        Place ID: 5491, Rating: 5.0
//        Place ID: 19957, Rating: 5.0
//        Place ID: 12090, Rating: 5.0
        userRatings.forEach((userId, ratings) -> {
            log.info("Cluster User Id : -{}",userId);
            ratings.forEach((placeId, rating) -> {
                log.info("\tPlace Id : -{}, Rating -{}",placeId,rating);
            });
        });
        // 현재 사용자의 리뷰 데이터만 가져오기
        Long memberId = memberService.getLoginMemberProcess().getId();
        Map<Long,Double> targetUserRatings = userRatings.get(memberId);
        if (targetUserRatings == null){
            return Collections.emptyList();
        }
        // 피어슨 상관계수를 통해 사용자 상관계수 구함
        Map<Long,Double> similarityScores = new HashMap<>();
        for(Long userId : userRatings.keySet()){
            if (!userId.equals(memberId)){
                double similarity = calculatePearsonCorrelation(targetUserRatings,userRatings.get(userId));
                log.info("Similarity User Score - {}",similarity);
                similarityScores.put(userId,similarity);
            }
        }
        Map<Long,Double> totalScores = new HashMap<>();
        Map<Long,Integer> similarityCount = new HashMap<>();
        for(Long userId : userRatings.keySet()){
            
            // 유사도 계산에 포함되어있고 유사도가 0보다 큰 경우 음수일 경우는 제외가 해야 추천하기 좋음
            if (similarityScores.containsKey(userId) && similarityScores.get(userId) > 0){
                // 해당 Member Id의 리뷰 데이터를 가져옴
                Map<Long,Double> otherUserRatings = userRatings.get(userId);
                // 해당 Member Id의 유사도를 가져옴
                double similarity = similarityScores.get(userId);
                // 해당 Member Id의 모든 리뷰 Set에 대하여
                for(Map.Entry<Long,Double> entry : otherUserRatings.entrySet()){
                    // 장소 아이디
                    Long placeId = entry.getKey();
                    // 장소 평점
                    Double rating = entry.getValue();
                    // 만약 대상 Member가 해당 PlaceId에 대한 리뷰를 포함하지 않고 있다면 추천 대상
                    if (!targetUserRatings.containsKey(placeId)){
                        // 해당 점수를 계산해서 total Score에 넣기
                        // Score = 별점 * 유사도
                        // 별점이 높을수록 추천하기 좋다. 최대 점수는 5점 > Rating = 5.0 유사도 = 1.0
                        totalScores.put(placeId, totalScores.getOrDefault(placeId,0.0) + rating * similarity);
                        // 총 Count를 센다.
                        similarityCount.put(placeId, similarityCount.getOrDefault(placeId,0) + 1);
                    }
                }
            }
        }
        // 추천 Place List
        List<ItemBasedRecommendResponse> recommendResponseList = new ArrayList<>();
        // 총 스코어 리스트에 있는 모든 Entry에 대하여
        for(Map.Entry<Long,Double> entry : totalScores.entrySet()){
            Place targetPlace = placeService.getPlaceByIdProcess(entry.getKey());
            double rating = entry.getValue() / similarityCount.get(entry.getKey());
            recommendResponseList.add(new ItemBasedRecommendResponse(targetPlace,rating));
        }
        // 내림차순으로 정렬
        recommendResponseList.sort((e1, e2) -> Double.compare(e2.getScore(),e1.getScore()));
        // 최대 5개만 지정
        return recommendResponseList.subList(0, Math.min(recommendResponseList.size(), 5));
    }
    // 거리 및 리뷰 가중치의 합을 통해 장소 추천
    public List<AroundPlaceResponse> getRecommendBasedDistProcess(CoordinateRequest request){
        // 거리 순으로 정렬된 데이터
        List<DistancePlaceResponse> distancePlaceResponses = placeService.getAllCalculateAndSortByDistProcess(request,"all");
        List<WeightByDistResponse> weightList = new ArrayList<>();
        List<DistancePlaceResponse> newDistPlaceResponse = new ArrayList<>();
        double ratingWeight = 0.3;
        double distWeight = 0.6;
        double countWeight = 0.1;
        for(DistancePlaceResponse distancePlaceResponse : distancePlaceResponses){
            Place targetPlace = distancePlaceResponse.getPlace();
            double weight = (targetPlace.getRating() * ratingWeight) + (targetPlace.getReviewCount() * countWeight) - (distancePlaceResponse.getDistance() * distWeight);
            weightList.add(new WeightByDistResponse(distancePlaceResponse,weight
                    ));
        }
        weightList.sort((r1, r2) -> Double.compare(r2.getWeight(), r1.getWeight()));
        for(int i=0;i<5;i++){
            log.info("Place name - {} weight - {}",weightList.get(i).getDistPlace().getPlace().getPlaceName(),weightList.get(i).getWeight());
            newDistPlaceResponse.add(weightList.get(i).getDistPlace());
        }
        return placeService.getRouteInfoProcess(newDistPlaceResponse);
    }
}
