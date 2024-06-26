package Backend.MASJIB.controller;

import Backend.MASJIB.member.dto.CreateMemberDto;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.member.repository.MemberRepository;
import Backend.MASJIB.review.dto.CreateReviewDto;
import Backend.MASJIB.review.service.ReviewService;
import Backend.MASJIB.shop.entity.Shop;
import Backend.MASJIB.shop.repository.ShopRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

@AutoConfigureRestDocs
@WebMvcTest(ReviewController.class)
@ExtendWith(SpringExtension.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReviewService reviewService;

    @Test
    @DisplayName("Review Create by Review Controller Using CreateReviewDto API")
    void 리뷰_컨트롤러_생성_테스트()throws Exception{

        String content = objectMapper.writeValueAsString(new CreateReviewDto("string",1,1,3.5,new ArrayList<>()));
        MockMultipartFile notice = new MockMultipartFile("reviewDto", "reviewDto", "multipart/form-data", content.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/post")
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .characterEncoding("UTF-8")
                        .content(content)
                        .param("revieDto",notice.getBytes().toString())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                                "review/create",
                                Preprocessors.preprocessRequest(prettyPrint()),
                                Preprocessors.preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("comment").type(JsonFieldType.STRING).description("리뷰 내용을 입력합니다.").attributes(key("Constraints").value("false")),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("멤버 아이디를 입력합니다.").attributes(key("Constraints").value("false")),
                                        fieldWithPath("shopId").type(JsonFieldType.NUMBER).description("가게 아이디를 입력합니다.").attributes(key("Constraints").value("false")),
                                        fieldWithPath("rating").type(JsonFieldType.NUMBER).description("평점을 입력합니다.").attributes(key("Constraints").value("false")),
                                        fieldWithPath("files").type(JsonFieldType.ARRAY).description("사진을 첨부합니다.").attributes(key("Constraints").value("false")).optional()
                                )
                        )
                );

    }
}
