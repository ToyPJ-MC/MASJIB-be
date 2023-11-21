package Backend.MASJIB.shop.dto;

import Backend.MASJIB.member.dto.ResponseMemberByCreateDto;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.rating.entity.Assessment;
import Backend.MASJIB.rating.entity.Rating;
import Backend.MASJIB.shop.entity.Shop;
import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseShopByCreateDto {
    private long id;
    private String name;
    private Double x;
    private Double y;
    private String address;
    private String status;

    private long reviewCount;
    private long followCount;

    private String kind;
    private Rating rating;
    private Assessment assessment;

    public static ResponseShopByCreateDto set(Shop shop){
        ResponseShopByCreateDto createDto = new ResponseShopByCreateDto();
        createDto.setId(shop.getId());
        createDto.setAddress(shop.getAddress());
        createDto.setName(shop.getName());
        createDto.setReviewCount(shop.getReviewCount());
        createDto.setRating(shop.getRating());
        createDto.setStatus(shop.getStatus());
        createDto.setX(shop.getX());
        createDto.setY(shop.getY());
        createDto.setKind(shop.getKind());
        createDto.setAddress(shop.getAddress());
        return createDto;
    }
}
