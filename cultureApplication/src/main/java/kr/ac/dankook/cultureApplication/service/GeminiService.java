package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.dto.request.ChatRequest;
import kr.ac.dankook.cultureApplication.dto.response.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class GeminiService {

    private final RestTemplate restTemplate;
    public GeminiService(@Qualifier("geminiRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Value("${api.gemini.url}")
    private String apiUrl;

    @Value("${api.gemini.app-key}")
    private String geminiApiKey;

    public String getContents(String prompt) {

        String requestUrl = apiUrl + "?key=" + geminiApiKey;
        ChatRequest request = new ChatRequest(prompt);
        ChatResponse response = restTemplate.postForObject(requestUrl, request, ChatResponse.class);
        return Objects.requireNonNull(response).getCandidates().getFirst().getContent().getParts().getFirst().getText();
    }
}
