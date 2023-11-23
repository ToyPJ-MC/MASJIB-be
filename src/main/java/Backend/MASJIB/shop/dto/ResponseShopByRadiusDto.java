package Backend.MASJIB.shop.dto;

import Backend.MASJIB.image.entity.Image;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.review.entity.Review;
import Backend.MASJIB.shop.entity.Shop;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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

    public static ResponseShopByRadiusDto set(Shop shop, Review review, Image image){
        ResponseShopByRadiusDto dto = new ResponseShopByRadiusDto();
        if(review == null) {
            dto.setRecentReview("코멘트가 없습니다.");
            dto.setImage("이미지가 없습니다.");
        }
        else{
            dto.setRecentReview(review.getComment());
            dto.setImage(image.getPath());
            System.out.println("시발 "+image.getPath());
        }
        dto.setName(shop.getName());
        dto.setKind(shop.getKind());
        dto.setAddress(shop.getAddress());
        dto.setX(shop.getX());
        dto.setY(shop.getY());
        dto.setReviewCount(shop.getReviewCount());
        dto.setFollowCount(shop.getFollowCount());
        Double totalRating = Rating.CalculationRating(shop.getRating());
        if(totalRating.isNaN()) dto.setTotalRating(0.0);
        else dto.setTotalRating(Double.valueOf(String.format("%.2f", totalRating)));
        return dto;
    }
}
