package likelion.branders.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "crawledData")
@Getter
@Setter
@NoArgsConstructor
public class CrawledDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String period;
    private String policyId;

    public CrawledDataEntity(String title, String period, String policyId) {
        this.title = title;
        this.period = period;
        this.policyId = policyId;
    }
}