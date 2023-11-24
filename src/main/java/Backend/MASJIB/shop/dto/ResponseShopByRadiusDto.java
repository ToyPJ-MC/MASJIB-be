package Backend.MASJIB.shop.dto;

import Backend.MASJIB.image.entity.Image;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.review.entity.Review;
import Backend.MASJIB.shop.entity.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseShopByRadiusDto {
    private String name;
    private String address;
    private double x;
    private double y;
    private String kind;
    private String image;
    private String recentReview;
    private long reviewCount;
    private long followCount;
    private Double totalRating;
    private long shopId;

    public static ResponseShopByRadiusDto set(Shop shop, String review, String image){
        ResponseShopByRadiusDto dto = new ResponseShopByRadiusDto();
        dto.setRecentReview(review);
        dto.setImage(image);
        dto.setName(shop.getName());
        dto.setKind(shop.getKind());
        dto.setAddress(shop.getAddress());
        dto.setX(shop.getX());
        dto.setY(shop.getY());
        dto.setReviewCount(shop.getReviewCount());
        dto.setFollowCount(shop.getFollowCount());
        dto.setShopId(shop.getId());
        Double totalRating = Rating.CalculationRating(shop.getRating());
        if(totalRating.isNaN()) dto.setTotalRating(0.0);
        else dto.setTotalRating(Double.valueOf(String.format("%.2f", totalRating)));
        return dto;
    }
}
