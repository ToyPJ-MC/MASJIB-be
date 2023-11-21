package Backend.MASJIB.shop.service;

import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.shop.dto.CreateShopDto;
import Backend.MASJIB.shop.dto.ResponseShopByCreateDto;
import Backend.MASJIB.shop.entity.Shop;
import Backend.MASJIB.shop.repository.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
public class ShopService {
    private final ShopRepository shopRepository;

    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }
    public ResponseShopByCreateDto createShop(CreateShopDto dto){
        if(shopRepository.existsByAddress(dto.getAddress())){
            throw new RuntimeException("이미 등록된 맛집입니다.");
        }

        Shop createShop = Shop.builder()
                .name(dto.getName())
                .y(Double.valueOf(dto.getY()))
                .x(Double.valueOf(dto.getX()))
                .rating(setRating())
                .assessment(setAssessment())
                .status(dto.getStatus())
                .reviewCount(0L)
                .followCount(0L)
                .kind(dto.getKind())
                .address(dto.getAddress())
                .build();
        shopRepository.save(createShop);
        return ResponseShopByCreateDto.set(createShop);
    }
    private Rating setRating(){
        return Rating.set();
    }
    private Assessment setAssessment(){
        return Assessment.set();
    }
}
