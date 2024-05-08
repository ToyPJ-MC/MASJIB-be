package Backend.MASJIB.controller;

import Backend.MASJIB.jwt.provider.TokenProvider;
import Backend.MASJIB.review.dto.CreateReviewDto;
import Backend.MASJIB.review.dto.ResponseReviewByCreateDto;
import Backend.MASJIB.review.dto.ReviewListDto;
import Backend.MASJIB.review.service.ReviewService;
import Backend.MASJIB.shop.dto.FindByShopByRadiusAllDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.restdocs.request.RequestDocumentation.*;
@AutoConfigureRestDocs
@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReviewService reviewService;
    @MockBean
    private TokenProvider tokenProvider;

    @Test
    @DisplayName("Create Review by Using CreateReviewDto API")
    void 리뷰_생성_테스트()throws Exception{
        given(reviewService.createReview(any(),any())).willReturn(new ResponseReviewByCreateDto(1L,"음식이 맛있습니다.",1,3.5,"goodTaste","goodHygiene","kindness", LocalDateTime.now(),new ArrayList<>()));

        String content = objectMapper.writeValueAsString(new CreateReviewDto("음식이 맛있습니다.",1,3.5,"goodTaste","goodHygiene","kindness",new ArrayList<>()));

        MockMultipartFile notice = new MockMultipartFile("reviewDto", "reviewDto", "multipart/form-data", content.getBytes(StandardCharsets.UTF_8));

        MockMultipartHttpServletRequestBuilder mockMultipartHttpServletRequestBuilder = MockMvcRequestBuilders.multipart("/api/review");
        mockMvc.perform(mockMultipartHttpServletRequestBuilder.file(notice).with(oauth2Login()).with(csrf())// with(csrf())를 추가해야 csrf 토큰을 넣어준다
                        .characterEncoding("UTF-8")
                        .content(content)
                        .param("reviewDto",notice.getBytes().toString())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                                "review/create",
                                Preprocessors.preprocessRequest(prettyPrint()),
                                Preprocessors.preprocessResponse(prettyPrint()),
                                requestParts(
                                        partWithName("reviewDto").description("리뷰 정보")
                                ),
                                requestFields(
                                        fieldWithPath("shopId").type(JsonFieldType.NUMBER).description("가게 아이디를 입력합니다.").attributes(key("Constraints").value("false")),
                                        fieldWithPath("comment").type(JsonFieldType.STRING).description("리뷰 내용을 입력합니다.").attributes(key("Constraints").value("false")),
                                        fieldWithPath("rating").type(JsonFieldType.NUMBER).description("평점을 입력합니다.").attributes(key("Constraints").value("false")),
                                        fieldWithPath("taste").type(JsonFieldType.STRING).description("맛을 입력합니다.").attributes(key("Constraints").value("false")),
                                        fieldWithPath("hygiene").type(JsonFieldType.STRING).description("친절함을 입력합니다.").attributes(key("Constraints").value("false")),
                                        fieldWithPath("kindness").type(JsonFieldType.STRING).description("위생도를 입력합니다.").attributes(key("Constraints").value("false")),
                                        fieldWithPath("files").type(JsonFieldType.ARRAY).description("사진을 첨부합니다.").attributes(key("Constraints").value("false")).optional()
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("리뷰의 고유 번호"),
                                        fieldWithPath("comment").type(JsonFieldType.STRING).description("리뷰 텍스트"),
                                        fieldWithPath("shopId").type(JsonFieldType.NUMBER).description("음식점 고유 번호"),
                                        fieldWithPath("rating").type(JsonFieldType.NUMBER).description("평점"),
                                        fieldWithPath("taste").type(JsonFieldType.STRING).description("goodTaste or badTaste"),
                                        fieldWithPath("hygiene").type(JsonFieldType.STRING).description("goodHygiene or badHygiene"),
                                        fieldWithPath("kindness").type(JsonFieldType.STRING).description("kindness or unKindness"),
                                        fieldWithPath("paths").type(JsonFieldType.ARRAY).description("사진"),
                                        fieldWithPath("createTime").type(JsonFieldType.STRING).description("리뷰 작성 시간")
                                )
                        )
                );

    }
    @Test
    @DisplayName("Check the user's registerd reivew API")
    void 멤버별_리뷰_조회_테스트() throws Exception {
        List<ReviewListDto> reviewListDtos = new ArrayList<>();
        ReviewListDto dto = new ReviewListDto(1L,"해장하면 역시 이집 !",37,"해장국 전문점",4.5,"goodTaste","badHygiene","kindness",LocalDateTime.now(),new ArrayList<>());
        ReviewListDto dto1 = new ReviewListDto(3L,"생맥주가 아주 굿이에요",45,"눈물비어?!",4.5,"goodTaste","goodHygiene","kindness",LocalDateTime.now(),new ArrayList<>());
        reviewListDtos.add(dto);
        reviewListDtos.add(dto1);
        given(reviewService.getReviews(any())).willReturn(reviewListDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/review").with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "review/list",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].reviewId").type(JsonFieldType.NUMBER).description("리뷰 고유 번호"),
                                fieldWithPath("[].comment").type(JsonFieldType.STRING).description("리뷰 코멘트"),
                                fieldWithPath("[].shopId").type(JsonFieldType.NUMBER).description("음식점 고유 번호"),
                                fieldWithPath("[].shopName").type(JsonFieldType.STRING).description("음식점 이름"),
                                fieldWithPath("[].rating").type(JsonFieldType.NUMBER).description("평점"),
                                fieldWithPath("[].taste").type(JsonFieldType.STRING).description("goodTaste or badTaste"),
                                fieldWithPath("[].hygiene").type(JsonFieldType.STRING).description("goodHygiene or badHygiene"),
                                fieldWithPath("[].kindness").type(JsonFieldType.STRING).description("kindness or unKindness"),
                                fieldWithPath("[].paths").type(JsonFieldType.ARRAY).description("사진"),
                                fieldWithPath("[].createTime").type(JsonFieldType.STRING).description("리뷰 작성 시간")
                        )
                ));
    }

}
