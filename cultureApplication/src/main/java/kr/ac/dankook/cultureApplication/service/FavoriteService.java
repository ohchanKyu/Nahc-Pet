package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.entity.Member;
import kr.ac.dankook.cultureApplication.entity.Place;
import kr.ac.dankook.cultureApplication.entity.PlaceFavorite;
import kr.ac.dankook.cultureApplication.exception.ErrorCode;
import kr.ac.dankook.cultureApplication.exception.NoEntityException;
import kr.ac.dankook.cultureApplication.repository.FavoriteRepository;
import kr.ac.dankook.cultureApplication.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteService {

    private final PlaceRepository placeRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional(readOnly = true)
    public List<PlaceFavorite> getAllUserFavoriteProcess(){
        return favoriteRepository.findAll();
    }
    @Transactional
    public void deleteAllMemberFavorite(Member member){
        List<PlaceFavorite> targetLists = getAllFavoriteProcess(member);
        for(PlaceFavorite placeFavorite : targetLists){
            favoriteRepository.deleteById(placeFavorite.getId());
        }
    }
    @Transactional(readOnly = true)
    public List<PlaceFavorite> getAllFavoriteProcess(Member member){
        return favoriteRepository.findByMemberId(member.getId());
    }
    @Transactional(readOnly = true)
    public Long checkingPlaceIsFavorite(Member member,Long placeId){
        Optional<PlaceFavorite> targetEntity = favoriteRepository.findByMemberIdAndPlaceId(member.getId(),placeId);
        return targetEntity.map(PlaceFavorite::getId).orElse(null);
    }
    @Transactional
    public boolean addFavoriteProcess(Member member, Long placeId){
        Optional<PlaceFavorite> targetPlace = favoriteRepository.findByMemberIdAndPlaceId(member.getId(),placeId);
        if (targetPlace.isPresent()){
            return false;
        }
        Optional<Place> place = placeRepository.findById(placeId);
        if (place.isEmpty()){
            throw new NoEntityException(ErrorCode.NoEntityException);
        }
        PlaceFavorite placeFavorite = PlaceFavorite.builder()
                .member(member)
                .place(place.get())
                .build();
        favoriteRepository.save(placeFavorite);
        return true;
    }
    @Transactional
    public boolean deleteFavoriteProcess(Long favoriteId){
        Optional<PlaceFavorite> targetPlace = favoriteRepository.findById(favoriteId);
        if (targetPlace.isPresent()){
            favoriteRepository.deleteById(targetPlace.get().getId());
            return true;
        }
        return false;
    }
}
