package likelion.branders.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CrawledDataDTO {

    private String title;
    private String period;
    private String policyId;

}