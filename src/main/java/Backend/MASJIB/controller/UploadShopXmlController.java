package Backend.MASJIB.controller;

import Backend.MASJIB.upload.xml.UploadShopXmlService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UploadShopXmlController {

    private final UploadShopXmlService uploadShopXml;

    public UploadShopXmlController(UploadShopXmlService uploadShopXml) {
        this.uploadShopXml = uploadShopXml;
    }
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "음식점 xml 업로드 API")
    public ResponseEntity uploadShopXml(@RequestPart MultipartFile file){
        try{
            uploadShopXml.upLoadShopXml(file);
            return ResponseEntity.ok().body("성공");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
