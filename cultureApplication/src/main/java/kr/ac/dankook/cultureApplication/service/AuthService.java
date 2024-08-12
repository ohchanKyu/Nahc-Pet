package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.common.MemberRole;
import kr.ac.dankook.cultureApplication.entity.Member;
import kr.ac.dankook.cultureApplication.exception.DuplicatedEmailException;
import kr.ac.dankook.cultureApplication.exception.ErrorCode;
import kr.ac.dankook.cultureApplication.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${admin.email}")
    private String adminEmail;

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member createNewMemberProcess(Member newMember){
        if (memberRepository.existsByEmail(newMember.getEmail())){
            throw new DuplicatedEmailException(ErrorCode.DUPLICATED_EMAIL);
        }

        String encodePassword = passwordEncoder.encode(newMember.getPassword());
        String role = newMember.getEmail().equals(adminEmail) ? "ADMIN" : "USER";
        log.info("New Member Sign up - {} / Role - {}", newMember.getEmail(),role);

        return memberRepository.save(
                Member.builder()
                        .name(newMember.getName())
                        .email(newMember.getEmail())
                        .password(encodePassword)
                        .role(MemberRole.valueOf(role))
                        .build()
        );
    }
    @Transactional(readOnly = true)
    public boolean existsByEmailProcess(String email){
        return memberRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<String> findEmailProcess(String name){
        List<Member> targetMembers = memberRepository.findByName(name);
        List<String> emailList = new ArrayList<>();
        for (Member member : targetMembers){
            emailList.add(member.getEmail());
        }
        return emailList;
    }
}
