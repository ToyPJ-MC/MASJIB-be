package Backend.MASJIB.member.dto;

import Backend.MASJIB.image.entity.Image;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.review.dto.ResponseReviewWithImagePathDto;
import Backend.MASJIB.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMemberbyFindDto {

    private Long id;
    private String name;
    private String email;
    private String nickName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createTime;
    private List<ResponseReviewWithImagePathDto> reviews = new ArrayList<>();

    public static ResponseMemberbyFindDto set(Member member){
        ResponseMemberbyFindDto createDto = new ResponseMemberbyFindDto();
        createDto.setId(member.getId());
        createDto.setCreateTime(member.getCreateTime());
        createDto.setName(member.getName());
        createDto.setNickName(member.getNickname());
        createDto.setEmail(member.getEmail());
        List<ResponseReviewWithImagePathDto> reviews = new ArrayList<>();
        for(int i=0;i<member.getReviews().size();i++){
            List<String> paths = new ArrayList<>();
            ResponseReviewWithImagePathDto dto = ResponseReviewWithImagePathDto.set(member.getReviews().get(i));
            for(Image image : member.getReviews().get(i).getImages()){
                if(image.getReview().getId()==member.getReviews().get(i).getId()){
                    String path = image.getPath();
                    paths.add(path);
                }
            }
            dto.setImagePaths(paths);
            reviews.add(dto);
        }
        createDto.setReviews(reviews);

        return createDto;
    }

}
