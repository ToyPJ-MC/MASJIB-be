package Backend.MASJIB.image.entity;

import Backend.MASJIB.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String path;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createTime;
    @ManyToOne
    @JoinColumn(name = "review_id",nullable = false)
    private Review review;
    private long shopId;

}
