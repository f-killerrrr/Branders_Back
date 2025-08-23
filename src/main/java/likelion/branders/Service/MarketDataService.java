package likelion.branders.Service;

import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import likelion.branders.DTO.MarketDataDTO;
import likelion.branders.DTO.SigunguSimpleDetail;
import likelion.branders.DTO.TopCategory; // 추가
import likelion.branders.Entity.MarketDataEntity;
import likelion.branders.Repository.MarketDataRepository;
import org.springframework.data.domain.PageRequest; // 추가
import org.springframework.data.domain.Pageable; // 추가
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    // A. 구별 업체 수 조회 로직 (단순)
    @Transactional(readOnly = true)
    public MarketDataDTO.SigunguCountResponse getSigunguSimpleBreakdown(String keyword) {
        log.info("Getting simple sigungu breakdown for keyword: {}", keyword);

        long totalCountInCity = marketDataRepository.countBySidoAndCategory(keyword);
        // 여기서 MarketDataDTO.SigunguSimpleDetail -> SigunguSimpleDetail로 수정
        List<SigunguSimpleDetail> sigunguCounts = marketDataRepository.countBySigunguAndCategory(keyword);

        return MarketDataDTO.SigunguCountResponse.builder()
                .keyword(keyword)
                .totalCountInCity(totalCountInCity)
                .data(sigunguCounts)
                .build();
    }

    // B. 구-동별 상세 업체 수 조회 로직 (상세)
    @Transactional(readOnly = true)
    public MarketDataDTO.SigunguBreakdownResponse getSigunguDetailedBreakdown(String keyword) {
        log.info("Getting detailed sigungu breakdown for keyword: {}", keyword);

        long totalCountInCity = marketDataRepository.countBySidoAndCategory(keyword);
        List<Object[]> rawData = marketDataRepository.findSigunguDongBreakdown(keyword);

        Map<String, List<MarketDataDTO.DongDetail>> dongsBySigungu = rawData.stream()
                .collect(Collectors.groupingBy(
                        data -> (String) data[0],
                        Collectors.mapping(
                                data -> MarketDataDTO.DongDetail.builder()
                                        .dong((String) data[1])
                                        .count((Long) data[2])
                                        .build(),
                                Collectors.toList()
                        )
                ));

        List<MarketDataDTO.SigunguDetail> sigunguBreakdown = dongsBySigungu.entrySet().stream()
                .map(entry -> {
                    String sigungu = entry.getKey();
                    List<MarketDataDTO.DongDetail> dongs = entry.getValue();
                    long totalCountInSigungu = dongs.stream().mapToLong(MarketDataDTO.DongDetail::getCount).sum();

                    return MarketDataDTO.SigunguDetail.builder()
                            .sigungu(sigungu)
                            .totalCountInSigungu(totalCountInSigungu)
                            .dongs(dongs)
                            .build();
                })
                .collect(Collectors.toList());

        return MarketDataDTO.SigunguBreakdownResponse.builder()
                .keyword(keyword)
                .totalCountInCity(totalCountInCity)
                .breakdown(sigunguBreakdown)
                .build();
    }

    // Top 10 업종 조회 로직 (새로 추가) 이름만 top 5
    @Transactional(readOnly = true)
    public MarketDataDTO.TopFiveResponse getTop5Categories(String sigungu) {
        log.info("Getting top 5 categories for sigungu: {}", sigungu);

        Pageable pageable = PageRequest.of(0, 10); // 0페이지부터 5개만 가져오도록 설정
        List<TopCategory> topCategories = marketDataRepository.findTop5CategoriesBySigungu(sigungu, pageable);

        return MarketDataDTO.TopFiveResponse.builder()
                .sigungu(sigungu)
                .topCategories(topCategories)
                .build();
    }
}
