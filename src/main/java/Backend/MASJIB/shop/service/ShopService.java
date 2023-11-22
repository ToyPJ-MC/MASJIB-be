package Backend.MASJIB.shop.service;

import Backend.MASJIB.image.repository.ImageRepository;
import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.review.entity.Review;
import Backend.MASJIB.review.repository.ReviewRepository;
import Backend.MASJIB.shop.dto.CreateShopDto;
import Backend.MASJIB.shop.dto.FindByShopByRadiusToSortDto;
import Backend.MASJIB.shop.dto.ResponseShopByCreateDto;
import Backend.MASJIB.shop.dto.ResponseShopByRadiusDto;
import Backend.MASJIB.shop.entity.Shop;
import Backend.MASJIB.shop.repository.ShopRepository;
import com.mysql.cj.xdevapi.JsonArray;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ShopService {
    private final ShopRepository shopRepository;
    private final ReviewRepository reviewRepository;
    @Autowired
    public ShopService(ShopRepository shopRepository, ReviewRepository reviewRepository) {
        this.shopRepository = shopRepository;
        this.reviewRepository = reviewRepository;
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
    public  List<JSONObject> getShopBySortWithPaging(String sort, FindByShopByRadiusToSortDto dto){
        List<Shop> findShop;
        if(sort.equals("rating")){
             findShop= shopRepository.sortByShopWithinRadiusWithRating(dto.getAddress(),dto.getX(),dto.getY());
        }
        else{
            findShop = shopRepository.FindByShopWithinRadiusAndSort(dto.getAddress(),dto.getX(),dto.getY(),sort);
        }
        List<JSONObject> responseShopDtoList = setResponseShopDto(findShop,dto.getPage());
        JSONObject object = setMaxPage(findShop.size());
        responseShopDtoList.add(object);
        return responseShopDtoList;
    }
    private JSONObject setMaxPage(int size){
        JSONObject object = new JSONObject();
        if(size/10<10)object.put("maxPage",1);
        else if(size%10!=0) object.put("maxPage",size/10+1);
        else object.put("maxPage",size/10);
        return object;
    }

    private   List<JSONObject> setResponseShopDto(List<Shop> shops, int size){
        List<JSONObject> responseShopDtoList = new ArrayList<>();
        for(int i=0;i<shops.size();i++){
            if(i<(size+1)*10 && i>=(size)*10){
                Review findReivewWithNotNUllImage = reviewRepository.findReviewByImageNotNUll(shops.get(i).getId());
                JSONObject responseShopByRadiusDto = ResponseShopByRadiusDto.set(shops.get(i),findReivewWithNotNUllImage);
                responseShopDtoList.add(responseShopByRadiusDto);
            }
        }
        return responseShopDtoList;
    }

    private Rating setRating(){
        return Rating.set();
    }
    private Assessment setAssessment(){
        return Assessment.set();
    }
}
