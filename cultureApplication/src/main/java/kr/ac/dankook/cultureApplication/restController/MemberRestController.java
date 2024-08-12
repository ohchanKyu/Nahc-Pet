package kr.ac.dankook.cultureApplication.restController;

import kr.ac.dankook.cultureApplication.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;
    private final FavoriteService favoriteService;
    private final AuthService authService;
    private final PlaceReviewService placeReviewService;

    @DeleteMapping("")
    public ResponseEntity<Boolean> deleteMember() throws IOException {
        commentService.deleteAllMemberComment(memberService.getLoginMemberProcess());
        postService.deleteAllMemberPost(memberService.getLoginMemberProcess());
        favoriteService.deleteAllMemberFavorite(memberService.getLoginMemberProcess());
        placeReviewService.deleteAllMemberReview(memberService.getLoginMemberProcess());
        memberService.deleteMemberProcess(memberService.getLoginMemberProcess());
        return ResponseEntity.ok(true);
    }

    @PostMapping("/name/{name}")
    public ResponseEntity<Boolean> editMemberName(@PathVariable String name){
        return ResponseEntity.ok(memberService.editMemberNameProcess(name));
    }
    @PostMapping("/email/{email}")
    public ResponseEntity<Boolean> editMemberEmail(@PathVariable String email){
        boolean isExist = authService.existsByEmailProcess(email);
        if (!isExist){
            return ResponseEntity.ok(memberService.editMemberEmailProcess(email));
        }
        return ResponseEntity.ok(false);
    }
}
