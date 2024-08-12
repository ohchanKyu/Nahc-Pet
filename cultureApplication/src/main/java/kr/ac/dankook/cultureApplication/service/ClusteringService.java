package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.entity.PlaceFavorite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClusteringService {

    private final MemberService memberService;
    private final FavoriteService favoriteService;

    public Set<Long> clusterByFavoritesProcess(){

        List<PlaceFavorite> allList = favoriteService.getAllUserFavoriteProcess();
        // 사용자의 즐겨찾기 리스트
        List<PlaceFavorite> favoriteList = favoriteService.getAllFavoriteProcess( memberService.getLoginMemberProcess());
        if (favoriteList.isEmpty()){
            return Collections.emptySet();
        }
        // 사용자의 즐겨찾기 장소 ID 리스트
        Set<Long> favoritePlaceIds = favoriteList.stream()
                .map(favorite -> favorite.getPlace().getId())
                .collect(Collectors.toSet());
        // 사용자 클러스터링
        return allList.stream()
                .filter(favorite -> favoritePlaceIds.contains(favorite.getPlace().getId()))
                .map(favorite -> favorite.getMember().getId())
                .collect(Collectors.toSet());

    }
}
