package Backend.MASJIB.rating.entity;

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
    private long goodTaste;
    private long badTaste;
    private long goodHygiene;
    private long badHygiene;
    private long kindness;
    private long unkindness;

    public static Assessment set(){
        return Assessment.builder()
                .goodTaste(0L)
                .badTaste(0L)
                .goodHygiene(0L)
                .badHygiene(0L)
                .kindness(0L)
                .unkindness(0L)
                .build();
    }
}
