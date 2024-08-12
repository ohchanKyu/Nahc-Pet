package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.dto.response.MailResponse;
import kr.ac.dankook.cultureApplication.entity.Member;
import kr.ac.dankook.cultureApplication.exception.ErrorCode;
import kr.ac.dankook.cultureApplication.exception.MailException;
import kr.ac.dankook.cultureApplication.exception.UserNotFoundException;
import kr.ac.dankook.cultureApplication.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSender mailSender;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, String> verificationCodes = new HashMap<>();

    public MailResponse createMailResponse(String emailAddress){
        String verificationCode = generateVerificationCode();
        saveVerificationCode(emailAddress, verificationCode);
        return MailResponse.builder()
                .mailAddress(emailAddress)
                .title("NaHC 인증번호 안내 이메일 입니다.")
                .content("안녕하세요. NaHC 인증번호 안내 관련 이메일 입니다. 회원님의 인증 번호는 " + verificationCode + " 입니다. 인증 후에 비밀번호를 변경을 해주세요.")
                .build();
    }
    public void sendMail(MailResponse mailResponse){
        SimpleMailMessage message = new SimpleMailMessage();
        try{
            message.setTo(mailResponse.getMailAddress());
            message.setSubject(mailResponse.getTitle());
            message.setText(mailResponse.getContent());
            message.setFrom("ogyuchan01@gmail.com");
            message.setReplyTo("ogyuchan01@gmail.com");
            log.info("Sending email to: {}", mailResponse.getMailAddress());
            mailSender.send(message);
        }catch(Exception ex){
            throw new MailException(ErrorCode.SEND_EMAIL_ERROR);
        }
    }
    @Transactional
    public void changePasswordProcess(String email, String newPassword){
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION));
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        verificationCodes.remove(email);
    }
    public boolean isVerifyCodeProcess(String email,String verifyCode){
        String storedCode = verificationCodes.get(email);
        return storedCode != null && storedCode.equals(verifyCode);
    }
    private String generateVerificationCode(){
        return RandomStringUtils.randomAlphanumeric(10);
    }
    private void saveVerificationCode(String emailAddress, String verificationCode) {
        verificationCodes.put(emailAddress, verificationCode);
    }

}
