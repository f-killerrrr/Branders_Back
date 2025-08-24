package likelion.branders.Repository;

import likelion.branders.Entity.CrawledDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrawledDataRepository extends JpaRepository<CrawledDataEntity, Long> {
    boolean existsByTitle(String title);
    void deleteByTitle(String title);

    @Query(value = "SELECT * FROM crawled_data " +
            "WHERE MATCH(title, period, policyId) AGAINST(:keyword IN BOOLEAN MODE)",
            nativeQuery = true)
    List<CrawledDataEntity> searchFullText(@Param("keyword") String keyword);
}