package Backend.MASJIB.rating.repository;

import Backend.MASJIB.rating.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment,Long> {
}
