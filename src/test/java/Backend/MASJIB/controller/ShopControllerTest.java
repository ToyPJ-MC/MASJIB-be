package Backend.MASJIB.controller;

import Backend.MASJIB.config.SecurityConfig;
import Backend.MASJIB.jwt.provider.TokenProvider;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.member.entity.Role;
import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.review.entity.Review;
import Backend.MASJIB.shop.dto.*;
import Backend.MASJIB.shop.entity.Shop;
import Backend.MASJIB.shop.service.ShopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;

@AutoConfigureRestDocs
@WebMvcTest(value = ShopController.class,
        excludeFilters =
                {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                                classes = SecurityConfig.class)}
)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ShopService shopService;
    @MockBean
    private TokenProvider tokenProvider;

    @DisplayName("Find Shop Within a 1KM Radius Of The Shop API")
    @WithMockUser
    @Test
    void 음식점_반경_1km_내_조회()throws Exception {
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        ResponseShopByRadiusDto dto=new ResponseShopByRadiusDto(1L,"된장찌개 강남점","서울특별시 강남구 테헤란로",127.03110424141911,37.49660481702947,"한식","imags/1.jpg",5.0,1L,1L,"코멘트");
        object.put("1",dto);
        object.put("totalPage",1);
        array.add(object);

        given(shopService.getShopBySortWithPaging(eq("rating"),any())).willReturn(array);

        String sort = "rating";
        String content = objectMapper.writeValueAsString(new FindByShopByRadiusToSortDto(127.03110424141911,37.49660481702947,1));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/shop/radius")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("sort",sort)
                .content(content)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "shop/radius/sort",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        queryParameters(
                          parameterWithName("sort").description("정렬 기준")
                        ),
                        requestFields(
                                fieldWithPath("x").type(JsonFieldType.NUMBER).description("음식점 경도"),
                                fieldWithPath("y").type(JsonFieldType.NUMBER).description("음식점 위도"),
                                fieldWithPath("page").type(JsonFieldType.NUMBER).description("페이지")
                        ),
                        responseFields(
                                fieldWithPath("[].1.shopId").type(JsonFieldType.NUMBER).description("음식점 고유 번호"),
                                fieldWithPath("[].1.name").type(JsonFieldType.STRING).description("음식점 이름"),
                                fieldWithPath("[].1.address").type(JsonFieldType.STRING).description("음식점 주소"),
                                fieldWithPath("[].1.x").type(JsonFieldType.NUMBER).description("음식점 경도"),
                                fieldWithPath("[].1.y").type(JsonFieldType.NUMBER).description("음식점 위도"),
                                fieldWithPath("[].1.kind").type(JsonFieldType.STRING).description("업종 구분"),
                                fieldWithPath("[].1.image").type(JsonFieldType.STRING).description("음식점 이미지"),
                                fieldWithPath("[].1.recentReview").type(JsonFieldType.STRING).description("음식점 코멘트"),
                                fieldWithPath("[].1.totalRating").type(JsonFieldType.NUMBER).description("음식점 평점"),
                                fieldWithPath("[].1.followCount").type(JsonFieldType.NUMBER).description("음식점 팔로우 수"),
                                fieldWithPath("[].1.reviewCount").type(JsonFieldType.NUMBER).description("음식점 리뷰 수"),
                                fieldWithPath("[].totalPage").type(JsonFieldType.NUMBER).description("총 페이지 수")
                        )
                ));
    }
    @Test
    @DisplayName("Find All Shop Within a 1KM Radius Of The Shop API")
    @WithMockUser
    void 음식점_반경_1km_내_전체_조회()throws Exception {
        List<ResponseShopByAllDto> list = new ArrayList<>();
        ResponseShopByAllDto dto=new ResponseShopByAllDto(1L,"된장찌개 강남점","서울특별시 강남구 테헤란로 117",127.03110424141911,37.49660481702947,"한식","images/i.jpg",3.78);
        ResponseShopByAllDto dto1 = new ResponseShopByAllDto(2L,"김치찌개 강남점","서울특별시 강남구 테헤란로 118",127.03110424141911,37.49660481702947,"한식","imags/2.jpg",4.0);
        list.add(dto);
        list.add(dto1);

        given(shopService.getShopByRadiusAll(any())).willReturn(list);

        String content = objectMapper.writeValueAsString(new FindByShopByRadiusAllDto(127.03110424141911,37.49660481702947));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/shop/radius/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "shop/radius/all",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("x").type(JsonFieldType.NUMBER).description("음식점 경도"),
                                fieldWithPath("y").type(JsonFieldType.NUMBER).description("음식점 위도")
                        ),
                        responseFields(
                                fieldWithPath("[].shopId").type(JsonFieldType.NUMBER).description("음식점 고유 번호"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("음식점 이름"),
                                fieldWithPath("[].address").type(JsonFieldType.STRING).description("음식점 주소"),
                                fieldWithPath("[].x").type(JsonFieldType.NUMBER).description("음식점 경도"),
                                fieldWithPath("[].y").type(JsonFieldType.NUMBER).description("음식점 위도"),
                                fieldWithPath("[].kind").type(JsonFieldType.STRING).description("업종 구분"),
                                fieldWithPath("[].image").type(JsonFieldType.STRING).description("음식점 이미지"),
                                fieldWithPath("[].totalRating").type(JsonFieldType.NUMBER).description("음식점 평점")
                        )
                ));
    }
    @Test
    @DisplayName("Request to add Shop image API")
    void 가게_이미지_추가요청_테스트() throws Exception {
        List<String> imagePath = new ArrayList<>();
        imagePath.add("images/member_id-UUID.확장자(jpg)");
        imagePath.add("images/member_id-UUID.확장자(png)");
        imagePath.add("images/member_id-UUID.확장자(png)");
        imagePath.add("images/member_id-UUID.확장자(png)");
        imagePath.add("images/member_id-UUID.확장자(png)");
        given(shopService.getShopImages(1L,1)).willReturn(imagePath);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/shop/{shopId}/images",1L).with(oauth2Login())
                .param("page","1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "shop/images",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("shopId").description("음식점 id")
                        ),
                        queryParameters(
                                parameterWithName("page").description("1부터 ~ ")
                        ),
                        responseBody()
                ));
    }

    @Test
    @DisplayName("Find Shop detailed inquiry API")
    void 음식점_세부_조회_테스트() throws Exception{
        JSONArray array = new JSONArray();
        Rating rating = Rating.builder()
                .id(1L)
                .count(2)
                .five(1)
                .fourHalf(0)
                .four(1)
                .threeHalf(0)
                .three(0)
                .twoHalf(0)
                .two(0)
                .oneHalf(0)
                .one(0)
                .half(0)
                .zero(0)
                .build();

        Assessment assessment = Assessment.builder()
                .id(1L)
                .goodTaste(1)
                .badTaste(1)
                .goodHygiene(1)
                .badHygiene(1)
                .kindness(2)
                .unKindness(0)
                .build();

        Shop shop = Shop.builder()
                    .id(1L)
                    .name("된장찌개 강남점")
                    .x(127.03110424141911)
                    .y(37.49660481702947)
                    .address("서울특별시 강남구 테헤란로 117")
                    .rating(rating)
                    .assessment(assessment)
                    .status("한식")
                    .followCount(0)
                    .reviewCount(2)
                    .build();
        Member member = Member.builder()
                .id(1L)
                .shops(new ArrayList<>())
                .createTime(LocalDateTime.now())
                .reviews(new ArrayList<>())
                .role(Role.ROLE_USER)
                .email("user@user.com")
                .nickname("서울시정복")
                .build();

        Review review = Review.builder()
                .shop(shop)
                .comment("맛이 좋아요")
                .createTime(LocalDateTime.now())
                .taste("goodTaste")
                .hygiene("goodHygiene")
                .kindness("kindness")
                .images(null)
                .member(member)
                .rating(5.0)
                .build();
        Review review2 = Review.builder()
                .shop(shop)
                .comment("맛이 좀 별로에요")
                .createTime(LocalDateTime.now())
                .taste("badTaste")
                .hygiene("badHygiene")
                .kindness("kindness")
                .images(null)
                .member(member)
                .rating(4.0)
                .build();
        JSONArray sortReviews = new JSONArray();
        JSONObject obj1 = new JSONObject();
        obj1.put("review",review);
        obj1.put("imagePath",new ArrayList<>());
        JSONObject obj2 = new JSONObject();
        obj2.put("review",review2);
        obj2.put("imagePath",new ArrayList<>());
        sortReviews.add(obj1);
        sortReviews.add(obj2);

        JSONObject totalPage = new JSONObject();
        totalPage.put("totalPage",1);
        JSONObject totalRating = new JSONObject();
        totalRating.put("totalRating",4.5);
        array.add(shop);
        JSONObject images = new JSONObject();
        images.put("shop_images",new ArrayList<>());
        array.add(images);
        array.add(sortReviews);
        array.add(totalPage);
        array.add(totalRating);
        given(shopService.getShopDetailsWithReviewsOrderBySorting(1L,"highestRated","based",1)).willReturn(array);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/shop/{shopId}",1L).with(oauth2Login())
                .param("sortType","highestRated")
                .param("reviewType","based")
                .param("page","1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "shop/detail",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("shopId").description("음식점 id")
                        ),
                        queryParameters(
                                parameterWithName("sortType").description("정렬 타입"),
                                parameterWithName("reviewType").description("리뷰 타입"),
                                parameterWithName("page").description("페이지 번호")
                        ),
                        responseBody()
                ));
    }

}
