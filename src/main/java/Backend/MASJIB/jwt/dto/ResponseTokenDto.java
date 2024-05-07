package Backend.MASJIB.jwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseTokenDto {
    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;
    private Long refreshTokenExpiresIn;
}
