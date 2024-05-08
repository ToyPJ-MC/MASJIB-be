package Backend.MASJIB.controller;

import Backend.MASJIB.review.dto.CreateReviewDto;
import Backend.MASJIB.review.dto.ResponseReviewByCreateDto;
import Backend.MASJIB.review.dto.ReviewListDto;
import Backend.MASJIB.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name="review API",description = "리뷰 api")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping(value = "/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE) // 사진의 경우
    @Operation(summary = "리뷰 등록 api",description = "리뷰 text, shopId, rating(맛 평점), taste(goodTaste or badTaste), hygiene(goodHygiene or badHygiene), kindness(kindness or unkindness) 입력합니다.")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity createReview(@ModelAttribute CreateReviewDto dto){
        try{
            String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            ResponseReviewByCreateDto returnDto =reviewService.createReview(dto,memberEmail);
            return ResponseEntity.ok().body(returnDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/review")
    @Operation(summary = "리뷰 조회 api",description = "사용자의 등록된 리뷰 정보를 조회합니다. 리뷰는 생성시간을 기준으로 오름차순 정렬합니다.")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity getReviews(){
        try{
            String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            List<ReviewListDto> reviewList =reviewService.getReviews(memberEmail);
            return ResponseEntity.ok().body(reviewList);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}