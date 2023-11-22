package Backend.MASJIB.shop.dto;

import Backend.MASJIB.review.entity.Review;
import Backend.MASJIB.shop.entity.Shop;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

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

    public static JSONObject set(Shop shop, Review review){
        JSONObject obj = new JSONObject();
        obj.put("name",shop.getName());
        obj.put("address",shop.getAddress());
        obj.put("x",shop.getX());
        obj.put("y",shop.getY());
        obj.put("kind",shop.getKind());
        if(review ==null){
            obj.put("image","존재하지 않습니다.");
            obj.put("recentReview","최근 리뷰가 없습니다.");
        }
        else{
            obj.put("image",review.getImages().get(0).getPath());
            obj.put("recentReview",review.getComment());
        }
        obj.put("reviewCount",shop.getReviewCount());

        return obj;
    }
    public static String toString(ResponseShopByRadiusDto dto, int index){
        return index + " {"+
                "name='" + dto.getName() + '\'' +
                ", address='" + dto.getAddress() + '\'' +
                ", x=" + dto.getX() +
                ", y=" + dto.getY() +
                ", kind='" + dto.getKind() + '\'' +
                ", image='" + dto.getImage() + '\'' +
                ", recentReview='" + dto.getRecentReview() + '\'' +
                ", reviewCount=" + dto.getReviewCount() +
                '}';
    }
}
