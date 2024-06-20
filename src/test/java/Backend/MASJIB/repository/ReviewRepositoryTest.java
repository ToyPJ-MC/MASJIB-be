package Backend.MASJIB.repository;

import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.rating.repository.AssessmentRepository;
import Backend.MASJIB.rating.repository.RatingRepository;
import Backend.MASJIB.review.entity.Review;
import Backend.MASJIB.member.repository.MemberRepository;
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
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ReviewRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private RatingRepository ratingRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReviewRepositoryTest.class);

    @BeforeEach
    void setUpMember(){
        // 멤버 생성
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

        Review createReview2 = Review.builder()
                .member(member)
                .comment("맛이 좋습니다. !!")
                .createTime(LocalDateTime.now().withNano(0).plusHours(1))
                .images(new ArrayList<>())
                .shop(createShop)
                .rating(4)
                .taste("goodTaste")
                .kindness("kindness")
                .hygiene("goodHygiene")
                .build();

        reviewRepository.save(createReview1);
        reviewRepository.save(createReview2);
        member.getReviews().add(createReview1);
        member.getReviews().add(createReview2);
    }

    @Test
    @DisplayName("Review Create Test")
    void 리뷰_작성_테스트(){
        Optional<Member> member = memberRepository.findByEmail("test@test.com");
        Optional<Shop> shop = shopRepository.findByName("김해 국밥");

        Review createReview = Review.builder()
                .member(member.get())
                .shop(shop.get())
                .comment("이집 음식이 최고에요 !")
                .rating(4.5)
                .hygiene("badHygiene")
                .taste("goodTaste")
                .kindness("kindness")
                .images(new ArrayList<>())
                .createTime(LocalDateTime.now())
                .build();
        reviewRepository.save(createReview);

        List<Review> saveReview  = reviewRepository.findByShopAndMember(shop.get(),member.get());
        assertFalse(saveReview.isEmpty(), "Reviews should be saved");
    }
    @Test
    @DisplayName("Review Sort By CreateTime DESC")
    void 리뷰_최신순_조회_테스트(){
        Optional<Member> member = memberRepository.findByEmail("test@test.com");
        Optional<Shop> shop = shopRepository.findByName("김해 국밥");
        List<Review> findReview = reviewRepository.findByReviewAndCreateTimeAsc(shop.get().getId());
        for(Review review : findReview){
            logger.info("review -> "+review.getComment());
        }
    }
    @Test
    @DisplayName("Review Sort By CreateTime ASC")
    void 리뷰_오래된순_조회_테스트(){
        Optional<Shop> shop = shopRepository.findByName("김해 국밥");
        List<Review> findReview = reviewRepository.findByReviewAndCreateTimeAsc(shop.get().getId());
        for(Review review : findReview){
            logger.info("review -> "+review.getComment());
        }
    }
    @Test
    @DisplayName("Review Sort By Rating DESC")
    void 리뷰_높은평점순_조회_테스트(){
        Optional<Shop> shop = shopRepository.findByName("김해 국밥");
        List<Review> findReview = reviewRepository.findByReviewAndRatingDesc(shop.get().getId());
        for(Review review : findReview){
            logger.info("review -> "+review.getComment());
        }
    }
    @Test
    @DisplayName("Review Sort By Rating ASC")
    void 리뷰_낮은평점순_조회_테스트(){
        Optional<Shop> shop = shopRepository.findByName("김해 국밥");
        List<Review> findReview = reviewRepository.findByReviewAndRatingAsc(shop.get().getId());
        for(Review review : findReview){
            logger.info("review -> "+review.getComment());
        }
    }
    @Test
    @DisplayName("Review Delete Test")
    @Transactional
    void 리뷰_삭제_테스트(){
        Optional<Member> member = memberRepository.findByEmail("test@test.com");
        Optional<Shop> shop = shopRepository.findByName("김해 국밥");
        List<Review> findReview = reviewRepository.findByShopAndMember(shop.get(),member.get());

        Long id = findReview.get(0).getId();
        reviewRepository.delete(findReview.get(0));

        Optional<Review> deletedReview = reviewRepository.findById(id);
        assertFalse(deletedReview.isPresent(), "리뷰가 삭제되었습니다.");

    }
}
