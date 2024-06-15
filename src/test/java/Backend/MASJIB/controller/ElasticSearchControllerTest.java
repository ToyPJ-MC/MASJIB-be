package Backend.MASJIB.controller;

import Backend.MASJIB.es.document.Shop;
import Backend.MASJIB.es.service.ShopDocumentService;
import Backend.MASJIB.jwt.provider.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
@AutoConfigureRestDocs
@WebMvcTest(ElasticSearchController.class)
@AutoConfigureMockMvc
public class ElasticSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ShopDocumentService shopDocumentService;
    @MockBean
    private TokenProvider tokenProvider;
    @Test
    @DisplayName("Elastic Search Save Document Test")
    void Elastic_Search_Save_Test() throws Exception {
        doNothing().when(shopDocumentService).convertToDocument();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/es/shop").with(oauth2Login())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "es/save",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())
                ));


    }
    @Test
    @DisplayName("Elastic Search Find Document Test")
    void Elastic_Search_Find_Document_Test() throws Exception {
        Shop shop1 = new Shop().builder()
                .id(1L)
                .name("역전할머니맥주 강남점")
                .address("서울특별시 강남구 강남대로 100")
                .kind("호프/통닭")
                .build();

        Shop shop2 = new Shop().builder()
                .id(2L)
                .name("역전다방1436")
                .address("강원특별자치도 춘천시 신동면 김유정로 1436, 1층")
                .kind("기타")
                .build();

        List<Shop> shops = new ArrayList<>();
        shops.add(shop1);
        shops.add(shop2);

        given(shopDocumentService.SearchShop("역전")).willReturn(shops);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/es/shop").with(oauth2Login())
                .param("keyword","역전")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ) .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document(
                        "es/search",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("keyword").description("검색어")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("음식점 id"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("음식점 상호명"),
                                fieldWithPath("[].address").type(JsonFieldType.STRING).description("음식점 주소"),
                                fieldWithPath("[].kind").type(JsonFieldType.STRING).description("음식점 업종명")
                        )
                ));
    }
}
