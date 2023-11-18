package Backend.MASJIB.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class CreateMemberDto {
    private String name;
    private String email;
    private String nickname;
    public CreateMemberDto(String name, String email, String nickname){
        this.email = email;
        this.name = name;
        this.nickname = nickname;
    }

}
