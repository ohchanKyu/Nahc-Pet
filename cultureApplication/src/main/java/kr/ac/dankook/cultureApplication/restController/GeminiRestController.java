package kr.ac.dankook.cultureApplication.restController;

import kr.ac.dankook.cultureApplication.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
public class GeminiRestController {

    private final GeminiService geminiService;

    @PostMapping("/question")
    public ResponseEntity<?> getAIResponse(@RequestParam("question") String question) {
        try {
            return ResponseEntity.ok().body(geminiService.getContents(question));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}