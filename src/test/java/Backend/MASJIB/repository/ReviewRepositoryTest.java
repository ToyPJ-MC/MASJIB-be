package Backend.MASJIB.repository;

import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.review.dto.CreateReviewDto;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReviewRepositoryTest.class);

    @BeforeEach
    void setUpMember(){
        Member member = Member.builder()
                .name("지우")
                .nickname("포켓몬 마스터")
                .email("test@test.com")
                .createTime(LocalDateTime.now())
                .reviews(new ArrayList<>())
                .shops(new ArrayList<>())
                .build();
        memberRepository.save(member);
        Shop createShop = Shop.builder()
                .name("김해 국밥")
                .x(12.123124)
                .y(25.1345)
                .reviewCount(1)
                .address("경남 김해시 인제로")
                .rating(new HashMap<>())
                .kind("한식")
                .build();
        shopRepository.save(createShop);

        Review createReview = Review.builder()
                .member(member)
                .comment("이집 음식이 굳 !")
                .createTime(LocalDateTime.now().withNano(0))
                .images(new ArrayList<>())
                .shop(createShop)
                .rating(3.5)
                .build();

        member.getReviews().add(createReview);
    }
    @Test
    @DisplayName("Find Review By Member Test")
    @Transactional
    void 멤버_리뷰_조회_테스트(){
        String findName = "지우";
        Optional<Member> findMember = memberRepository.findByName(findName);
        findMember.orElseThrow(RuntimeException::new);

        Optional<Shop> findShop = shopRepository.findByName("김해 국밥");
        findShop.orElseThrow(RuntimeException::new);

        Optional<Review> findReview = reviewRepository.findByShopAndMember(findShop.get(), findMember.get());
        findReview.orElseThrow(RuntimeException::new);

        logger.info(findReview.get().getComment());
    }

    @Test
    @DisplayName("Review Create Test")
    @Transactional
    void 리뷰_작성_테스트(){
        String findName = "지우";
        Optional<Member> findMember = memberRepository.findByName(findName);
        findMember.orElseThrow(RuntimeException::new);

        String findShopName = "김해 국밥";
        Optional<Shop> findShop = shopRepository.findByName(findShopName);
        findShop.orElseThrow(RuntimeException::new);

        Review createReview = Review.builder()
                .member(findMember.get())
                .comment("이집 음식이 최고에요 !")
                .images(new ArrayList<>())
                .shop(findShop.get())
                .rating(4.5)
                .createTime(LocalDateTime.now())
                .build();
        long reviewCount = findShop.get().getReviewCount();
        if(findShop.get().getRating().containsKey(createReview.getRating())){
            findShop.get().getRating().put(createReview.getRating(),findShop.get().getRating().get(createReview.getRating())+1);
        }

        findShop.get().setReviewCount(reviewCount+1);

        findMember.get().getReviews().add(createReview);

        shopRepository.save(findShop.get());
        memberRepository.save(findMember.get());
        Review actualReview = findMember.get().getReviews().get(1);
        assertThat(actualReview.getComment()).isEqualTo("이집 음식이 최고에요 !");
    }

    @Test
    @DisplayName("Review Update Test")
    @Transactional
    void 리뷰_업데이트_테스트(){
        String findName = "지우";
        Optional<Member> findMember = memberRepository.findByName(findName);
        findMember.orElseThrow(RuntimeException::new);

        String findShopName = "김해 국밥";
        Optional<Shop> findShop = shopRepository.findByName(findShopName);
        findShop.orElseThrow(RuntimeException::new);

        Optional<Review> findReview = reviewRepository.findByShopAndMember(findShop.get(),findMember.get());
        findReview.orElseThrow(RuntimeException::new);

        String updateComment = "사실 음식이 별로였어요";
        findReview.get().setComment(updateComment);
        findMember.get().getReviews().add(findReview.get());

        Review actualReview = findMember.get().getReviews().get(0);
        assertThat(actualReview.getComment()).isEqualTo(updateComment);
    }



    @Test
    @DisplayName("Review Hard Delete Test")
    @Transactional
    void 리뷰_Hard_Delete_테스트(){
        String findName = "지우";
        Optional<Member> findMember = memberRepository.findByName(findName);
        findMember.orElseThrow(RuntimeException::new);

        String findShopName = "김해 국밥";
        Optional<Shop> findShop = shopRepository.findByName(findShopName);
        findShop.orElseThrow(RuntimeException::new);

        Optional<Review> findReview = reviewRepository.findByShopAndMember(findShop.get(),findMember.get());
        findReview.orElseThrow(RuntimeException::new);

        findMember.get().getReviews().remove(findReview.get());
        reviewRepository.delete(findReview.get());
        //memberRepository.save(findMember.get());
    }

}
