package Backend.MASJIB.shop.entity;

import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private Double x;
    private Double y;
    private String address;
    private String status;

    private long reviewCount;
    private long followCount;

    private String kind; //업종 명 ex 한식, 중식 ...

    @OneToOne
    private Rating rating; //가게 평점
    @OneToOne
    private Assessment assessment; //가게 평가(맛, 위생, 친절함)

}
