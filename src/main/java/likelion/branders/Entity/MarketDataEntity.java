package likelion.branders.Entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "market_data",
        indexes = {
                @Index(name = "idx_category", columnList = "category"),
                @Index(name = "idx_sido", columnList = "sido"),
                @Index(name = "idx_sigungu", columnList = "sigungu"),
                @Index(name = "idx_dong", columnList = "dong")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MarketDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long marketDataId;

    @Column(length = 100)
    private String category;      // 상권업종소분류명

    @Column(length = 50)
    private String sido;          // 시도명

    @Column(length = 50)
    private String sigungu;       // 시군구명

    @Column(length = 50)
    private String dong;          // 행정동명  (※ 법정동 사용하려면 로더에서 바꾸면 됨)

    @Column(length = 200)
    private String roadAddress;   // 도로명주소

    @Column
    private Double longitude;// 경도

    @Column
    private Double latitude;      // 위도
}
