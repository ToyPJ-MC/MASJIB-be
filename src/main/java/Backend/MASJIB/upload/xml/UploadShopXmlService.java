package Backend.MASJIB.upload.xml;

import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.shop.entity.Shop;
import Backend.MASJIB.shop.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Service
public class UploadShopXmlService {
    private final ShopRepository shopRepository;

    public UploadShopXmlService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public void upLoadShopXml(MultipartFile file){
        try{
            InputStream inputStream = file.getInputStream();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            // 임시 파일을 나타내는 File 객체를 만듭니다.
            File tempFile = File.createTempFile("temp", null);

            // MultipartFile의 내용을 임시 파일로 복사합니다.
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // XML 파일을 파싱하는 핸들러 생성
            MyHandler handler = new MyHandler();
            saxParser.parse(tempFile, handler);
        }catch (Exception e){

        }
    }

    private class MyHandler extends DefaultHandler {
        String name = "";
        String address = "";
        String x = "";
        String y = "";
        String status = "";
        String element = "";
        String category = "";

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
                if (element.equals("x")) x = textData;
                else if (element.equals("y")) y = textData;
                else if (element.equals("bplcNm")) name = textData;
                else if (element.equals("rdnWhlAddr")) address = textData;
                else if (element.equals("dtlStateNm")) status = textData;
                else if (element.equals("uptaeNm")) category = textData;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            // 종료 태그를 만났을 때 실행되는 코드
            if (qName.equals("row") && !status.equals("폐업")) {
                Shop shop = Shop.builder()
                        .name(name)
                        .address(address)
                        .kind(category)
                        .x(Double.valueOf(x))
                        .y(Double.valueOf(y))
                        .reviewCount(0)
                        .rating(Rating.set())
                        .status(status)
                        .assessment(Assessment.set())
                        .build();
                shopRepository.save(shop);
            }
        }

    }
}
