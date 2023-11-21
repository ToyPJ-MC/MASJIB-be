package Backend.MASJIB.review.dto;

import Backend.MASJIB.member.entity.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CreateReviewDto {
    @NotNull
    private String comment;
    @NotNull
    private long memberId;
    @NotNull
    private long shopId;
    @NotNull
    private double rating;

    //Assessment
    private String taste;
    private String hygiene;
    private String kindness;
    private List<MultipartFile> files = new ArrayList<>();
}
