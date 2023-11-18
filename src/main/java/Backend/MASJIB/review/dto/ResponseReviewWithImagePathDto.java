package Backend.MASJIB.review.dto;

import Backend.MASJIB.member.dto.ResponseMemberbyFindDto;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResponseReviewWithImagePathDto {
    private long id;
    private String comment;
    private LocalDateTime createTime;
    private List<String> imagePaths = new ArrayList<>();
    private long shopId;
    private String shopName;
    public static ResponseReviewWithImagePathDto set(Review review){
        ResponseReviewWithImagePathDto dto = new ResponseReviewWithImagePathDto();
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setShopId(review.getShop().getId());
        dto.setCreateTime(review.getCreateTime());
        dto.setImagePaths(new ArrayList<>());
        dto.setShopName(review.getShop().getName());
        return dto;
    }
}
