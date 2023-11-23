package Backend.MASJIB.controller;

import Backend.MASJIB.member.dto.CreateMemberDto;
import Backend.MASJIB.member.dto.ResponseMemberbyFindwithReviewDto;
import Backend.MASJIB.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;

@AutoConfigureRestDocs
@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // MemberController이 의존하는 빈을 모킹
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Member Create by Member Controller Using Post API")
    @Test
    void 멤버_컨트롤러_멤버_생성_테스트() throws Exception {
        // given
        /*given(memberService.findMemberById())
                .willReturn(new Member(1L,"NickName",new ArrayList<>()));*/
        String content = objectMapper.writeValueAsString(new CreateMemberDto("지우","test@test.com","포켓몬 마스터"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/member/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("name", "지우")
                        .param("email", "test@test.com")
                        .param("nickname", "포켓몬 마스터")
                        .content(content)
                        )
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print())
                        .andDo(MockMvcRestDocumentation.document(
                                "member/create",
                                Preprocessors.preprocessRequest(prettyPrint()),
                                Preprocessors.preprocessResponse(prettyPrint()),
                                requestFields(
                                        //fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디는 고유 값입니다.").attributes(key("Constraints").value("true")).optional(),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("멤버 이름을 지정합니다.").attributes(key("Constraints").value("false")),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임은 고유 값입니다.").attributes(key("Constraints").value("false")),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일은 고유 값입니다.").attributes(key("Constraints").value("false"))
                                        //fieldWithPath("createTime").type(JsonFieldType.STRING).description("멤버 가입 시간입니다.").attributes(key("Constraints").value("false")),
                                        //fieldWithPath("reviews").type(JsonFieldType.ARRAY).description("작성한 리뷰를 보여줍니다.").attributes(key("Constraints").value("true"),key("NULL").value("true")).optional()
                                )
                            )
                        );


        // when & then
       /* mockMvc.perform(MockMvcRequestBuilders.get("/api/member/test"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("members/test",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())

                ))
                .andExpect(MockMvcResultMatchers.status().isOk());*/
        /*responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("멤버의 고유 번호"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("멤버의 닉네임"),
                fieldWithPath("reviews").type(JsonFieldType.ARRAY).description("등록된 리뷰")
        )*/
    }

    @Test
    @DisplayName("MemberController Find Member By Member Id Test")
    void 멤버_컨트롤러_멤버_조회_테스트() throws Exception {

        given(memberService.findMemberById(1L))
                .willReturn(new ResponseMemberbyFindwithReviewDto(1L, "지우","test@test.com","포켓몬 마스터",new JSONArray()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/member/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "member/find",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("멤버의 고유 번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("멤버의 이름"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("멤버의 고유 닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("멤버의 고유 이메일"),
                                fieldWithPath("reviews").type(JsonFieldType.ARRAY).description("등록된 리뷰정보와 images")
                        )
                ));
    }
}
