package Backend.MASJIB.review.dto;

import Backend.MASJIB.image.entity.Image;
import Backend.MASJIB.member.dto.ResponseMemberByCreateDto;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResponseReviewByCreateDto {
    private long id;
    private String comment;
    private long member_id;
    private long shop_id;
    private double rating;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createTime;
    private List<Image> images;

    public static ResponseReviewByCreateDto set(Review review){
        ResponseReviewByCreateDto createDto = new ResponseReviewByCreateDto();
        createDto.setId(review.getId());
        createDto.setComment(review.getComment());
        createDto.setMember_id(review.getMember().getId());
        createDto.setShop_id(review.getShop().getId());
        createDto.setRating(review.getRating());
        if(review.getImages() == null)
            createDto.setImages(new ArrayList<>());
        else createDto.setImages(review.getImages());
        createDto.setCreateTime(review.getCreateTime());
        return createDto;
    }
}
