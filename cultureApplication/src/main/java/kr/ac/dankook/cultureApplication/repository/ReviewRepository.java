package kr.ac.dankook.cultureApplication.repository;

import kr.ac.dankook.cultureApplication.entity.PlaceReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<PlaceReview,Long> {
    List<PlaceReview> findByMemberId(Long memberId);
    Optional<PlaceReview> findByMemberIdAndPlaceId(Long memberId, Long placeId);
    List<PlaceReview> findByPlaceId(Long placeId);
}
