package Backend.MASJIB.member.dto;

import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private List<Review> reviews = new ArrayList<>();

    public static ResponseMemberbyFindDto set(Member member){
        ResponseMemberbyFindDto createDto = new ResponseMemberbyFindDto();
        createDto.setId(member.getId());
        createDto.setCreateTime(member.getCreateTime());
        createDto.setName(member.getName());
        createDto.setNickName(member.getNickname());
        createDto.setEmail(member.getEmail());
        if(member.getReviews().isEmpty()) createDto.setReviews(new ArrayList<>());
        else createDto.setReviews(member.getReviews());

        return createDto;
    }

}
