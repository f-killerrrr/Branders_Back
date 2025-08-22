package likelion.branders.Repository;

import likelion.branders.Entity.CrawledDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawledDataRepository extends JpaRepository<CrawledDataEntity, Long> {
    boolean existsByTitle(String title);
    void deleteByTitle(String title);
}