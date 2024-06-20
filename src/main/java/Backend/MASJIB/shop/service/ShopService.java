package Backend.MASJIB.shop.service;

import Backend.MASJIB.image.entity.Image;
import Backend.MASJIB.image.repository.ImageRepository;
import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.review.entity.Review;
import Backend.MASJIB.review.repository.ReviewRepository;
import Backend.MASJIB.shop.dto.*;
import Backend.MASJIB.shop.entity.Shop;
import Backend.MASJIB.shop.repository.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public List<Shop> testshop(){
        return shopRepository.testByes();
    }
    public List<String> getShopImages(long shopId, int page){
        Optional<Shop> findShop = shopRepository.findById(shopId);
        findShop.orElseThrow(RuntimeException::new);

        List<Image> findImages = imageRepository.findByAllImageWithShopId(findShop.get().getId());
        return getImagesPathWithPaging(findImages,page);
    }
    @Transactional(readOnly = true)
    public JSONArray getShopDetailsWithReviewsOrderBySorting(long shopId, String sortType, String reviewType, int page){
        Optional<Shop> findShop = shopRepository.findById(shopId);
        findShop.orElseThrow(RuntimeException::new);
        List<Image> findImages = imageRepository.findByImageWithShopIdAndLimitFive(findShop.get().getId());
        List<Review> findReviews = new ArrayList<>();
        switch (sortType){
            case "newest": findReviews = reviewRepository.findByReviewAndCreateTimeDesc(findShop.get().getId());
            break;
            case "oldest": findReviews = reviewRepository.findByReviewAndCreateTimeAsc(findShop.get().getId());
            break;
            case "highestRated": findReviews = reviewRepository.findByReviewAndRatingDesc(findShop.get().getId());
            break;
            case "lowestRated": findReviews = reviewRepository.findByReviewAndRatingAsc(findShop.get().getId());
            break;
            default: break;
        }
        JSONArray sortReviews = new JSONArray();
        switch (reviewType){
            case "onlyPictures": sortReviews = getReviewWithImage(findReviews,page);
                break;
            case "onlyText": sortReviews = getReviewWithOutImage(findReviews,page);
                break;
            case "based" : sortReviews = getReviewWithDefault(findReviews,page);
                break;
            default: break;
        }
        JSONArray array = new JSONArray();
        JSONObject images = new JSONObject();

        JSONObject totalPage = new JSONObject();
        totalPage.put("totalPage",totalPage(sortReviews.size()));

        JSONObject totalRating = new JSONObject();
        totalRating.put("totalRating",getTotalRating(findShop.get().getRating()));

        array.add(findShop.get());
        images.put("shop_images",getImagesPath(findImages));
        array.add(images);
        array.add(sortReviews);
        array.add(totalPage);
        array.add(totalRating);

        return array;
    }
    private JSONArray getReviewWithDefault(List<Review> reviews, int page){
        JSONArray arr = new JSONArray();
        for(int i=0;i<reviews.size();i++){
            JSONObject obj = new JSONObject();
            if((page-1)*10 <=i&& i<page*10){
                JSONArray images = new JSONArray();
                obj.put("review",reviews.get(i));
                if(!reviews.get(i).getImages().isEmpty()){
                    for(Image image : reviews.get(i).getImages()){
                        images.add(image.getPath());
                    }
                    obj.put("imagePath",images);
                }
            }
            arr.add(obj);
        }
        return arr;
    }
    private Double getTotalRating(Rating rating){
        return Rating.CalculationRating(rating);
    }
    private List<String> getImagesPathWithPaging(List<Image> images, int page){
        List<String> path = new ArrayList<>();
        for(int i=0;i<images.size();i++){
            if((page-1)*5<=i && i<page*5){
                path.add(images.get(i).getPath());
            }
        }
        return path;
    }

    private List<String> getImagesPath(List<Image> images){
        List<String> path = new ArrayList<>();
        for(Image image : images){
            path.add(image.getPath());
        }
        return path;
    }
    private JSONArray getReviewWithImage(List<Review> reviews,int page){
        JSONArray arr = new JSONArray();

        for(int i=0;i<reviews.size();i++){
            JSONObject obj = new JSONObject();
            if((page-1)*10 <=i && i<page*10){ // 0~9
                JSONArray images = new JSONArray();
                if(reviews.get(i).getImages().isEmpty()) continue;
                else{
                    obj.put("review",reviews.get(i));
                    for(Image image : reviews.get(i).getImages()){
                        images.add(image.getPath());
                    }
                }
                obj.put("imagePath",images);
                arr.add(obj);
            }
        }
        return arr;
    }

    private JSONArray getReviewWithOutImage(List<Review> reviews,int page){
        JSONArray arr =new JSONArray();
        for(int i=0;i<reviews.size();i++){
            JSONObject obj = new JSONObject();
            if((page-1)*10 <=i&& i<page*10){
                if(reviews.get(i).getImages().isEmpty()) obj.put("review",reviews.get(i));
            }
            arr.add(obj);
        }
        return arr;
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
    @Transactional(readOnly = true)
    public List<ResponseShopByAllDto> getShopByRadiusAll(FindByShopByRadiusAllDto dto){

        List<Shop> findShop = shopRepository.findByShopWhtinRadiusAll(dto.getX(),dto.getY());
        List<ResponseShopByAllDto> dtos = new ArrayList<>();
        for(Shop shop : findShop){
             Image image = imageRepository.findByRecentImage(shop.getId());
             if(image==null) dtos.add(ResponseShopByAllDto.set(shop,"등록된 사진이 없습니다."));
             else dtos.add(ResponseShopByAllDto.set(shop,image.getPath()));
             dtos.get(0);
        }
        return dtos;
    }
    @Transactional
    public JSONArray getShopBySortWithPaging(String sort, FindByShopByRadiusToSortDto dto){
        List<Shop> findShop;
        if(sort.equals("rating")){
             findShop= shopRepository.sortByShopWithinRadiusWithRating(dto.getX(),dto.getY());
        }
        else if(sort.equals("review")) findShop = shopRepository.findByShopWithinRadiusAndReviewCount(dto.getX(),dto.getY());
        else findShop = shopRepository.findByShopWithinRadiusAndFollowCount(dto.getX(),dto.getY());
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
               Image image = imageRepository.findByRecentImage(shops.get(i).getId());
               if (image ==null) {
                   Review review  = reviewRepository.findByRecentReview(shops.get(i).getId());
                   if(review==null){
                       ResponseShopByRadiusDto dto = ResponseShopByRadiusDto.set(shops.get(i), "등록된 리뷰가 없습니다.", "등록된 사진이 없습니다.");
                       map.put(String.valueOf(i + 1), dto);
                   }
                   else{
                       ResponseShopByRadiusDto dto = ResponseShopByRadiusDto.set(shops.get(i), review.getComment(),"등록된 사진이 없습니다.");
                       map.put(String.valueOf(i+1),dto);
                   }
               }
               else{
                   Optional<Review> review = reviewRepository.findById(image.getReview().getId());
                   ResponseShopByRadiusDto dto = ResponseShopByRadiusDto.set(shops.get(i), review.get().getComment(), image.getPath());
                   map.put(String.valueOf(i+1),dto);
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
