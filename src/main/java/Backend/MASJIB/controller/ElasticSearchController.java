package Backend.MASJIB.controller;

import Backend.MASJIB.es.document.Shop;
import Backend.MASJIB.es.service.ShopDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/es")
@Tag(name="ElasticSearch API",description = "검색 API")
public class ElasticSearchController {
    private final ShopDocumentService shopDocumentService;

    public ElasticSearchController(ShopDocumentService shopDocumentService) {
        this.shopDocumentService = shopDocumentService;
    }

    @PostMapping("/shop")
    @Operation(summary = "db에 등록된 shop을 document 객체 즉 es에 저장합니다.",description = "es의 지연 저장 문제로 db 저장 속도를 따라오지 못함으로 인한 2차 저장 작업")
    public ResponseEntity searchShop(){
        try{
            shopDocumentService.convertToDocument();
            return ResponseEntity.ok("ok");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/shop")
    @Operation(summary = "es에 등록된 shop을 검색합니다.",description = "가게 상호명, 주소, 카테고리 등등 단 줄임말은 제외(역할, 노통 등), 예시)맘스, 순천, 호프, 기타 이런식으로 검색한다면 조회가능")
    public ResponseEntity getShop(@RequestParam String keyword){
        try{
            List<Shop> shops=shopDocumentService.SearchShop(keyword);
            return ResponseEntity.ok(shops);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body("Error while searching shop");
        }
    }
}
