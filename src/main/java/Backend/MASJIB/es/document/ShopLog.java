package Backend.MASJIB.es.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "log"+"-#{@indexNameProvider.dateSuffix()}")
@Builder
public class ShopLog {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Text)
    private String keyword;
    @Field(type = FieldType.Long)
    private Long count;
    @Field(type = FieldType.Date, format = {DateFormat.date_hour_minute, DateFormat.date_hour_minute})
    private LocalDateTime createTime;

    public static ShopLog from(String keyword){
        return ShopLog.builder()
                .id(UUID.randomUUID().toString())
                .keyword(keyword)
                .count(0L)
                .createTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")).withNano(0))
                .build();
    }
}
