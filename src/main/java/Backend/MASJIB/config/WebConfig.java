package Backend.MASJIB.config;

import Backend.MASJIB.filter.MDCLoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")
    private String uploadPath;
    @Bean
    public MDCLoggingFilter mdcLoggingFilter(){
        return new MDCLoggingFilter();
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") //스프링 2.4.0 이상부터 allowCredentials를 사용하려면 allowOrigins에 "*"를 사용할 수 없게됨
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedMethods("*").maxAge(3600);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {   // 기본 컨버터를 유지관리
        converters.removeIf(v->v.getSupportedMediaTypes().contains(MediaType.MULTIPART_FORM_DATA_VALUE));  // 기존 json용 컨버터 제거
        converters.add(new MappingJackson2HttpMessageConverter());  // 새로 json 컨버터 추가. 필요시 커스텀 컨버터 bean 사용
    }
}
