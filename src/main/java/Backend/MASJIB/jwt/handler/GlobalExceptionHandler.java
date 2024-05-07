package Backend.MASJIB.jwt.handler;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RestController
public class GlobalExceptionHandler {
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity handleJwtExpiredException(ExpiredJwtException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("토큰이 만료되었습니다.");
    }
}
