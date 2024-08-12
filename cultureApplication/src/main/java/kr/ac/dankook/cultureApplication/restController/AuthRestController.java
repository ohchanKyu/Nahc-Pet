package kr.ac.dankook.cultureApplication.restController;

import kr.ac.dankook.cultureApplication.dto.request.PasswordChangeRequest;
import kr.ac.dankook.cultureApplication.dto.request.VerifyCodeRequest;
import kr.ac.dankook.cultureApplication.dto.response.MailResponse;
import kr.ac.dankook.cultureApplication.entity.Member;
import kr.ac.dankook.cultureApplication.service.AuthService;
import kr.ac.dankook.cultureApplication.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;
    private final MailService mailService;


    @PostMapping("/member/signup")
    public ResponseEntity<Member> signupMember(@RequestBody Member member){
        return ResponseEntity.ok(authService.createNewMemberProcess(member));
    }
    @GetMapping("/{name}/getEmail")
    public ResponseEntity<List<String>> getEmailByName(@PathVariable String name){
        return ResponseEntity.ok(authService.findEmailProcess(name));
    }
    @GetMapping("/{email}/isExist")
    public ResponseEntity<Boolean> isExistEmail(@PathVariable String email){
        return ResponseEntity.ok(authService.existsByEmailProcess(email));
    }
    @PostMapping("/{email}/verify-code")
    public ResponseEntity<Boolean> sendVerifyCode(@PathVariable String email) {
        MailResponse mailResponse = mailService.createMailResponse(email);
        mailService.sendMail(mailResponse);
        return ResponseEntity.ok(true);
    }
    @PostMapping("/isVerify")
    public ResponseEntity<Boolean> isVerifyCode(@RequestBody VerifyCodeRequest verifyCodeRequest) {
        return ResponseEntity.ok(mailService.isVerifyCodeProcess(
                verifyCodeRequest.getEmail(),
                verifyCodeRequest.getVerifyCode()
        ));
    }
    @PatchMapping("/password")
    public ResponseEntity<Boolean> changeMemberPassword(@RequestBody PasswordChangeRequest passwordChangeRequest){
        mailService.changePasswordProcess(
                passwordChangeRequest.getEmail(),
                passwordChangeRequest.getNewPassword()
        );
        return ResponseEntity.ok(true);
    }
}