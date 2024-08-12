package kr.ac.dankook.cultureApplication.repository;

import kr.ac.dankook.cultureApplication.entity.PlaceFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<PlaceFavorite,Long> {
    Optional<PlaceFavorite> findByMemberIdAndPlaceId(Long memberId, Long placeId);
    List<PlaceFavorite> findByMemberId(Long memberId);
}
