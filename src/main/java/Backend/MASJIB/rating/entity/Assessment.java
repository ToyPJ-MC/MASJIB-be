package Backend.MASJIB.rating.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long goodTaste; //맛
    private long badTaste;
    private long goodHygiene; //위생
    private long badHygiene;
    private long kindness; //친절함
    private long unKindness;
    @JsonIgnore
    public static Assessment set(){
        return Assessment.builder()
                .goodTaste(0L)
                .badTaste(0L)
                .goodHygiene(0L)
                .badHygiene(0L)
                .kindness(0L)
                .unKindness(0L)
                .build();
    }
}
