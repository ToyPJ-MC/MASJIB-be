package Backend.MASJIB.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateShopDto {
    private String name;
    private Double x;
    private Double y;
    private String address;
    private String status;
    private String kind;
}
