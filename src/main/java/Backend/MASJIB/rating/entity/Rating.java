package Backend.MASJIB.rating.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long five;
    private long fourHalf;
    private long four;
    private long threeHalf;
    private long three;
    private long twoHalf;
    private long two;
    private long oneHalf;
    private long one;
    private long half;
    private long zero;
    private long count;
    @JsonIgnore
    public static Rating set(){
        return  Rating.builder()
                .five(0L)
                .fourHalf(0L)
                .four(0L)
                .threeHalf(0L)
                .three(0L)
                .twoHalf(0L)
                .two(0L)
                .oneHalf(0L)
                .one(0L)
                .half(0L)
                .zero(0L)
                .count(0L)
                .build();
    }
    @JsonIgnore
    public static Double CalculationRating(Rating rating){
        Double result = 0.0;
        result = (rating.getFive()*5.0+rating.getFourHalf()*4.5+rating.getFour()*4.0+rating.getThreeHalf()*3.5
                +rating.getThree()*3.0+rating.getTwoHalf()*2.5+rating.getTwo()*2.0+rating.getOneHalf()*1.5+rating.getOne()*1.0
                +rating.getHalf()*0.5+rating.getZero()*0.0)/rating.getCount();

        BigDecimal bd = new BigDecimal(result).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    @JsonIgnore
    public Double getRating(){
        Double result = 0.0;
        result = (this.getFive()*5.0+this.getFourHalf()*4.5+this.getFour()*4.0+this.getThreeHalf()*3.5
                +this.getThree()*3.0+this.getTwoHalf()*2.5+this.getTwo()*2.0+this.getOneHalf()*1.5+this.getOne()*1.0
                +this.getHalf()*0.5+this.getZero()*0.0)/this.getCount();

        BigDecimal bd = new BigDecimal(result).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
