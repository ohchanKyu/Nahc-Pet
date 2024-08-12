package kr.ac.dankook.cultureApplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VerifyCodeRequest {
    private String email;
    private String verifyCode;
}
