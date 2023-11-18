package Backend.MASJIB.shop.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String kind;
    @ElementCollection
    private Map<Double,Long> rating = new HashMap<>();

}
