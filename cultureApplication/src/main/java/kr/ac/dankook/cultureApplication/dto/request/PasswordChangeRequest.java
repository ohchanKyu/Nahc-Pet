package kr.ac.dankook.cultureApplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PasswordChangeRequest {
    private String email;
    private String newPassword;
}
