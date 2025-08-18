package likelion.branders.Controller;

import likelion.branders.DTO.ChatMessageDTO;
import likelion.branders.DTO.ChatSessionDTO;
import likelion.branders.Enum.SenderType;
import likelion.branders.Service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor

public class ChatController {

    private final ChatBotService chatBotService;

    //새 세션 생성 + 첫 메시지 전송

    @PostMapping("/session")
    public ResponseEntity<ChatSessionDTO> createSession(
            @RequestParam Long userId,
            @RequestParam String title,
            @RequestParam String message) {
        return ResponseEntity.ok(chatBotService.createSessionAndSendMessage(userId, title, message));
    }

    //기존 세션에 메시지 전송 + GPT 답변 저장

    @PostMapping("/session/{sessionId}/send")
    public ResponseEntity<ChatMessageDTO> sendMessage(
            @PathVariable Long sessionId,
            @RequestParam String message) {
        return ResponseEntity.ok(chatBotService.sendMessage(sessionId, message));
    }

    //유저별 세션 목록 조회

    @GetMapping("/sessions/{userId}")
    public ResponseEntity<List<ChatSessionDTO>> getSessions(@PathVariable Long userId) {
        return ResponseEntity.ok(chatBotService.getUserSessions(userId));
    }

    //세션별 모든 메시지 조회
    @GetMapping("/session/{sessionId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Long sessionId) {
        return ResponseEntity.ok(chatBotService.getSessionMessages(sessionId));
    }
}
