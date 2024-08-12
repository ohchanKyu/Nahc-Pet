package kr.ac.dankook.cultureApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/private/pet")
public class PrivatePetController {

    @RequestMapping("/hospital")
    public String hospitalPage(){
        return "page/pet/petHospital.html";
    }
    @RequestMapping("/hotel")
    public String hotelPage(){
        return "page/pet/petHotel.html";
    }
    @GetMapping("/tour")
    public String tourPage(){
        return "page/pet/petTour.html";
    }
    @GetMapping("/care")
    public String carePage(){
        return "page/pet/petCare.html";
    }
}
