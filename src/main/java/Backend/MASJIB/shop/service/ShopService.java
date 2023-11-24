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
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ShopService {
    private final ShopRepository shopRepository;
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    @Autowired
    public ShopService(ShopRepository shopRepository, ReviewRepository reviewRepository, ImageRepository imageRepository) {
        this.shopRepository = shopRepository;
        this.reviewRepository = reviewRepository;
        this.imageRepository = imageRepository;
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
    @Transactional
    public JSONArray getShopBySortWithPaging(String sort, FindByShopByRadiusToSortDto dto){
        List<Shop> findShop;
        if(sort.equals("rating")){
             findShop= shopRepository.sortByShopWithinRadiusWithRating(dto.getAddress(),dto.getX(),dto.getY());
        }
        else{
            findShop = shopRepository.FindByShopWithinRadiusAndSort(dto.getAddress(),dto.getX(),dto.getY(),sort);
        }
        Map<String,ResponseShopByRadiusDto> shopList = setResponseShop(findShop, dto.getPage());
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalPage",totalPage(findShop.size()));
        jsonArray.add(shopList);
        jsonArray.add(jsonObject);
        return jsonArray;
    }
    private int totalPage(int size){
        if(size<10 && size>=0) return 1;
        else if(size%10!=0)return size/10+1;
        else return size/10;
    }
    private   Map<String,ResponseShopByRadiusDto> setResponseShop(List<Shop> shops,int size){
        Map<String,ResponseShopByRadiusDto> map = new HashMap<>();
        for(int i=0;i<shops.size();i++){
           if((size-1)*10 <=i&& i<size*10){
               Review review = reviewRepository.findReviewByImageNotNUll(shops.get(i).getId());
               if (review == null) {
                   ResponseShopByRadiusDto dto = ResponseShopByRadiusDto.set(shops.get(i), "등록된 리뷰가 없습니다.", "등록된 사진이 없습니다.");
                   map.put(String.valueOf(i + 1), dto);
               }
               else{
                   if(review.getImages().size()==0){
                       ResponseShopByRadiusDto dto = ResponseShopByRadiusDto.set(shops.get(i),review.getComment(),"등록된 사진이 없습니다.");
                       map.put(String.valueOf(i+1),dto);
                   }
                   else{
                       ResponseShopByRadiusDto dto = ResponseShopByRadiusDto.set(shops.get(i),review.getComment(),review.getImages().get(0).getPath());
                       map.put(String.valueOf(i+1),dto);
                   }
               }

           }
        }
        return map;
    }
    private Rating setRating(){
        return Rating.set();
    }
    private Assessment setAssessment(){
        return Assessment.set();
    }
}
