package kr.ac.dankook.cultureApplication.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MailResponse {
    private String mailAddress;
    private String title;
    private String content;
}
