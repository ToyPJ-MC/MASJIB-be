package Backend.MASJIB.repository;

import Backend.MASJIB.shop.entity.Shop;
import Backend.MASJIB.shop.repository.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShopRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(ReviewRepositoryTest.class);
    @Autowired
    private ShopRepository shopRepository;

    @BeforeEach
    void setUp(){
        Shop createShop1 = Shop.builder()
                .name("김해 돼지국밥 본점")
                .x(12.123010)
                .y(89.123123)
                .reviewCount(0)
                .status("영업")
                .address("경남 김해시 인제로 327")
                .kind("한식")
                .rating(new HashMap<>())
                .build();

        Shop createShop2 = Shop.builder()
                .name("김해 공룡피자 본점")
                .x(12.333010)
                .y(89.44423)
                .reviewCount(0)
                .status("영업")
                .address("경남 김해시 인제로 14-3")
                .kind("양식")
                .rating(new HashMap<>())
                .build();
        shopRepository.save(createShop1);
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

        findShop.get().getRating().put(3.5,3L);
        findShop.get().getRating().put(2.5,4L);
        double sum =0;
        long count=0;
        for(double key : findShop.get().getRating().keySet()){
            sum += key*findShop.get().getRating().get(key);
            count+= findShop.get().getRating().get(key);
        }
        shopRepository.save(findShop.get());
        String result = String.format("%.2f",sum/count);

        logger.info(result);
    }
    @Test
    @DisplayName("Search For Shop Within a 1km Radius Based On Your Location Test")
    void 반경_1km내_음식점_조회_테스트(){
        Optional<Shop> findShopByAddress = shopRepository.findByAddressAndXAndY("경남 김해시 인제로",12.114001,89.11414);
        findShopByAddress.orElseThrow(RuntimeException::new);
    }
    void 도로명_주소_조회_테스트(){

    }
}
