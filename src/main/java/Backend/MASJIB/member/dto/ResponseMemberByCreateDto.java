package Backend.MASJIB.member.dto;

import Backend.MASJIB.member.entity.Member;
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
public class ResponseMemberByCreateDto {
    private Long id;
    private String name;
    private String email;
    private String nickName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createTime;
    private List<Review> reviews = new ArrayList<>();

    public static ResponseMemberByCreateDto set(Member member){
        ResponseMemberByCreateDto createDto = new ResponseMemberByCreateDto();
        createDto.setId(member.getId());
        createDto.setCreateTime(member.getCreateTime());
        createDto.setName(member.getName());
        createDto.setReviews(member.getReviews());
        createDto.setNickName(member.getNickname());
        createDto.setEmail(member.getEmail());

        return createDto;
    }
}
