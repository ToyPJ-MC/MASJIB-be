package Backend.MASJIB.review.service;

import Backend.MASJIB.image.entity.Image;
import Backend.MASJIB.image.repository.ImageRepository;
import Backend.MASJIB.member.dto.CreateMemberDto;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.member.repository.MemberRepository;
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

        Review review = Review.builder()
                .comment(dto.getComment())
                .createTime(LocalDateTime.now().withNano(0))
                .images(new ArrayList<>())
                .member(findMember.get())
                .rating(dto.getRating())
                .shop(findShop.get())
                .build();

        if(!dto.getFiles().isEmpty()){
            List<String> paths = createImagesPath(dto.getFiles(),findMember.get().getNickname());
            for(String path : paths){
                Image image = Image.builder()
                        .path(path)
                        .review(review)
                        .createTime(LocalDateTime.now())
                        .build();
                review.getImages().add(image);
            }
            findShop.get().setReviewCount(findShop.get().getReviewCount()+1);
        }
        findShop.get().getRating().put(dto.getRating(),findShop.get().getRating().get(dto.getRating())+1);
        reviewRepository.save(review);
        shopRepository.save(findShop.get());

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


}
