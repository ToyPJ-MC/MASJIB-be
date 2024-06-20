package Backend.MASJIB.es.dto;

import lombok.*;
import  org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RealTimeKeywordDto {

    private String name;
    private long count;

    public static RealTimeKeywordDto from(Bucket bucket){
        RealTimeKeywordDto dto = new RealTimeKeywordDto();
        dto.setName(bucket.getKeyAsString());
        dto.setCount(bucket.getDocCount());
        return dto;
    }
}
