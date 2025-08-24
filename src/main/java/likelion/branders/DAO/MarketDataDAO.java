package likelion.branders.DAO;

import likelion.branders.Entity.MarketDataEntity;
import likelion.branders.Repository.MarketDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MarketDataDAO {
    private final MarketDataRepository repository;


}