package Backend.MASJIB.es.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "idx_es_shop")
@Builder
@Setting(settingPath = "es-config/setting.json")
//@Mapping(mappingPath = "es-config/mapping.json")
public class Shop {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String address;

    @Field(type = FieldType.Text)
    private String kind; //업종 명 ex 한식, 중식 ...
    @Field(type = FieldType.Double)
    private Double x;
    @Field(type = FieldType.Double)
    private Double y;
    @Field(type = FieldType.Text)
    private Completion name_suggest;
    public static Shop from(Backend.MASJIB.shop.entity.Shop shop){
        return Shop.builder()
                .id(shop.getId())
                .kind(shop.getKind())
                .address(shop.getAddress())
                .name(shop.getName())
                .x(shop.getX())
                .y(shop.getY())
                .name_suggest(new Completion(new String[]{shop.getName()}))
                .build();
    }
}
