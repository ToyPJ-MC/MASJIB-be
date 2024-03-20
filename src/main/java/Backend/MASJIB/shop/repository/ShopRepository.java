package Backend.MASJIB.shop.repository;

import Backend.MASJIB.shop.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop,Long> {
    Optional<Shop> findByName (String name);
    boolean existsByAddress (String address);

    @Query("SELECT s FROM Shop s WHERE " +
            "6371 * acos(cos(radians(:myY)) * cos(radians(s.y)) * cos(radians(s.x) - radians(:myX)) + " +
            "sin(radians(:myY)) * sin(radians(s.y))) < 1 AND s.address LIKE %:address%")
    Optional<Shop> findByAddressAndXAndY (@Param("address") String address, @Param("myX") double myX, @Param("myY") double myY);

    @Query(value = "select s," +
            "trunc(sum(s.rating.five*5.0+s.rating.fourHalf*4.5+s.rating.four*4.0+s.rating.threeHalf*3.5" +
            "+s.rating.three*3.0+s.rating.threeHalf*2.5+s.rating.two*2.0+s.rating.oneHalf*1.5+s.rating.one*1.0+" +
            "s.rating.half*0.5)/count(s.rating.count),2) as accessValue from Shop s where 6371 * acos(cos(radians(:myY)) * cos(radians(s.y)) * cos(radians(s.x) - radians(:myX)) +" +
            "sin(radians(:myY)) * sin(radians(s.y))) < 1 AND s.address LIKE %:address% " +
            "group by s.id order by accessValue desc, s.rating.count desc")
    List<Shop> sortByShopWithinRadiusWithRating(@Param("address") String address, @Param("myX") double myX, @Param("myY") double myY);

    @Query("SELECT s, s.assessment.goodTaste as assess FROM Shop s WHERE " +
            "6371 * acos(cos(radians(:myY)) * cos(radians(s.y)) * cos(radians(s.x) - radians(:myX)) + " +
            "sin(radians(:myY)) * sin(radians(s.y))) < 1 AND s.address LIKE %:address% group by s.id order by assess desc, s.name") //GoodTaste 순 정렬
    List<Shop> sortByShopWithinRadiusAndTasteAssess(@Param("address") String address, @Param("myX") double myX, @Param("myY") double myY);

    @Query("SELECT s FROM Shop s WHERE " +
            "6371 * acos(cos(radians(:myY)) * cos(radians(s.y)) * cos(radians(s.x) - radians(:myX)) + " +
            "sin(radians(:myY)) * sin(radians(s.y))) < 1 AND s.address LIKE %:address% group by s.id order by :sort desc, s.name")
    List<Shop> FindByShopWithinRadiusAndSort(@Param("address") String address, @Param("myX") double myX, @Param("myY") double myY,@Param("sort") String sort);
    @Query("select s from Shop s where " +
            "6371 * acos(cos(radians(:myY)) * cos(radians(s.y)) * cos(radians(s.x) - radians(:myX)) + "
            + "sin(radians(:myY)) * sin(radians(s.y))) < 1 AND s.address LIKE %:address% group by s.id order by s.name")
    List<Shop> findByShopWhtinRadiusAll(@Param("address") String address, @Param("myX") double myX, @Param("myY") double myY);
}
