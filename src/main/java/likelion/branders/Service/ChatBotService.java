package likelion.branders.Service;

import jakarta.transaction.Transactional;
import likelion.branders.DTO.ChatMessageDTO;
import likelion.branders.DTO.ChatSessionDTO;
import likelion.branders.Entity.ChatMessageEntity;
import likelion.branders.Entity.ChatSessionEntity;
import likelion.branders.Entity.UserEntity;
import likelion.branders.Enum.SenderType;
import likelion.branders.Repository.ChatMessageRepository;
import likelion.branders.Repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ChatBotService {

    private final ChatSessionRepository sessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final OpenAiService openAiService;

    // 새 세션 생성 + 첫 메시지 전송
    @Transactional
    public ChatSessionDTO createSessionAndSendMessage(Long userId, String sessionTitle, String message) {
        ChatSessionEntity session = ChatSessionEntity.builder()
                .sessionTitle(sessionTitle)
                .user(new UserEntity(userId))
                .messages(new ArrayList<>())
                .build();

        // 사용자 메시지 저장
        ChatMessageEntity userMsg = ChatMessageEntity.builder()
                .message(message)
                .senderType(SenderType.USER)
                .session(session)
                .build();
        chatMessageRepository.save(userMsg);
        session.getMessages().add(userMsg);

        // AI 메시지 생성 및 저장
        String reply = openAiService.ask(message);
        ChatMessageEntity botMsg = ChatMessageEntity.builder()
                .message(reply)
                .senderType(SenderType.AI)
                .session(session)
                .build();
        chatMessageRepository.save(botMsg);  //
        session.getMessages().add(botMsg);

        // 세션 저장
        sessionRepository.save(session);

        return session.toDTO();
    }

    // 기존 세션에 메시지 전송 + GPT 답변 저장
    @Transactional
    public ChatMessageDTO sendMessage(Long sessionId, String message) {
        ChatSessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션이 존재하지 않습니다."));

        // 사용자 메시지 저장
        ChatMessageEntity userMsg = ChatMessageEntity.builder()
                .message(message)
                .senderType(SenderType.USER)
                .session(session)
                .build();
        chatMessageRepository.save(userMsg);
        session.getMessages().add(userMsg);

        // AI 메시지 생성 및 저장
        String reply = openAiService.ask(message);
        ChatMessageEntity botMsg = ChatMessageEntity.builder()
                .message(reply)
                .senderType(SenderType.AI)
                .session(session)
                .build();
        chatMessageRepository.save(botMsg);
        session.getMessages().add(botMsg);

        return botMsg.toDTO();  // 이제 messageId 포함
    }

    public List<ChatSessionDTO> getUserSessions(Long userId) {
        return sessionRepository.findByUserUserId(userId)
                .stream()
                .map(ChatSessionEntity::toDTO)
                .toList();
    }

    public List<ChatMessageDTO> getSessionMessages(Long sessionId) {
        return chatMessageRepository.findBySessionSessionId(sessionId)
                .stream()
                .map(ChatMessageEntity::toDTO)
                .toList();
    }

    // 세션 삭제
    @Transactional
    public void deleteSession(Long sessionId) {
        ChatSessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 세션이 존재하지 않습니다."));
        sessionRepository.delete(session);
    }
}
