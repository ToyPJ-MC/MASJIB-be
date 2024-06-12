package Backend.MASJIB.upload.xml;

import Backend.MASJIB.es.repository.ShopDocumentRepository;
import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.rating.repository.AssessmentRepository;
import Backend.MASJIB.rating.repository.RatingRepository;
import Backend.MASJIB.shop.entity.Shop;
import Backend.MASJIB.shop.repository.ShopRepository;
import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class UploadShopXmlService {
    private final ShopRepository shopRepository;
    private final AssessmentRepository assessmentRepository;
    private final RatingRepository ratingRepository;
    @Autowired
    public UploadShopXmlService(ShopRepository shopRepository, AssessmentRepository assessmentRepository, RatingRepository ratingRepository) {
        this.shopRepository = shopRepository;
        this.assessmentRepository = assessmentRepository;
        this.ratingRepository = ratingRepository;
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
                List<Double> result = changeESPG_5174(Double.valueOf(x),Double.valueOf(y));
                Rating rating =Rating.set();
                Assessment assessment = Assessment.set();
                Shop shop = Shop.builder()
                        .name(name)
                        .address(address)
                        .kind(category)
                        .x(result.get(0))
                        .y(result.get(1))
                        .reviewCount(0)
                        .rating(rating)
                        .status(status)
                        .assessment(assessment)
                        .build();
                assessmentRepository.save(assessment);
                ratingRepository.save(rating);
                shopRepository.save(shop);
            }
        }
        private List<Double> changeESPG_5174(Double x, Double y){
            List<Double> result = new ArrayList<>();

            CRSFactory crsFactory = new CRSFactory();

            // WGS84 system 정의
            String wgs84Name = "WGS84";
            String wgs84Proj = "+proj=longlat +datum=WGS84 +no_defs";
            CoordinateReferenceSystem wgs84System = crsFactory.createFromParameters(wgs84Name, wgs84Proj);

            // UTMK system 정의
            String epsg2097Name = "ESPG:5174";
            String epsg2097Proj = "+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
            CoordinateReferenceSystem utmkSystem = crsFactory.createFromParameters(epsg2097Name, epsg2097Proj);

            ProjCoordinate p = new ProjCoordinate();
            p.x = x;
            p.y = y;

            ProjCoordinate p2 = new ProjCoordinate();
            BasicCoordinateTransform coordinateTransform = new BasicCoordinateTransform(utmkSystem,wgs84System);

            coordinateTransform.transform(p, p2);

            result.add(p2.x);
            result.add(p2.y);
            return result;
        }
    }
}
