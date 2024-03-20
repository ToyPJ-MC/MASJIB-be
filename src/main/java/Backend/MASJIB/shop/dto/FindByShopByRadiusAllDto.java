package Backend.MASJIB.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class FindByShopByRadiusAllDto {
    private String address;
    private Double x;
    private Double y;
}
