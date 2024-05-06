package Backend.MASJIB.shop.dto;

import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.shop.entity.Shop;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseShopByAllDto {

    private Long shopId;
    private String name;
    private String address;
    private Double x;
    private Double y;
    private String kind;
    private String image;
    private Double totalRating;

    public static ResponseShopByAllDto set(Shop shop,String image){
        ResponseShopByAllDto dto = new ResponseShopByAllDto();
        dto.setImage(image);
        dto.setAddress(shop.getAddress());
        dto.setX(shop.getX());
        dto.setY(shop.getY());
        dto.setName(shop.getName());
        dto.setShopId(shop.getId());
        Double totalRating = Rating.CalculationRating(shop.getRating());
        if(totalRating.isNaN()) dto.setTotalRating(0.0);
        else dto.setTotalRating(Double.valueOf(String.format("%.2f", totalRating)));
        dto.setKind(shop.getKind());
        return dto;
    }
}
