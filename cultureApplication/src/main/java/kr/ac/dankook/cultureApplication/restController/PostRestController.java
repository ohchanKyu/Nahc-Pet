package kr.ac.dankook.cultureApplication.restController;

import kr.ac.dankook.cultureApplication.entity.Post;
import kr.ac.dankook.cultureApplication.service.MemberService;
import kr.ac.dankook.cultureApplication.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostRestController {

    private final PostService postService;
    private final MemberService memberService;

    @PostMapping("/add")
    public ResponseEntity<Post> addNewPost(@RequestBody Post newPost) throws IOException {
        return ResponseEntity.ok(postService.addNewPostProcess(
                memberService.getLoginMemberProcess(),newPost
        ));
    }
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Boolean> deletePost(@PathVariable Long postId) throws IOException {
        return ResponseEntity.ok(postService.deleteOnePostProcess(postId));
    }
    @PatchMapping("/patch/{postId}")
    public ResponseEntity<Post> patchPost(@RequestBody Post editPost,@PathVariable Long postId) throws IOException {
        return ResponseEntity.ok(postService.editPostProcess(postId,editPost));
    }
    @PostMapping("/delete/list")
    public ResponseEntity<Boolean> deletePosts(@RequestParam("posts") List<Long> postsId) throws IOException {
        boolean isSuccess = false;
        for(Long id : postsId){
            isSuccess = postService.deleteOnePostProcess(id);
        }
        return ResponseEntity.ok(isSuccess);
    }
}
