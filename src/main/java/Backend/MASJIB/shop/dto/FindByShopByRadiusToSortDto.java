package Backend.MASJIB.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.springframework.context.annotation.Description;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindByShopByRadiusToSortDto {
    @NotBlank(message = "현재 x좌표를 입력해주세요")
    private double x;
    @NotBlank(message = "현재 y좌표를 입력해주세요")
    private double y;
    @NotBlank(message = "페이지 번호 입력해주세요")
    private int page;

}
