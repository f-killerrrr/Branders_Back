package likelion.branders.Service;

import likelion.branders.DTO.CrawledDataDTO;
import likelion.branders.Entity.CrawledDataEntity;
import likelion.branders.Repository.CrawledDataRepository;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlingService {

    private final CrawledDataRepository crawledDataRepository;

    @Value("${chrome.driver.path}")
    private String chromeDriverPath;

    public CrawlingService(CrawledDataRepository crawledDataRepository) {
        this.crawledDataRepository = crawledDataRepository;
    }

    public List<CrawledDataDTO> crawlAndSave() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        List<CrawledDataDTO> rawList = new ArrayList<>();

        try {
            driver.get("https://bigdata.sbiz.or.kr/#/sprtBiz");
            wait.until(wd -> ((JavascriptExecutor) wd)
                    .executeScript("return document.readyState").equals("complete"));

            while (true) {
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".gallery-loop > div")));
                List<WebElement> cards = driver.findElements(By.cssSelector(".gallery-loop > div"));

                for (int i = 0; i < cards.size(); i++) {
                    WebElement card = cards.get(i);
                    try {
                        String title    = extractByCss(card, ".reBoxCon h2",    "제목 없음");
                        String period   = extractByCss(card, ".reBoxBot > span", "기간 없음");
                        String policyId = extractByCss(card, ".reBoxBot > div",  "ID 없음");
                        rawList.add(new CrawledDataDTO(title, period, policyId));
                    } catch (StaleElementReferenceException e) {
                        System.out.println("StaleElementReferenceException 발생: 카드 " + i + " 건너뜀");
                    }
                }

                List<WebElement> nextBtns = driver.findElements(
                        By.xpath("//button[.//i[text()='keyboard_arrow_right']]")
                );
                if (nextBtns.isEmpty()) break;

                WebElement nextBtn = nextBtns.get(0);
                String ariaDisabled = nextBtn.getAttribute("aria-disabled");
                boolean isDisabled = "true".equals(ariaDisabled) || nextBtn.getAttribute("class").contains("q-btn--disabled");
                if (isDisabled) break;

                nextBtn.click();
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".gallery-loop > div")));
            }

        } catch (Exception e) {
            throw new RuntimeException("크롤링 중 오류 발생", e);
        } finally {
            driver.quit();
        }

        List<CrawledDataDTO> merged = mergeRawList(rawList);
        saveToDatabase(merged);
        return merged;
    }

    private String extractByCss(WebElement parent, String css, String defaultTxt) {
        try {
            return parent.findElement(By.cssSelector(css)).getText().trim();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return defaultTxt;
        }
    }

    private List<CrawledDataDTO> mergeRawList(List<CrawledDataDTO> rawList) {
        List<CrawledDataDTO> merged = new ArrayList<>();
        CrawledDataDTO current = null;

        for (CrawledDataDTO part : rawList) {
            boolean hasTitle    = !part.getTitle().equals("제목 없음");
            boolean hasPeriod   = !part.getPeriod().equals("기간 없음");
            boolean hasPolicyId = !part.getPolicyId().equals("ID 없음");

            if (hasTitle) {
                if (current != null) merged.add(current);
                current = new CrawledDataDTO(
                        part.getTitle(),
                        hasPeriod   ? part.getPeriod()   : "기간 없음",
                        hasPolicyId ? part.getPolicyId() : "ID 없음"
                );
            } else if (current != null) {
                if (hasPeriod)   current.setPeriod(part.getPeriod());
                if (hasPolicyId) current.setPolicyId(part.getPolicyId());
            }
        }
        if (current != null) merged.add(current);
        return merged;
    }

    private void saveToDatabase(List<CrawledDataDTO> merged) {
        for (CrawledDataDTO dto : merged) {
            if (!crawledDataRepository.existsByTitle(dto.getTitle())) {
                CrawledDataEntity entity = new CrawledDataEntity(
                        dto.getTitle(), dto.getPeriod(), dto.getPolicyId()
                );
                crawledDataRepository.save(entity);
            }
        }
    }

    public List<CrawledDataDTO> getAllData() {
        return crawledDataRepository.findAll().stream()
                .map(e -> new CrawledDataDTO(e.getTitle(), e.getPeriod(), e.getPolicyId()))
                .toList();
    }

    public void deleteAllData() {
        crawledDataRepository.deleteAll();
    }

    public void deleteByTitle(String title) {
        if (crawledDataRepository.existsByTitle(title)) {
            crawledDataRepository.deleteByTitle(title);
        } else {
            throw new IllegalArgumentException("해당 제목의 데이터가 존재하지 않습니다: " + title);
        }
    }
}