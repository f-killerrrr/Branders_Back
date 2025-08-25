package likelion.branders.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "crawled_data")
@Getter
@Setter
@NoArgsConstructor
public class CrawledDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "period")
    private String period;

    @Column(name = "policyId")
    private String policyId;

    public CrawledDataEntity(String title, String period, String policyId) {
        this.title = title;
        this.period = period;
        this.policyId = policyId;
    }
}