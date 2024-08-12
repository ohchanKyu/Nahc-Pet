package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.common.MemberDetail;
import kr.ac.dankook.cultureApplication.entity.Member;
import kr.ac.dankook.cultureApplication.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        Optional<Member> principal = memberRepository.findByEmail(email);
        if (principal.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        return new MemberDetail(principal.get());
    }

}
