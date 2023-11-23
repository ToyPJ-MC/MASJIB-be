package Backend.MASJIB.controller;

import Backend.MASJIB.shop.dto.CreateShopDto;
import Backend.MASJIB.shop.dto.FindByShopByRadiusToSortDto;
import Backend.MASJIB.shop.dto.ResponseShopByCreateDto;
import Backend.MASJIB.shop.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import org.json.simple.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping("/shop")
    @Operation(summary = "맛집 등록")
    public ResponseEntity createShop(CreateShopDto dto){
        try{
            ResponseShopByCreateDto shop =shopService.createShop(dto);
            return ResponseEntity.ok().body(shop);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/shop/radius")
    @Operation(summary = "반경 1km 내 맛집 조회")
    public ResponseEntity getShopByRadius(@RequestParam("sort") String sort,FindByShopByRadiusToSortDto dto){
       try{
           JSONArray shop =shopService.getShopBySortWithPaging(sort, dto);
           System.out.println(shop);
           return ResponseEntity.ok().body(shop);
       }catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }
}
