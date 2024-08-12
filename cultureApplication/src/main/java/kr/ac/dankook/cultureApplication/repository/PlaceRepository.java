package kr.ac.dankook.cultureApplication.repository;

import kr.ac.dankook.cultureApplication.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place,Long> {
    List<Place> findByCategory(String category);
    List<Place> findByCategoryAndPlaceNameContaining(String category, String placeName);
}
