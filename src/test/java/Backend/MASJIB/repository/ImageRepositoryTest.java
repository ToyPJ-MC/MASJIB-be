package Backend.MASJIB.repository;

import Backend.MASJIB.image.entity.Image;
import Backend.MASJIB.image.repository.ImageRepository;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.member.repository.MemberRepository;
import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.rating.repository.AssessmentRepository;
import Backend.MASJIB.rating.repository.RatingRepository;
import Backend.MASJIB.review.entity.Review;
import Backend.MASJIB.review.repository.ReviewRepository;
import Backend.MASJIB.shop.entity.Shop;
import Backend.MASJIB.shop.repository.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;

    private static final Logger logger = LoggerFactory.getLogger(ImageRepositoryTest.class);

    @BeforeEach
    void setUp(){
        Member member = Member.builder()
                .nickname("@user-a1da23")
                .email("test@test.com")
                .createTime(LocalDateTime.now())
                .reviews(new ArrayList<>())
                .shops(new ArrayList<>())
                .build();
        memberRepository.save(member);

        // 평점 생성
        Rating rating=Rating.set();
        ratingRepository.save(rating);
        // 평가 생성
        Assessment assessment = Assessment.set();
        assessmentRepository.save(assessment);

        //가게 생성
        Shop createShop = Shop.builder()
                .name("김해 국밥")
                .x(12.123124)
                .y(25.1345)
                .reviewCount(2)
                .address("경남 김해시 인제로")
                .rating(rating)
                .assessment(assessment)
                .kind("한식")
                .build();
        shopRepository.save(createShop);

        // 리뷰 생성
        Review createReview1 = Review.builder()
                .member(member)
                .comment("이집 음식이 굳 !")
                .createTime(LocalDateTime.now().withNano(0))
                .images(new ArrayList<>())
                .shop(createShop)
                .rating(3.5)
                .taste("goodTaste")
                .kindness("kindness")
                .hygiene("goodHygiene")
                .build();
        reviewRepository.save(createReview1);

        Image image =Image.builder()
                .createTime(LocalDateTime.now())
                .path("images/멤버_id-UUID.확장자(jpeg)")
                .review(createReview1)
                .shopId(createShop.getId())
                .build();

        Image image2 =Image.builder()
                .createTime(LocalDateTime.now().plusHours(1))
                .path("images/멤버_id-UUID.확장자(png)")
                .review(createReview1)
                .shopId(createShop.getId())
                .build();
        Image image3 =Image.builder()
                .createTime(LocalDateTime.now().plusHours(2))
                .path("images/멤버_id-UUID.확장자(png)")
                .review(createReview1)
                .shopId(createShop.getId())
                .build();
        Image image4 =Image.builder()
                .createTime(LocalDateTime.now().plusHours(3))
                .path("images/멤버_id-UUID.확장자(png)")
                .review(createReview1)
                .shopId(createShop.getId())
                .build();
        Image image5 =Image.builder()
                .createTime(LocalDateTime.now().plusHours(4))
                .path("images/멤버_id-UUID.확장자(png)")
                .review(createReview1)
                .shopId(createShop.getId())
                .build();
        Image image6 =Image.builder()
                .createTime(LocalDateTime.now().plusHours(5))
                .path("images/멤버_id-UUID.확장자(png)")
                .review(createReview1)
                .shopId(createShop.getId())
                .build();

        imageRepository.save(image);
        imageRepository.save(image2);
        imageRepository.save(image3);
        imageRepository.save(image4);
        imageRepository.save(image5);
        imageRepository.save(image6);
    }
    @Test
    @DisplayName("이미지를 가게 아이디로 조회합니다. 최근한 건만")
    void  이미지_음식점_id_조회_테스트(){
        String findShopName = "김해 국밥";
        Optional<Shop> findShop = shopRepository.findByName(findShopName);

        Image image = imageRepository.findByRecentImage(findShop.get().getId());
        logger.info(String.valueOf(image.getCreateTime()));
    }
    @Test
    @DisplayName("이미지를 최근 생성 시간 기준으로 5개를 가져옵니다.")
    void 이미지_조회시_최근_생성시간_기준_다섯개만_가져오는_테스트(){
        String findShopName = "김해 국밥";
        Optional<Shop> findShop = shopRepository.findByName(findShopName);

        List<Image> images = imageRepository.findByImageWithShopIdAndLimitFive(findShop.get().getId());
        for(Image image : images) logger.info(String.valueOf(image.getCreateTime()));
    }
    @Test
    @DisplayName("이미지를 최근 생성 시간 기준으로 가져옵니다.")
    void 이미지_조회시_최근_생성시간_기준_전체_가져오는_테스트(){
        String findShopName = "김해 국밥";
        Optional<Shop> findShop = shopRepository.findByName(findShopName);

        List<Image> images = imageRepository.findByImageWithShopIdAndLimitFive(findShop.get().getId());
        for(Image image : images) logger.info(String.valueOf(image.getCreateTime()));
    }

}
