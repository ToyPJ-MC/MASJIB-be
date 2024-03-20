package Backend.MASJIB.image.repository;

import Backend.MASJIB.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> findByShopId(Long shopId);
    @Query("select i from Image i where i.shopId = :shopId order by i.createTime desc limit 1")
    Image findByImageWithShopId (@Param("shopId") Long shopId);
}
