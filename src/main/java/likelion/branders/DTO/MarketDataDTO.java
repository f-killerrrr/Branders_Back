package likelion.branders.DTO;

import lombok.*;

import java.util.List;
public class MarketDataDTO {
    // 검색 요청 DTO
    @Getter
    @Setter
    public static class SearchRequest {
        private String keyword;
        private String sido;
        private String sigungu;
        private String dong;
    }

    // 검색 결과 DTO
    @Getter
    @Builder
    public static class SearchResponse {
        private long totalCount;
        private List<MarketDataDetail> data;

        @Getter
        @Builder
        public static class MarketDataDetail {
            private String category;
            private String sido;
            private String sigungu;
            private String dong;
            private String roadAddress;
            private Double longitude;
            private Double latitude;
        }
    }

    // 백분율 계산 결과 DTO (새로 추가)
    @Getter
    @Builder
    public static class PercentageResponse {
        private String keyword;
        private String sido;
        private String sigungu;
        private String dong;
        private long totalCountInCity;
        private long countInDistrict;
        private double percentage;
    }

    // A. 구별 업체 수 응답 DTO (단순 카운트)
    @Getter
    @Builder
    public static class SigunguCountResponse {
        private String keyword;
        private long totalCountInCity;
        private List<SigunguSimpleDetail> data;
    }


    // B. 구-동별 상세 업체 수 응답 DTO (상세 카운트)
    @Getter
    @Builder
    public static class SigunguBreakdownResponse {
        private String keyword;
        private long totalCountInCity;
        private List<SigunguDetail> breakdown;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SigunguDetail {
        private String sigungu;
        private long totalCountInSigungu;
        private List<DongDetail> dongs;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DongDetail {
        private String dong;
        private long count;
    }

}