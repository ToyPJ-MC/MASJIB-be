package Backend.MASJIB.review.entity;

import Backend.MASJIB.image.entity.Image;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.shop.entity.Shop;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String comment;
    @Column(nullable = false)
    private LocalDateTime createTime;
    @ManyToOne
    @JoinColumn(name="member_id",nullable = false)
    @JsonIgnore
    private Member member;
    //rating
    private double rating;

    //assessment
    private String taste;
    private String hygiene;
    private String kindness;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "review")
    @Builder.Default
    @JsonIgnore
    private List<Image> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shop_id",nullable = false)
    @JsonIgnore
    private Shop shop;



}
