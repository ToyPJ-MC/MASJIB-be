package Backend.MASJIB.review.dto;

import Backend.MASJIB.image.entity.Image;
import Backend.MASJIB.member.dto.ResponseMemberByCreateDto;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseReviewByCreateDto {
    private long id;
    private String comment;
    private long shopId;
    private double rating;

    private String taste;
    private String hygiene;
    private String kindness;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createTime;
    private List<String> paths = new ArrayList<>();

    public static ResponseReviewByCreateDto set(Review review){
        ResponseReviewByCreateDto createDto = new ResponseReviewByCreateDto();
        createDto.setId(review.getId());
        createDto.setComment(review.getComment());
        createDto.setShopId(review.getShop().getId());
        createDto.setRating(review.getRating());
        for(int i=0;i<review.getImages().size();i++){
            createDto.getPaths().add(review.getImages().get(i).getPath());
        }
        createDto.setCreateTime(review.getCreateTime());
        createDto.setTaste(review.getTaste());
        createDto.setHygiene(review.getHygiene());
        createDto.setKindness(review.getKindness());

        return createDto;
    }
}
