package Backend.MASJIB.repository;

import Backend.MASJIB.image.entity.Image;
import Backend.MASJIB.image.repository.ImageRepository;
import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.rating.repository.AssessmentRepository;
import Backend.MASJIB.rating.repository.RatingRepository;
import Backend.MASJIB.shop.entity.Shop;
import Backend.MASJIB.shop.repository.ShopRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.locationtech.proj4j.*;
import org.locationtech.proj4j.proj.Projection;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShopRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(ReviewRepositoryTest.class);
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ImageRepository imageRepository;
    @BeforeEach
    void setUp(){

        Rating rating = Rating.set();
        rating.setFive(3);
        rating.setHalf(2);
        rating.setCount(5);
        ratingRepository.save(rating);
        Assessment assessment = Assessment.set();
        assessment.setGoodTaste(3);
        assessmentRepository.save(assessment);

        Shop createShop1 = Shop.builder()
                .name("김해 돼지국밥 본점")
                .x(127.03110424141911)
                .y(37.49660481702947)
                .reviewCount(0)
                .status("영업")
                .address("경남 김해시 인제로 327")
                .kind("한식")
                .rating(rating)
                .assessment(assessment)
                .followCount(0)
                .build();

        shopRepository.save(createShop1);

        Rating rating1 = Rating.set();
        rating1.setFive(5);
        rating1.setHalf(1);
        rating1.setCount(6);
        ratingRepository.save(rating1);
        Assessment assessment1 = Assessment.set();
        assessment1.setGoodTaste(5);
        assessmentRepository.save(assessment1);

        Shop createShop2 = Shop.builder()
                .name("김해 공룡피자 본점")
                .x(12.123010)
                .y(89.123123)
                .reviewCount(0)
                .status("영업")
                .address("경남 김해시 인제로 14-3")
                .kind("양식")
                .rating(rating1)
                .followCount(0)
                .assessment(assessment1)
                .build();

        shopRepository.save(createShop2);
    }
    @Test
    @DisplayName("Shop update Test")
    void 가게_정보_테스트(){
        String findName = "김해 돼지국밥 본점";
        Optional<Shop> findShop = shopRepository.findByName(findName);
        findShop.orElseThrow(RuntimeException::new);

        String updateName = "김해 돼지국밥 삼방점";
        findShop.get().setName(updateName);

        shopRepository.save(findShop.get());
        assertThat(findShop.get().getName()).isEqualTo(updateName);
    }
    @Test
    @DisplayName("Shop AccumulateRating Update Test")
    void 가게_평점_테스트(){
        String findName = "김해 돼지국밥 본점";
        Optional<Shop> findShop = shopRepository.findByName(findName);
        findShop.orElseThrow(RuntimeException::new);

        double sum = Rating.CalculationRating(findShop.get().getRating());
        shopRepository.save(findShop.get());
        String result = String.format("%.2f",sum);

        logger.info("result : "+result);
    }
    /*@Test
    @DisplayName("Search For Shop Within a 1km Radius Based On Your Location Test")
    void 반경_1km내_음식점_조회_테스트(){
        Optional<Shop> findShopByAddress = shopRepository.findByAddressAndXAndY("경남 김해시 인제로",12.114001,89.11414);
        findShopByAddress.orElseThrow(RuntimeException::new);
    }*/
    @Test
    @DisplayName("Shop Within a 1km Radius In Order Of Taste Ratings Avg Test")
    void 반경_1km내_음식점_맛_별점_조회_테스트(){
        Page<Shop> findShopByRating = shopRepository.sortByShopWithinRadiusWithRating("경남 김해시 인제로",127.030619,37.496568, PageRequest.of(0,10));// 내림차순
    }
    @Test
    @DisplayName("Shop Within a 1km Radius In Order Of Taste Ratings Test")
    void 반경_1km내_음식점_맛_평가_조회_테스트(){
        List<Shop> findShopByRating = shopRepository.sortByShopWithinRadiusAndTasteAssess("경남 김해시 인제로",12.114001,89.11414); //내림차순
    }
    @Test
    @DisplayName("View Photos Included In a Shop Test")
    void 음식점_id_사진_조회_테스트(){
        Optional<Shop> findShop = shopRepository.findByName("김해 돼지국밥 본점");
        findShop.orElseThrow(RuntimeException::new);

        List<Image> findImages = imageRepository.findByShopId(findShop.get().getId());

    }
    @Test
    @DisplayName("Change Address to X,Y Using GeoCode Test")
    void 주소_좌표_변환_테스트() throws Exception{

       // CRS 객체 생성
        CRSFactory crsFactory = new CRSFactory();

        // WGS84 system 정의
        String wgs84Name = "WGS84";
        String wgs84Proj = "+proj=longlat +datum=WGS84 +no_defs";
        CoordinateReferenceSystem wgs84System = crsFactory.createFromParameters(wgs84Name, wgs84Proj);

        // UTMK system 정의
        String epsg2097Name = "ESPG:5174";
        String epsg2097Proj = "+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
        CoordinateReferenceSystem utmkSystem = crsFactory.createFromParameters(epsg2097Name, epsg2097Proj);

        // 변환할 좌표계 정보 생성
        ProjCoordinate p = new ProjCoordinate();
        p.x = 242924.59192413;
        p.y = 351685.948976672;

        // 변환된 좌표를 담을 객체 생성
        ProjCoordinate p2 = new ProjCoordinate();

        //CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        // 변환 시스템 지정. (원본 시스템, 변환 시스템)
        BasicCoordinateTransform coordinateTransform = new BasicCoordinateTransform(utmkSystem,wgs84System);
        // 좌표 변환
        //ProjCoordinate projCoordinate = coordinateTransform.transform(p, p2);
        coordinateTransform.transform(p, p2);
        // 변환된 좌표
        double x = p2.x;
        double y = p2.y;

        System.out.println(x+" , "+y);
        System.out.println(Math.abs(127.04653258456933-x));
        System.out.println(Math.abs(37.502974431241725-y));
        /*assertThat(x).isEqualTo(127.03161500261596);
        assertThat(y).isEqualTo(37.49665525639916);*/
    }

    //0.0007920188797 , 0.00837743122996

}
