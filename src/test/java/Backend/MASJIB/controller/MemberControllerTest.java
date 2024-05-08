package Backend.MASJIB.controller;

import Backend.MASJIB.jwt.provider.TokenProvider;

import Backend.MASJIB.member.dto.ResponseMemberbyFindwithReviewDto;
import Backend.MASJIB.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
@AutoConfigureRestDocs
@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc
public class MemberControllerTest{

    @Autowired
    private MockMvc mockMvc;
    @MockBean // MemberController이 의존하는 빈을 모킹
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TokenProvider tokenProvider;

    @Test
    @DisplayName("MemberController Find Member By Member Id Test")
    void 멤버_컨트롤러_멤버_조회_테스트() throws Exception {

        given(memberService.findMemberById(any()))
                .willReturn(new ResponseMemberbyFindwithReviewDto(15L, "지우","test@test.com","@user-a1da23",new JSONArray()));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/members/{id}",15).with(oauth2Login())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "member/find",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("멤버의 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("멤버의 고유 번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("멤버의 이름"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("멤버의 고유 닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("멤버의 고유 이메일"),
                                fieldWithPath("reviews").type(JsonFieldType.ARRAY).description("등록된 리뷰정보와 images")
                        )
                ));
    }
    @Test
    @DisplayName("멤버의 닉네임을 e-mail를 통해 조회 테스트")
    void 멤버_닉네임_조회_테스트() throws Exception{
        given(memberService.findNickNameByEmail(any())).willReturn("@user-9dd33e6");

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/members/info").with(oauth2Login())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "member/info",
                        Preprocessors.preprocessResponse(prettyPrint())
                ));
    }
    @Test
    @DisplayName("멤버의 고유 닉네임 변경을 성공하는 테스트")
    void 멤버_닉네임_변경_성공_테스트() throws Exception{
        given(memberService.modifyMemberNickName(any(),any())).willReturn(true);
        String nickName = "엄마커서랄로가될래요";

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/{nickname}",nickName).with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "member/nickname/modify/success",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("nickname").description("변경하고 싶은 닉네임")
                        ),responseBody()
                ));
    }

    @Test
    @DisplayName("멤버의 고유 닉네임 변경을 실패하는 테스트")
    void 멤버_닉네임_변경_실패_테스트() throws Exception{
        given(memberService.modifyMemberNickName(any(),any())).willReturn(false);
        String nickName = "엄마커서랄로가될래요";

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/{nickname}",nickName).with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "member/nickname/modify/failure",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("nickname").description("변경하고 싶은 닉네임")
                        ),responseBody()
                ));
    }
}
