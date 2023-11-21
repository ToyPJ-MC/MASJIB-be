package Backend.MASJIB.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Description;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindByShopByRadiusToSortDto {
    
    private String address;
    private double x;
    private double y;

}
