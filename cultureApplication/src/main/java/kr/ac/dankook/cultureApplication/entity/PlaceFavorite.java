package kr.ac.dankook.cultureApplication.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "placefavorite")
public class PlaceFavorite extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Builder
    public PlaceFavorite(Member member,Place place) {
        this.member = member;
        this.place = place;
    }
}
