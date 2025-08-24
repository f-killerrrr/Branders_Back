package likelion.branders.Controller;

import likelion.branders.DTO.CrawledDataDTO;
import likelion.branders.Entity.CrawledDataEntity;
import likelion.branders.Service.CrawlingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crawl")
public class CrawlingController {

    private final CrawlingService crawlingService;

    public CrawlingController(CrawlingService crawlingService) {
        this.crawlingService = crawlingService;
    }

    // 크롤링 실행 및 저장
    @PostMapping("/run")
    public ResponseEntity<List<CrawledDataDTO>> runCrawling() {
        List<CrawledDataDTO> result = crawlingService.crawlAndSave();
        return ResponseEntity.ok(result);
    }

    // 전체 데이터 조회
    @GetMapping("/all")
    public ResponseEntity<List<CrawledDataDTO>> getAllData() {
        return ResponseEntity.ok(crawlingService.getAllData());
    }

    // 검색 기능
    @GetMapping("/search")
    public ResponseEntity<List<CrawledDataEntity>> searchCrawledDTO(@RequestParam String keyword) {
        return ResponseEntity.ok(crawlingService.search(keyword));
    }

    // 특정 제목으로 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteByTitle(@RequestParam String title) {
        try {
            crawlingService.deleteByTitle(title);
            return ResponseEntity.ok("삭제 완료: " + title);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 전체 데이터 삭제
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllData() {
        crawlingService.deleteAllData();
        return ResponseEntity.ok("전체 데이터 삭제 완료");
    }
}