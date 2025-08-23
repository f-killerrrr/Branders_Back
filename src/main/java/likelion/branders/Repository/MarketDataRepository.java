package likelion.branders.Repository;

import likelion.branders.DTO.MarketDataDTO;
import likelion.branders.DTO.SigunguSimpleDetail;
import likelion.branders.Entity.MarketDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarketDataRepository extends JpaRepository<MarketDataEntity, Long> {
    @Query("SELECT m FROM MarketDataEntity m WHERE " +
            "(:keyword IS NULL OR m.category LIKE %:keyword%) AND " +
            "(:sido IS NULL OR m.sido = :sido) AND " +
            "(:sigungu IS NULL OR m.sigungu = :sigungu) AND " +
            "(:dong IS NULL OR m.dong = :dong)")
    List<MarketDataEntity> findByCriteria(
            @Param("keyword") String keyword,
            @Param("sido") String sido,
            @Param("sigungu") String sigungu,
            @Param("dong") String dong
    );

    // 대구광역시 전체의 특정 키워드 업체 수 카운트 (새로 추가)
    @Query("SELECT COUNT(m) FROM MarketDataEntity m WHERE m.sido = '대구광역시' AND m.category LIKE %:keyword%")
    long countBySidoAndCategory(@Param("keyword") String keyword);


    // 1. 구별 업체 수 카운트 (단순)
    @Query("SELECT new likelion.branders.DTO.SigunguSimpleDetail(m.sigungu, COUNT(m)) " +
            "FROM MarketDataEntity m " +
            "WHERE m.sido = '대구광역시' AND m.category LIKE %:keyword% " +
            "GROUP BY m.sigungu")
    List<SigunguSimpleDetail> countBySigunguAndCategory(@Param("keyword") String keyword);

    // 2. 구-동별 업체 수 카운트 (상세)
    @Query("SELECT m.sigungu, m.dong, COUNT(m) " +
            "FROM MarketDataEntity m " +
            "WHERE m.sido = '대구광역시' AND m.category LIKE %:keyword% " +
            "GROUP BY m.sigungu, m.dong")
    List<Object[]> findSigunguDongBreakdown(@Param("keyword") String keyword);
}