package kr.ac.dankook.cultureApplication.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity(name = "PlaceReviewImage")
public class PlaceReviewImage extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "placeReview_id", nullable = false)
    private PlaceReview placeReview;

    private String path;
}
