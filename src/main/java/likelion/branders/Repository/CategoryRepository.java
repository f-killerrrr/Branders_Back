package likelion.branders.Repository;
import likelion.branders.Entity.CategoryEntity;
import likelion.branders.Entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
