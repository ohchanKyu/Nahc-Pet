package kr.ac.dankook.cultureApplication.restController;

import kr.ac.dankook.cultureApplication.entity.Comment;
import kr.ac.dankook.cultureApplication.entity.Post;
import kr.ac.dankook.cultureApplication.service.CommentService;
import kr.ac.dankook.cultureApplication.service.MemberService;
import kr.ac.dankook.cultureApplication.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentRestController {

    private final CommentService commentService;
    private final MemberService memberService;
    private final PostService postService;

    @PostMapping("/add/{postId}")
    public ResponseEntity<Comment> addNewComment(
            @PathVariable Long postId,
            @RequestBody Comment newComment) {
        return ResponseEntity.ok(commentService.addCommentProcess(
                memberService.getLoginMemberProcess(),
                postService.findOneTargetPost(postId)
                ,newComment
        ));
    }
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteOneCommentProcess(commentId));
    }
    @PatchMapping("/patch/{commentId}")
    public ResponseEntity<Comment> pathComment(
            @RequestBody Comment editComment,
            @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.editCommentProcess(commentId,editComment));
    }
}
