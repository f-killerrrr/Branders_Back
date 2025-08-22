package likelion.branders.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CrawledDataDTO {
    // Getters & Setters
    private String title;
    private String period;
    private String policyId;

    public CrawledDataDTO() {}

    public CrawledDataDTO(String title, String period, String policyId) {
        this.title = title;
        this.period = period;
        this.policyId = policyId;
    }

}