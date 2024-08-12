package kr.ac.dankook.cultureApplication.controller;

import kr.ac.dankook.cultureApplication.service.FileService;
import kr.ac.dankook.cultureApplication.service.MemberService;
import kr.ac.dankook.cultureApplication.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/private/board")
@RequiredArgsConstructor
public class PrivateBoardController {

    private final PostService postService;
    private final MemberService memberService;
    private final FileService fileService;

    @RequestMapping("/write")
    public String boardWritePage(){
        return "page/board/boardWrite.html";
    }

    @RequestMapping("/edit/{postId}")
    public String boardEditPage(@PathVariable Long postId, Model model) {
        model.addAttribute("result", postService.getFetchOnePost(postId));
        return "page/board/boardEdit.html";
    }
    @RequestMapping("/me")
    public String myBoardPage(
          @RequestParam( name="start", required = false,defaultValue ="0") int start,
          Model model){
        model.addAttribute("userId", memberService.getLoginMemberProcess().getId());
        model.addAttribute("start",start);
        model.addAttribute("type", "me");
        return "page/board/boardMe.html";
    }
    @PostMapping("/upload")
    public ModelAndView uploadCKImage(MultipartHttpServletRequest request){
        ModelAndView mav = new ModelAndView("jsonView");

        String uploadPath = fileService.ckFileTempUpload(request);
        mav.addObject("uploaded",true);
        mav.addObject("url",uploadPath);
        return mav;
    }
}
