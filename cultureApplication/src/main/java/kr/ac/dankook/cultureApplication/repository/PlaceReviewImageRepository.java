package kr.ac.dankook.cultureApplication.repository;

import kr.ac.dankook.cultureApplication.entity.PlaceReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceReviewImageRepository extends JpaRepository<PlaceReviewImage,Long> {
    List<PlaceReviewImage> findByPlaceReviewId(Long reviewID);
}
