package Backend.MASJIB.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Server Health Check API")
public class HealthCheckController {
    @GetMapping("/ping")
    @Operation(summary = "서버의 상태를 확인하는 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "pong"),
            @ApiResponse(responseCode = "500", description = "서버가 다운되어 있습니다.")
    })
    public ResponseEntity healthCheck(){
        try {
            return ResponseEntity.ok("pong");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("서버가 다운되어 있습니다.");
        }
    }
}
