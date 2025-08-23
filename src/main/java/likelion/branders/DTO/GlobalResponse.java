package likelion.branders.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class GlobalResponse <T>{
    private String code;      // 상태 코드, 예: "200"
    private String message;   // 메시지, 예: "성공"
    private T data;           // 실제 데이터

    // 생성자
    public GlobalResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 편리한 정적 메서드
    public static <T> GlobalResponse<T> of(String code, String message, T data) {
        return new GlobalResponse<>(code, message, data);
    }

    public static <T> GlobalResponse<T> of(String code, String message) {
        return new GlobalResponse<>(code, message, null);
    }
}
