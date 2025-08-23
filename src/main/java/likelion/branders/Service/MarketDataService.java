package likelion.branders.Service;

import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import likelion.branders.DAO.MarketDataDAO;
import likelion.branders.DTO.MarketDataDTO;
import likelion.branders.DTO.SigunguCountDetail;
import likelion.branders.Entity.MarketDataEntity;
import likelion.branders.Repository.MarketDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook; // 추가
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketDataService {
    private final MarketDataRepository marketDataRepository;

    @PostConstruct
    @Transactional
    public void loadCsvData() {
        if (marketDataRepository.count() > 0) {
            log.info("Database already contains data. Skipping CSV load.");
            return;
        }

        log.info("Starting to load CSV data...");
        List<MarketDataEntity> marketDataList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(
                new InputStreamReader(
                        new ClassPathResource("data/대구상권정보.csv").getInputStream(), "UTF-8"))) {
            String[] nextLine;
            reader.readNext(); // 헤더 스킵
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length > 38) {
                    try {
                        marketDataList.add(MarketDataEntity.builder()
                                .category(nextLine[8])       // 9번째 열 (인덱스 8)
                                .sido(nextLine[12])          // 13번째 열 (인덱스 12)
                                .sigungu(nextLine[14])       // 15번째 열 (인덱스 14)
                                .dong(nextLine[16])          // 17번째 열 (인덱스 16)
                                .roadAddress(nextLine[31])   // 32번째 열 (인덱스 31)
                                .longitude(Double.parseDouble(nextLine[37]))  // 38번째 열 (인덱스 37)
                                .latitude(Double.parseDouble(nextLine[38]))   // 39번째 열 (인덱스 38)
                                .build());
                    } catch (NumberFormatException e) {
                        log.warn("Skipping row due to number format error: {}", String.join(",", nextLine));
                    }
                }
            }
            marketDataRepository.saveAll(marketDataList);
            log.info("CSV data loaded successfully. Total records: {}", marketDataList.size());
        } catch (Exception e) {
            log.error("Failed to load CSV data", e);
        }
    }

    @Transactional(readOnly = true)
    public MarketDataDTO.SearchResponse searchMarketData(MarketDataDTO.SearchRequest request) {
        log.info("Searching with keyword: {}, sido: {}, sigungu: {}, dong: {}",
                request.getKeyword(), request.getSido(), request.getSigungu(), request.getDong());

        List<MarketDataEntity> results = marketDataRepository.findByCriteria(
                request.getKeyword(),
                request.getSido(),
                request.getSigungu(),
                request.getDong()
        );

        long count = results.size();
        List<MarketDataDTO.SearchResponse.MarketDataDetail> details = results.stream()
                .map(entity -> MarketDataDTO.SearchResponse.MarketDataDetail.builder()
                        .category(entity.getCategory())
                        .sido(entity.getSido())
                        .sigungu(entity.getSigungu())
                        .dong(entity.getDong())
                        .roadAddress(entity.getRoadAddress())
                        .longitude(entity.getLongitude())
                        .latitude(entity.getLatitude())
                        .build())
                .collect(Collectors.toList());

        log.info("Search completed. Found {} records.", count);
        return MarketDataDTO.SearchResponse.builder()
                .totalCount(count)
                .data(details)
                .build();
    }

    // 백분율 계산 로직 (새로 추가)
    @Transactional(readOnly = true)
    public MarketDataDTO.PercentageResponse calculatePercentage(MarketDataDTO.SearchRequest request) {
        log.info("Calculating percentage for keyword: {} in sido: {}, sigungu: {}, dong: {}",
                request.getKeyword(), request.getSido(), request.getSigungu(), request.getDong());

        // 1. 대구광역시 전체 업체 수 (총합)
        long totalCountInCity = marketDataRepository.countBySidoAndCategory(request.getKeyword());

        // 2. 특정 구/동의 업체 수 (부분)
        List<MarketDataEntity> resultsInDistrict = marketDataRepository.findByCriteria(
                request.getKeyword(),
                request.getSido(),
                request.getSigungu(),
                request.getDong()
        );
        long countInDistrict = resultsInDistrict.size();

        // 3. 백분율 계산
        double percentage = 0.0;
        if (totalCountInCity > 0) {
            percentage = (double) countInDistrict / totalCountInCity * 100.0;
        }

        log.info("Percentage calculated: totalCountInCity={}, countInDistrict={}, percentage={}%",
                totalCountInCity, countInDistrict, percentage);

        return MarketDataDTO.PercentageResponse.builder()
                .keyword(request.getKeyword())
                .sido(request.getSido())
                .sigungu(request.getSigungu())
                .dong(request.getDong())
                .totalCountInCity(totalCountInCity)
                .countInDistrict(countInDistrict)
                .percentage(percentage)
                .build();
    }

    // 구별 업체 수 조회 로직 (새로 추가)
    @Transactional(readOnly = true)
    public MarketDataDTO.SigunguCountResponse getSigunguBreakdown(String keyword) {
        log.info("Getting sigungu breakdown for keyword: {}", keyword);

        // 1. 대구광역시 전체 업체 수 카운트 (새로 추가)
        long totalCountInCity = marketDataRepository.countBySidoAndCategory(keyword);
        // 2. 구별 업체 수 조회
        List<SigunguCountDetail> sigunguCounts = marketDataRepository.countBySigunguAndCategory(keyword);

        log.info("Found {} sigungu entries.", sigunguCounts.size());
        // 3. 빌더에 전체 업체 수를 추가하여 반환
        return MarketDataDTO.SigunguCountResponse.builder()
                .keyword(keyword)
                .totalCountInCity(totalCountInCity) // 새로 추가
                .data(sigunguCounts)
                .build();
    }
}
