package likelion.branders.Service;

import likelion.branders.Config.OpenAiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class OpenAiService {

    private final OpenAiConfig config;

    public String ask(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();

        //AI 창업컨설팅 모드 설정
        List<Map<String, String>> messages = List.of(
                Map.of(
                        "role", "system",
                        "content", "당신은 스타트업 창업 컨설턴트입니다. 사용자가 창업 관련 질문을 하면 전문가답게 조언을 해주세요. 시장조사, 사업 아이디어, 정부지원금, 마케팅, 재무, 팀빌딩 등 창업 관련 주제를 포함하고, 친절하고 구체적으로 단계별 실행 방법까지 제안하세요."
                ),
                Map.of(
                        "role", "user",
                        "content", userMessage
                )
        );

        Map<String, Object> body = new HashMap<>();
        body.put("model", config.getModel());
        body.put("messages", messages);
        body.put("max_tokens", 2000);
        body.put("temperature", 0.6);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + config.getApiKey());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                config.getApiUrl(),
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null) {
                    String content = (String) message.get("content");
                    return content; // 전체 반환
                }
            }
        }
         return "답변을 가져오지 못했습니다.";
    }
}