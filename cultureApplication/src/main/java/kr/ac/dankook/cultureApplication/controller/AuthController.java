package kr.ac.dankook.cultureApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @RequestMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false, defaultValue = "false") boolean error,
                            Model model){
        if (error) {
            model.addAttribute("errorMessage", "로그인하신 회원정보가 없습니다.");
        }
        return "page/auth/login.html";
    }

    @RequestMapping("/signup")
    public String signupPage(){
        return "page/auth/signup.html";
    }
    @RequestMapping("/findEmail")
    public String findEmailPage(){
        return "page/auth/findEmail.html";
    }
    @RequestMapping("/findPassword")
    public String findPasswordPage(){
        return "page/auth/findPassword.html";
    }
}
