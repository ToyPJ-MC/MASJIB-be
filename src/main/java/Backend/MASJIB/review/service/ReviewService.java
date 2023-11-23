package Backend.MASJIB.review.service;

import Backend.MASJIB.image.entity.Image;
import Backend.MASJIB.image.repository.ImageRepository;
import Backend.MASJIB.member.dto.CreateMemberDto;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.member.repository.MemberRepository;
import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.review.dto.CreateReviewDto;
import Backend.MASJIB.review.dto.ResponseReviewByCreateDto;
import Backend.MASJIB.review.entity.Review;
import Backend.MASJIB.review.repository.ReviewRepository;
import Backend.MASJIB.shop.entity.Shop;
import Backend.MASJIB.shop.repository.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReviewService {
    private final ShopRepository shopRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ImageRepository imageRepository,
                         MemberRepository memberRepository,
                         ShopRepository shopRepository) {
        this.reviewRepository = reviewRepository;
        this.imageRepository = imageRepository;
        this.memberRepository = memberRepository;
        this.shopRepository = shopRepository;
    }
    @Transactional
    public ResponseReviewByCreateDto createReview(CreateReviewDto dto) {
        Optional<Member> findMember = memberRepository.findById(dto.getMemberId());
        findMember.orElseThrow(RuntimeException::new);

        Optional<Shop> findShop = shopRepository.findById(dto.getShopId());
        findShop.orElseThrow(RuntimeException::new);
        setRatingToShop(findShop.get(),dto.getRating());
        setAssessment(findShop.get(),dto.getTaste(),dto.getHygiene(),dto.getKindness());
        shopRepository.save(findShop.get());

        Review review = Review.builder()
                .comment(dto.getComment())
                .createTime(LocalDateTime.now().withNano(0))
                .images(new ArrayList<>())
                .member(findMember.get())
                .rating(dto.getRating())
                .shop(findShop.get())
                .hygiene(dto.getHygiene())
                .kindness(dto.getKindness())
                .taste(dto.getTaste())
                .build();

        reviewRepository.save(review);

        if(!dto.getFiles().isEmpty()){
            List<String> paths = createImagesPath(dto.getFiles(),findMember.get().getNickname());
            for(String path : paths){
                Image image = Image.builder()
                        .path(path)
                        .review(review)
                        .createTime(LocalDateTime.now())
                        .shopId(findShop.get().getId())
                        .build();
                imageRepository.save(image);
                review.getImages().add(image);
            }
        }
        findMember.get().getReviews().add(review);
        return ResponseReviewByCreateDto.set(review);
    }

    private List<String> createImagesPath(List<MultipartFile> file,String name){
        List<String> paths = new ArrayList<>();

        String uploadDir = "images";

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for(int i=0;i<file.size();i++){
            String outputFileName = name+"-"+file.get(i).getOriginalFilename();
            Path outputFile = Paths.get(uploadDir, outputFileName);
            paths.add(outputFile.toString());
        }
        return paths;
    }

    private void setRatingToShop(Shop shop, Double rating){
        if(rating==5.0) shop.getRating().setFive(shop.getRating().getFive()+1);
        else if(rating==4.5) shop.getRating().setFourHalf(shop.getRating().getFourHalf()+1);
        else if(rating==4.0) shop.getRating().setFour(shop.getRating().getFour()+1);
        else if(rating==3.5) shop.getRating().setThreeHalf(shop.getRating().getThreeHalf()+1);
        else if(rating==3.0) shop.getRating().setThree(shop.getRating().getThree()+1);
        else if(rating==2.5) shop.getRating().setTwoHalf(shop.getRating().getTwoHalf()+1);
        else if(rating==2.0) shop.getRating().setTwo(shop.getRating().getTwo()+1);
        else if(rating==1.5) shop.getRating().setOneHalf(shop.getRating().getOneHalf()+1);
        else if(rating==1.0) shop.getRating().setOne(shop.getRating().getOne()+1);
        else if(rating==0.5) shop.getRating().setHalf(shop.getRating().getHalf()+1);
        else shop.getRating().setZero(shop.getRating().getZero()+1);
        shop.getRating().setCount(shop.getRating().getCount()+1);
        shop.setReviewCount(shop.getReviewCount()+1);
    }
    private void setAssessment(Shop shop,String taste,String hygiene,String kindness){
        if(taste.equals("goodTaste")) shop.getAssessment().setGoodTaste(shop.getAssessment().getGoodTaste()+1);
        else if(taste.equals("badTaste")) shop.getAssessment().setBadTaste(shop.getAssessment().getBadTaste()+1);
        if(hygiene.equals("goodHygiene")) shop.getAssessment().setGoodHygiene(shop.getAssessment().getGoodHygiene()+1);
        else if(hygiene.equals("badHygiene")) shop.getAssessment().setBadHygiene(shop.getAssessment().getBadHygiene()+1);
        if(kindness.equals("unkindness")) shop.getAssessment().setUnkindness(shop.getAssessment().getUnkindness()+1);
        else if(kindness.equals("kindness")) shop.getAssessment().setKindness(shop.getAssessment().getKindness()+1);
    }
}
