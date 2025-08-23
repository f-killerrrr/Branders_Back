package likelion.branders.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import likelion.branders.DTO.MarketDataDTO;
import likelion.branders.Service.MarketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/marketdata")
@RequiredArgsConstructor
@Validated
@Slf4j
public class MarketDataController {

    private final MarketDataService marketDataService;

    @GetMapping("/search")
    public ResponseEntity<MarketDataDTO.SearchResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sido,
            @RequestParam(required = false) String sigungu,
            @RequestParam(required = false) String dong) {

        MarketDataDTO.SearchRequest request = new MarketDataDTO.SearchRequest();
        request.setKeyword(keyword);
        request.setSido(sido);
        request.setSigungu(sigungu);
        request.setDong(dong);

        MarketDataDTO.SearchResponse response = marketDataService.searchMarketData(request);
        return ResponseEntity.ok(response);
    }

    // 백분율 계산 API (새로 추가)
    @GetMapping("/percentage")
    public ResponseEntity<MarketDataDTO.PercentageResponse> getPercentage(
            @RequestParam(required = true) String keyword,
            @RequestParam(required = true) String sido,
            @RequestParam(required = true) String sigungu,
            @RequestParam(required = true) String dong) {

        MarketDataDTO.SearchRequest request = new MarketDataDTO.SearchRequest();
        request.setKeyword(keyword);
        request.setSido(sido);
        request.setSigungu(sigungu);
        request.setDong(dong);

        // 요청 매개변수가 하나라도 null이거나 비어있으면 오류를 반환
        if (request.getKeyword() == null || request.getSido() == null ||
                request.getSigungu() == null || request.getDong() == null ||
                request.getKeyword().isEmpty() || request.getSido().isEmpty() ||
                request.getSigungu().isEmpty() || request.getDong().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        MarketDataDTO.PercentageResponse response = marketDataService.calculatePercentage(request);
        return ResponseEntity.ok(response);
    }

    // 구별 업체 수 조회 API (새로 추가)
    @GetMapping("/breakdown")
    public ResponseEntity<MarketDataDTO.SigunguCountResponse> getSigunguBreakdown(
            @RequestParam(required = true) String keyword) {

        MarketDataDTO.SigunguCountResponse response = marketDataService.getSigunguBreakdown(keyword);
        return ResponseEntity.ok(response);
    }
}