package Backend.MASJIB.shop.dto;

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

    public static ResponseShopByRadiusDto set(Shop shop, Review review){
        ResponseShopByRadiusDto dto = new ResponseShopByRadiusDto();
        dto.setName(shop.getName());
        dto.setAddress(shop.getAddress());
        dto.setX(shop.getX());
        dto.setY(shop.getY());
        dto.setKind(shop.getKind());
        if(review ==null){
            dto.setImage("존재하지 않습니다.");
            dto.setRecentReview("최근 리뷰가 없습니다.");
        }
        else{
            dto.setImage(review.getImages().get(0).getPath());
            dto.setRecentReview(review.getComment());
        }
        dto.setReviewCount(shop.getReviewCount());
        return dto;
    }
}
