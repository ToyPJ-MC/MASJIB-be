package Backend.MASJIB.shop.repository;

import Backend.MASJIB.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop,Long> {
    Optional<Shop> findByName (String name);
    boolean existsByAddress (String address);

    @Query("SELECT s FROM Shop s WHERE " +
            "6371 * acos(cos(radians(:myY)) * cos(radians(s.y)) * cos(radians(s.x) - radians(:myX)) + " +
            "sin(radians(:myY)) * sin(radians(s.y))) < 1 AND s.address LIKE %:address%")
    Optional<Shop> findByAddressAndXAndY (@Param("address") String address, @Param("myX") double myX, @Param("myY") double myY);

}
