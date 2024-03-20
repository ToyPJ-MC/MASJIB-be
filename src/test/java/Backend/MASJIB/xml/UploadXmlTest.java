package Backend.MASJIB.xml;

import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.shop.entity.Shop;
import Backend.MASJIB.shop.repository.ShopRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UploadXmlTest {
   /* @Autowired
    private ShopRepository shopRepository;

    @Test
    void xml_파싱_테스트()  {
        String path = ""; ///Users/moon/Downloads/fulldata_07_24_04_P_일반음식점.xml

        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            // XML 파일을 파싱하는 핸들러 생성
            MyHandler handler = new MyHandler();
            saxParser.parse(path, handler);
        }catch (Exception e){

        }

    }
    private class MyHandler extends DefaultHandler {
        String name ="";
        String address ="";
        String x ="";
        String y ="";
        String status ="";
        String element="";
        String category="";

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            // 시작 태그를 만났을 때 실행되는 코드
            element = qName;
            // System.out.println("Element: " + qName);
        }
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            // 텍스트 데이터를 만났을 때 실행되는 코드
            String textData = new String(ch, start, length).trim();
            if (!textData.isEmpty()) {
                if(element.equals("x")) x=textData;
                else if(element.equals("y"))y=textData;
                else if(element.equals("bplcNm"))name=textData;
                else if(element.equals("rdnWhlAddr"))address=textData;
                else if(element.equals("dtlStateNm"))status=textData;
                else if(element.equals("uptaeNm"))category=textData;
            }
        }
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            // 종료 태그를 만났을 때 실행되는 코드
            if(qName.equals("row") && !status.equals("폐업")){
                Shop shop = Shop.builder()
                        .name(name)
                        .address(address)
                        .kind(category)
                        .x(Double.valueOf(x))
                        .y(Double.valueOf(y))
                        .reviewCount(0)
                        .rating(Rating.set())
                        .assessment(Assessment.set())
                        .status(status)
                        .build();
                shopRepository.save(shop);

            }
        }
    }*/
}
