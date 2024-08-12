package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.common.MemberUtil;
import kr.ac.dankook.cultureApplication.entity.Member;
import kr.ac.dankook.cultureApplication.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getLoginMemberProcess(){
        String loginMemberEmail = MemberUtil.getCurrentUserEmail();
        Optional<Member> loginMember = memberRepository.findByEmail(loginMemberEmail);
        return loginMember.orElse(null);
    }

    @Transactional
    public void deleteMemberProcess(Member member){
        memberRepository.deleteById(member.getId());
    }

    @Transactional
    public boolean editMemberNameProcess(String newName){
        Member targetMember = getLoginMemberProcess();
        targetMember.setName(newName);
        memberRepository.save(targetMember);
        return true;
    }
    @Transactional
    public boolean editMemberEmailProcess(String newEmail){
        Member targetMember = getLoginMemberProcess();
        targetMember.setEmail(newEmail);
        memberRepository.save(targetMember);
        return true;
    }

}
