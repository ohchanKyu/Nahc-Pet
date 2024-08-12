package kr.ac.dankook.cultureApplication.controller;

import kr.ac.dankook.cultureApplication.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/private")
@RequiredArgsConstructor
public class PrivateController {

    private final PostService postService;
    private final FavoriteService favoriteService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final PlaceReviewService placeReviewService;

    @RequestMapping("/me/{type}")
    public String myInformationPage(Model model, @PathVariable String type){
        if (type.equals("favorite")){
            model.addAttribute("koType","즐겨찾기");
        }else if (type.equals("post")){
            model.addAttribute("koType","게시물 및 리뷰");
        }else{
            model.addAttribute("koType","회원정보");
        }
        model.addAttribute("member",memberService.getLoginMemberProcess());
        model.addAttribute("type",type);
        model.addAttribute("favorite", favoriteService.getAllFavoriteProcess(
                memberService.getLoginMemberProcess()
        ));
        model.addAttribute("post", postService.findByUserIdProcess(
                memberService.getLoginMemberProcess().getId()
        ));
        model.addAttribute("review", placeReviewService.getReviewsByMemberId(
                memberService.getLoginMemberProcess().getId()
        ));
        return "page/myInformation.html";
    }
}
