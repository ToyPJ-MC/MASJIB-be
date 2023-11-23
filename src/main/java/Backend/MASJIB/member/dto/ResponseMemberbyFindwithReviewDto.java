package Backend.MASJIB.member.dto;

import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.review.entity.Review;
import lombok.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMemberbyFindwithReviewDto {

    private Long id;
    private String name;
    private String email;
    private String nickName;
    private JSONArray reviews;

    public static ResponseMemberbyFindwithReviewDto set(Member member){
        ResponseMemberbyFindwithReviewDto createDto = new ResponseMemberbyFindwithReviewDto();
        createDto.setId(member.getId());
        createDto.setName(member.getName());
        createDto.setNickName(member.getNickname());
        createDto.setEmail(member.getEmail());
        createDto.setReviews(setReviewWithImage(member.getReviews()));


        return createDto;
    }
    private static JSONArray setReviewWithImage(List<Review> review){
        JSONArray jsonArray = new JSONArray();
        for(int j=0;j<review.size();j++){
            Map<String,String> map = new HashMap<>();
            JSONObject object = new JSONObject();
            object.put("comment",review.get(j).getComment());
            object.put("createTime",review.get(j).getCreateTime());
            object.put("rating",review.get(j).getRating());
            object.put("taste",review.get(j).getTaste());
            object.put("hygiene",review.get(j).getHygiene());
            object.put("kindness",review.get(j).getKindness());
            object.put("shopName",review.get(j).getShop().getName());
            object.put("shopId",review.get(j).getShop().getId());
            for(int i=0;i<review.get(j).getImages().size();i++){
                map.put(String.valueOf(i+1),review.get(j).getImages().get(i).getPath());
            }
            object.put("images",map);
            jsonArray.add(object);
        }
        return jsonArray;
    }
}
