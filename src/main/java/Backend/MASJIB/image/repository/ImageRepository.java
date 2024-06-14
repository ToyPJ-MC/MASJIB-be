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
    @Query("select i from Image i where i.shopId = :shopId order by i.createTime desc limit 5") //최근 생성 시간 기준으로 리뷰 사진 5개를 가져옴
    List<Image> findByImageWithShopIdAndLimitFive(@Param("shopId") long shopId);

    @Query("select i from Image i where i.shopId = :shopId order by i.createTime desc") //최근 생성 시간 기준으로 리뷰 사진 가져옴
    List<Image> findByAllImageWithShopId(@Param("shopId") long shopId);

    @Query("select i from Image i order by i.createTime desc limit 1")
    Image findByRecentImage();
}
