package Backend.MASJIB.controller;

import Backend.MASJIB.shop.dto.*;
import Backend.MASJIB.shop.entity.Shop;
import Backend.MASJIB.shop.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.simple.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@Tag(name = "Shop API",description = "음식점 API")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping("/shop")
    @Operation(summary = "맛집 등록")
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity createShop(CreateShopDto dto){
        try{
            ResponseShopByCreateDto shop =shopService.createShop(dto);
            return ResponseEntity.ok().body(shop);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/shop/{shopId}/images")
    @Operation(summary = "선택한 음식점의 세부 페이지의 이미지를 추가로 가져옵니다.", description = "페이징 5개씩 ")
    public ResponseEntity getShopDetails(@PathVariable("shopId") long shopId,
                                         @RequestParam(name = "page", defaultValue = "1") int page){
        try{
             List<String> imagePath = shopService.getShopImages(shopId, page);
            return ResponseEntity.ok().body(imagePath);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/shop/{shopId}")
    @Operation(summary = "선택한 음식점의 세부 페이지를 불러옵니다.", description = "음식점 id와 sortType(newest, oldest, highestRated, lowestRated), reviewType(onlyPictures,onlyText,based) 원하는 페이지 1부터 ~")
    public ResponseEntity getShopDetails(@PathVariable("shopId") long shopId,@RequestParam String sortType, @RequestParam String reviewType, @RequestParam int page){
        try{
            JSONArray shopDetails = shopService.getShopDetailsWithReviewsOrderBySorting(shopId, sortType, reviewType, page);
            return ResponseEntity.ok().body(shopDetails);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/shop/radius")
    @Operation(summary = "반경 1km 내 맛집을 선택한 정렬 기준을 사용해 조회합니다.",description = "rating(별점), review(리뷰 갯수 순), follow(찜 순) 내림차순으로 함, page 번호는 1번부터 ~")
    public ResponseEntity getShopByRadius(@RequestParam("sort") String sort,FindByShopByRadiusToSortDto dto){
       try{
           JSONArray shop =shopService.getShopBySortWithPaging(sort, dto);
           return ResponseEntity.ok().body(shop);
       }catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }
    @GetMapping("/shop/radius/all")
    @Operation(summary = "반경 1km 내 맛집 전체 조회", description = "이름 기준으로 내림차순해서 정렬함")
    public ResponseEntity getShopByRadiusAll(FindByShopByRadiusAllDto dto){
        try{
            List<ResponseShopByAllDto> dtos = shopService.getShopByRadiusAll(dto);
            return ResponseEntity.ok().body(dtos);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/shop/test")
    @Operation(summary = "속도 테스트")
    public ResponseEntity getShopByTest(){
        try{
            List<Shop> dtos = shopService.testshop();
            return ResponseEntity.ok().body(dtos);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

/*
long beforeTime = System.nanoTime(); //코드 실행 전에 시간 받아오기
long afterTime = System.nanoTime(); // 코드 실행 후에 시간 받아오기
long secDiffTime = (afterTime - beforeTime)/1000000; //두 시간에 차 계산
System.out.println("시간차이(m) : "+secDiffTime);
*/
