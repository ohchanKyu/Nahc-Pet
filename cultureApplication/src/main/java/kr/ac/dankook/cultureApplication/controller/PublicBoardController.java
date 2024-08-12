package kr.ac.dankook.cultureApplication.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.ac.dankook.cultureApplication.entity.Comment;
import kr.ac.dankook.cultureApplication.entity.Member;
import kr.ac.dankook.cultureApplication.service.CommentService;
import kr.ac.dankook.cultureApplication.service.MemberService;
import kr.ac.dankook.cultureApplication.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/public/board")
@RequiredArgsConstructor
public class PublicBoardController {

    private final PostService postService;
    private final MemberService memberService;
    private final CommentService commentService;

    @RequestMapping("/type/{type}")
    public String boardPage(Model model,
                            @RequestParam(name="keyword", required = false) String keyword,
                            @PathVariable String type,
                            @RequestParam(name="start",required = false,defaultValue ="0") int start){

        model.addAttribute("start",start);
        model.addAttribute("type",type);
        return "page/board/board.html";
    }

    @RequestMapping("/{postId}")
    public String boardDetailPage(@PathVariable Long postId, Model model, HttpServletRequest request) {
        Member loginMember = memberService.getLoginMemberProcess();
        String clientIpAddress = (String) request.getRemoteAddr();

        if (loginMember != null){
            model.addAttribute("role",memberService.getLoginMemberProcess().getRole());
            model.addAttribute("userId", loginMember.getId());
        }else{
            model.addAttribute("userId", null);
        }
        List<Comment> allComments = commentService.getAllCommentByPostId(postId);
        model.addAttribute("commentLength", allComments.size());
        model.addAttribute("comment",allComments);
        model.addAttribute("result", postService.updateTotalCountProcess(postId,clientIpAddress));
        return "page/board/boardDetail.html";
    }
}
