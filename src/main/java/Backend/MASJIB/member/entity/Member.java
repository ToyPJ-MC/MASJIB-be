package Backend.MASJIB.member.entity;

import Backend.MASJIB.review.entity.Review;
import Backend.MASJIB.shop.entity.Shop;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column(nullable = false)
    private String name;
    //@Column(nullable = false)
    private String email;
    //@Column(nullable = false)
    private String nickname;

    //@Column(nullable = false)
    private LocalDateTime createTime;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private List<Shop> shops = new ArrayList<>();
}
