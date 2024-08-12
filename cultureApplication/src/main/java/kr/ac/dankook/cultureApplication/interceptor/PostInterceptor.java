package kr.ac.dankook.cultureApplication.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ac.dankook.cultureApplication.dto.response.AllPageResponse;
import kr.ac.dankook.cultureApplication.dto.response.PageSectionResponse;
import kr.ac.dankook.cultureApplication.dto.response.PostResponse;
import kr.ac.dankook.cultureApplication.service.MemberService;
import kr.ac.dankook.cultureApplication.service.PostService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostInterceptor implements HandlerInterceptor {

    private final PostService postService;
    private final MemberService memberService;

    private PageSectionResponse getPageSection(int currentPage, int endPage){

        int currentSection;
        int endSection;
        if (endPage % 5 != 0){
            endSection = endPage / 5 + 1;
        }else{
            endSection = endPage / 5;
        }
        if (currentPage % 5 != 0){
            currentSection = currentPage / 5 + 1;
        }else{
            currentSection = currentPage / 5;
        }
        return new PageSectionResponse(currentSection,endSection);
    }
    private AllPageResponse getFetchPostsByCurrentPage(List<PostResponse> allPostLists, int start){

        AllPageResponse allPageResponse = new AllPageResponse();
        PageSectionResponse pageSectionResponse;

        int totalPostLength = allPostLists.size();
        int totalPageLength = totalPostLength / 10;

        int end;

        if (totalPostLength % 10 != 0){
            totalPageLength++;
        }
        List<PostResponse> currentPostsList = new ArrayList<>();
        if (start == 0) {
            for(int i=0;i<10;i++){
                if (i >= totalPostLength){
                    break;
                }
                currentPostsList.add(allPostLists.get(i));
            }
            start = 1;
        }else{
            int startPoint = (start - 1) * 10;
            end = start * 10;

            for (int i=startPoint;i<end;i++){
                if (i >= totalPostLength){
                    break;
                }
                currentPostsList.add(allPostLists.get(i));
            }
        }
        pageSectionResponse = getPageSection(start,totalPageLength);
        allPageResponse.setCurrentSection(pageSectionResponse.getCurrentSection());
        allPageResponse.setEndSection(pageSectionResponse.getEndSection());
        allPageResponse.setPosts(currentPostsList);
        allPageResponse.setCurrentPage(start);
        allPageResponse.setTotalPage(totalPageLength);
        allPageResponse.setTotalPost(totalPostLength);
        return allPageResponse;
    }

    @Override
    public void postHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler, @Nullable ModelAndView modelAndView) {

        assert modelAndView != null;
        ModelMap modelMap = modelAndView.getModelMap();
        AllPageResponse allPageResponse;

        List<PostResponse> allPostLists;

        String type = (String) modelMap.get("type");
        int start = (int) modelMap.get("start");

        if (type.equals("me")){
            Long loginMemberId = memberService.getLoginMemberProcess().getId();
            allPostLists = postService.findByUserIdProcess(loginMemberId);
        }else if (type.equals("keyword")){
            String keyword = request.getParameter("keyword");
            allPostLists = postService.findAllPostsProcess(type,keyword);
        }
        else{
            allPostLists = postService.findAllPostsProcess(type,"None");
        }
        allPageResponse = getFetchPostsByCurrentPage(allPostLists,start);
        allPageResponse.setType(type);

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        modelAndView.addObject("result",allPageResponse);
    }
}
