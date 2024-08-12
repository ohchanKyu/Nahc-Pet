package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String,String> redisTemplate;
    private final MemberService memberService;

    // 만료기간 하루
    // 비로그인일 경우 IP에 따라
    // 로그인일 경우 해당 포스트의 만료 시간에 따라
    // 중복되면 true
    public boolean fetchCountByRedisCacheProcess(Long postId, String ipAddress) {
        Member loginMember = memberService.getLoginMemberProcess();
        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        String key = createKey(postId, ipAddress, loginMember);
        if (key == null) {
            return false;
        }
        if (operations.get(key) != null) {
            return true;
        } else {
            operations.set(key, "true", 24, TimeUnit.HOURS);
            return false;
        }
    }

    private String createKey(Long postId, String ipAddress, Member loginMember) {
        if (loginMember != null) {
            return postId + "_" + loginMember.getEmail();
        } else if (ipAddress != null && !ipAddress.isEmpty()) {
            return postId + "_" + ipAddress;
        } else {
            return null;
        }
    }
}
