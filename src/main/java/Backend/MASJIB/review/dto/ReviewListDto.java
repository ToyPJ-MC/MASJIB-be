package Backend.MASJIB.review.dto;

import Backend.MASJIB.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewListDto {

    private long reviewId;
    private String comment;
    private long shopId;
    private String shopName;
    private double rating;

    private String taste;
    private String hygiene;
    private String kindness;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createTime;
    private List<String> paths = new ArrayList<>();

    public static ReviewListDto set(Review review, List<String> path){
        ReviewListDto dto = new ReviewListDto();
        dto.setReviewId(review.getId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());

        dto.setShopId(review.getShop().getId());
        dto.setShopName(review.getShop().getName());

        dto.setCreateTime(review.getCreateTime());
        dto.setTaste(review.getTaste());
        dto.setHygiene(review.getHygiene());
        dto.setKindness(review.getKindness());
        dto.setPaths(path);
        return dto;
    }
}
