package Backend.MASJIB.controller;

import Backend.MASJIB.config.SecurityConfig;
import Backend.MASJIB.jwt.provider.TokenProvider;
import Backend.MASJIB.member.dto.CreateMemberDto;
import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.shop.dto.CreateShopDto;
import Backend.MASJIB.shop.dto.FindByShopByRadiusToSortDto;
import Backend.MASJIB.shop.dto.ResponseShopByCreateDto;
import Backend.MASJIB.shop.dto.ResponseShopByRadiusDto;
import Backend.MASJIB.shop.service.ShopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@AutoConfigureRestDocs
@WebMvcTest(value = ShopController.class,
        excludeFilters =
                {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                                classes = SecurityConfig.class)}
)

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
            ResponseShopByRadiusDto dto=new ResponseShopByRadiusDto("파파이스 강남점","서울특별시 강남구 테헤란로",127.03110424141911,37.49660481702947,"","imags/1.jpg","코멘트",1L,1L,5.0,1L);
            object.put("1",dto);
            object.put("totalPage",1);
            array.add(object);

            given(shopService.getShopBySortWithPaging(eq("rating"),any())).willReturn(array);

        String sort = "rating";
        String content = objectMapper.writeValueAsString(new FindByShopByRadiusToSortDto("서울특별시 강남구 테헤란로",127.03110424141911,37.49660481702947,1));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/shop/radius")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("sort",sort)
                .content(content)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "shop/radius",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("address").type(JsonFieldType.STRING).description("음식점 주소"),
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
                                fieldWithPath("[].1.image").type(JsonFieldType.STRING).description("음식점 이미지"),
                                fieldWithPath("[].1.recentReview").type(JsonFieldType.STRING).description("음식점 코멘트"),
                                fieldWithPath("[].1.reviewCount").type(JsonFieldType.NUMBER).description("음식점 리뷰 수"),
                                fieldWithPath("[].1.followCount").type(JsonFieldType.NUMBER).description("음식점 팔로우 수"),
                                fieldWithPath("[].1.totalRating").type(JsonFieldType.NUMBER).description("음식점 평점"),
                                fieldWithPath("[].1.kind").type(JsonFieldType.STRING).description("음식점 종류"),
                                fieldWithPath("[].totalPage").type(JsonFieldType.NUMBER).description("총 페이지 수")
                        )
                ));

    }
}
