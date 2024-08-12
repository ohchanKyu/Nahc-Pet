package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.entity.*;
import kr.ac.dankook.cultureApplication.exception.ErrorCode;
import kr.ac.dankook.cultureApplication.exception.NoEntityException;
import kr.ac.dankook.cultureApplication.repository.PlaceRepository;
import kr.ac.dankook.cultureApplication.repository.PlaceReviewImageRepository;
import kr.ac.dankook.cultureApplication.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceReviewService {

    private final ReviewRepository reviewRepository;
    private final PlaceService placeService;
    private final FileService fileService;
    private final PlaceReviewImageRepository placeReviewImageRepository;
    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public List<PlaceReview> getAllReviewProcess() {
        return reviewRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PlaceReview> getReviewsByMemberId(Long memberId){
        return reviewRepository.findByMemberId(memberId);
    }
    @Transactional(readOnly = true)
    public PlaceReview checkAlreadyReviewProcess(Long memberId,Long placeId){
        Optional<PlaceReview> checkEntity = reviewRepository.findByMemberIdAndPlaceId(memberId,placeId);
        return checkEntity.orElse(null);
    }
    @Transactional
    public PlaceReview addNewReviewProcess(
            Member loginMember,
            PlaceReview newReview,
            Long placeId, MultipartFile[] fileList) {
        Place targetPlace = placeService.getPlaceByIdProcess(placeId);

        long updateCount = targetPlace.getReviewCount() + 1;
        targetPlace.setReviewCount(updateCount);
        targetPlace.setRating(targetPlace.getRating() + newReview.getRating());

        newReview.setMember(loginMember);
        newReview.setPlace(placeService.getPlaceByIdProcess(placeId));
        PlaceReview placeReview = reviewRepository.save(newReview);
        if (fileList != null){
            for (MultipartFile file : fileList) {
                fileService.fileUpload(file, placeReview);
            }
        }
        return placeReview;
    }
    @Transactional
    public List<ByteArrayResource> getReviewFileProcess(Long reviewId) throws IOException {
        Optional<PlaceReview> review = reviewRepository.findById(reviewId);
        if (review.isEmpty()){
            throw new NoEntityException(ErrorCode.NoEntityException);
        }
        List<ByteArrayResource> resources = new ArrayList<>();
        List<PlaceReviewImage> filePathList = placeReviewImageRepository.findByPlaceReviewId(review.get().getId());
        for(PlaceReviewImage image: filePathList){
            resources.add(fileService.getImageFile(image.getPath()));
        }
        return resources;
    }
    @Transactional(readOnly = true)
    public PlaceReview getOneReviewProcess(Long reviewId){
        Optional<PlaceReview> review = reviewRepository.findById(reviewId);
        if (review.isEmpty()){
            throw new NoEntityException(ErrorCode.NoEntityException);
        }
        return review.get();
    }
    @Transactional(readOnly = true)
    public List<PlaceReview> getAllReviewByPlaceIdProcess(Long placeId){
       return reviewRepository.findByPlaceId(placeId);
    }
    @Transactional
    public boolean deleteReviewByIdProcess(Long reviewId) throws IOException {
        Optional<PlaceReview> review = reviewRepository.findById(reviewId);
        if (review.isEmpty()){
            return false;
        }
        Place targetPlace = review.get().getPlace();
        long updateCount = targetPlace.getReviewCount()  - 1;
        targetPlace.setReviewCount(updateCount);
        targetPlace.setRating(targetPlace.getRating() - review.get().getRating());
        placeRepository.save(targetPlace);

        List<PlaceReviewImage> filePathList = placeReviewImageRepository.findByPlaceReviewId(review.get().getId());
        for(PlaceReviewImage image: filePathList){
            fileService.deleteFile(image.getPath());
            placeReviewImageRepository.deleteById(image.getId());
        }
        reviewRepository.deleteById(review.get().getId());
        return true;
    }
    @Transactional
    public void deleteAllMemberReview(Member member) throws IOException {
        List<PlaceReview> allReviews = reviewRepository.findByMemberId(member.getId());
        for(PlaceReview review : allReviews){
            List<PlaceReviewImage> filePathList = placeReviewImageRepository.findByPlaceReviewId(review.getId());
            for(PlaceReviewImage image: filePathList){
                fileService.deleteFile(image.getPath());
                placeReviewImageRepository.deleteById(image.getId());
            }
            reviewRepository.deleteById(review.getId());
        }
    }
    @Transactional
    public PlaceReview editOneReviewProcess(
            PlaceReview newReview,
            Long reviewId, MultipartFile[] fileList) throws IOException {

        Optional<PlaceReview> optionalEntity = reviewRepository.findById(reviewId);

        if (optionalEntity.isEmpty()){
            throw new NoEntityException(ErrorCode.NoEntityException);
        }
        PlaceReview targetReview = optionalEntity.get();

        double newRating = newReview.getRating();
        double oldRating = targetReview.getRating();
        String newText = newReview.getText();

        // 리뷰 별점 및 텍스트 업데이트
        targetReview.setRating(newRating);
        targetReview.setText(newText);

        // place 총 별점 업데이트
        Place targetPlace = targetReview.getPlace();
        double updateRating = targetPlace.getRating() + newRating - oldRating;
        targetPlace.setRating(updateRating);
        placeRepository.save(targetPlace);

        // 리뷰 기존 파일 삭제
        List<PlaceReviewImage> filePathList = placeReviewImageRepository.findByPlaceReviewId(reviewId);
        for(PlaceReviewImage image: filePathList){
            fileService.deleteFile(image.getPath());
            placeReviewImageRepository.deleteById(image.getId());
        }
        if (fileList != null){
            for (MultipartFile file : fileList) {
                fileService.fileUpload(file, targetReview);
            }
        }
        return targetReview;
    }
}
