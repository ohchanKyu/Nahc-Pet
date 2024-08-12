package kr.ac.dankook.cultureApplication.restController;

import kr.ac.dankook.cultureApplication.dto.response.ImageResponse;
import kr.ac.dankook.cultureApplication.entity.PlaceReview;
import kr.ac.dankook.cultureApplication.service.MemberService;
import kr.ac.dankook.cultureApplication.service.PlaceReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewRestController {

    private final PlaceReviewService placeReviewService;
    private final MemberService memberService;

    @PostMapping("/add/{placeId}")
    public ResponseEntity<PlaceReview> addNewReview(
            @RequestPart("review") PlaceReview newReview, @PathVariable Long placeId,
            @RequestParam(value = "file", required = false) MultipartFile[] fileList) throws IOException {
        return ResponseEntity.ok(placeReviewService.addNewReviewProcess(
                memberService.getLoginMemberProcess(),newReview,placeId,fileList
        ));
    }
    @PostMapping("/edit/{reviewId}")
    public ResponseEntity<PlaceReview> editOneReview(
            @RequestPart("review") PlaceReview newReview, @PathVariable Long reviewId,
            @RequestParam(value = "file", required = false) MultipartFile[] fileList) throws IOException {
            return ResponseEntity.ok(placeReviewService.editOneReviewProcess(
                    newReview,reviewId,fileList
            )
        );
    }
    @GetMapping("/check/{placeId}")
    public ResponseEntity<PlaceReview> checkIsReview(@PathVariable Long placeId) {
        return ResponseEntity.ok(placeReviewService.checkAlreadyReviewProcess(
                memberService.getLoginMemberProcess().getId(), placeId
        ));
    }
    @GetMapping("/{reviewId}")
    public ResponseEntity<PlaceReview> getOneReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(placeReviewService.getOneReviewProcess(reviewId)
        );
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<Boolean> deletePost(@PathVariable Long reviewId) throws IOException {
        return ResponseEntity.ok(placeReviewService.deleteReviewByIdProcess(reviewId));
    }
    @GetMapping("/file/{reviewId}")
    @ResponseBody
    public ResponseEntity<List<ImageResponse>> getReviewFile(@PathVariable Long reviewId) throws IOException {
        List<ByteArrayResource> fileResources = placeReviewService.getReviewFileProcess(reviewId);
        List<ImageResponse> fileList = new ArrayList<>();

        for (ByteArrayResource resource : fileResources) {
            byte[] bytes = resource.getByteArray();
            String base64String = Base64.getEncoder().encodeToString(bytes);
            String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(bytes));
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            fileList.add(new ImageResponse(base64String,mimeType));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("no-cache");
        return ResponseEntity.ok().headers(headers).body(fileList);
    }
    @GetMapping("/getAll/{placeId}")
    public ResponseEntity<List<PlaceReview>> getAllReviewByPlaceId(@PathVariable Long placeId) {
        return ResponseEntity.ok(placeReviewService.getAllReviewByPlaceIdProcess(placeId));
    }
}
