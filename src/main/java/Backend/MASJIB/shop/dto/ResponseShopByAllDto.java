package Backend.MASJIB.shop.dto;

import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.shop.entity.Shop;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseShopByAllDto {

    private String image;
    private String address;
    private Double x;
    private Double y;
    private String name;
    private Double totalRating;
    private String kind;

    public static ResponseShopByAllDto set(Shop shop,String image){
        ResponseShopByAllDto dto = new ResponseShopByAllDto();
        dto.setImage(image);
        dto.setAddress(shop.getAddress());
        dto.setX(shop.getX());
        dto.setY(shop.getY());
        dto.setName(shop.getName());
        Double totalRating = Rating.CalculationRating(shop.getRating());
        if(totalRating.isNaN()) dto.setTotalRating(0.0);
        else dto.setTotalRating(Double.valueOf(String.format("%.2f", totalRating)));
        dto.setKind(shop.getKind());
        return dto;
    }
}
