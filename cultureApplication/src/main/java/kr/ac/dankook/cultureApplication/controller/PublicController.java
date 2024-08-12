package kr.ac.dankook.cultureApplication.controller;

import kr.ac.dankook.cultureApplication.dto.response.BlogResponse;
import kr.ac.dankook.cultureApplication.repository.PetShelterRepository;
import kr.ac.dankook.cultureApplication.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final PetShelterRepository petShelterRepository;
    private final LocationService locationService;

    @RequestMapping("/main")
    public String mainPage(){
        return "page/main.html";
    }

    @RequestMapping("/except")
    public String exceptPage(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", "접근 권한이 없습니다.");
        return "redirect:/public/main";
    }

    @RequestMapping("/petInformation/cafe")
    public String petInformationPage(){
        return "page/petInformation/petCafe.html";
    }
    @RequestMapping("/petInformation/education")
    public String petEducationPage(Model model){
        List<BlogResponse> allBlogs = new ArrayList<>();

        List<BlogResponse> firstList = locationService.getPetBlogProcess("애견 훈련 방법");
        List<BlogResponse> secondList = locationService.getPetBlogProcess("애견 훈련 관련 유용한 팁");
        List<BlogResponse> thirdList = locationService.getPetBlogProcess("애견 문제 행동 교정 방법");

        allBlogs.addAll(firstList);
        allBlogs.addAll(secondList);
        allBlogs.addAll(thirdList);
        removeDuplicateTitles(allBlogs);

        model.addAttribute("blogs",allBlogs);
        return "page/petInformation/petEducation.html";
    }
    @RequestMapping("/petInformation/shelter")
    public String petShelterPage(Model model){
        model.addAttribute("petShelter", petShelterRepository.findAll());
        return "page/petInformation/petShelter.html";
    }

    private void removeDuplicateTitles(List<BlogResponse> allBlogs){
        Set<String> seenTitles = new HashSet<>();
        Iterator<BlogResponse> iterator = allBlogs.iterator();

        while (iterator.hasNext()) {
            BlogResponse blog = iterator.next();
            if (seenTitles.contains(blog.getTitle())) {
                iterator.remove();
            } else {
                seenTitles.add(blog.getTitle());
            }
        }
    }
}
