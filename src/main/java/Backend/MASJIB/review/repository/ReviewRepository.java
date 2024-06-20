package Backend.MASJIB.review.repository;

import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.review.entity.Review;
import Backend.MASJIB.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("select r from Review r where r.member = :member and r.id = :id")
    Optional<Review> findByIdAndMember (@Param("id") long id, @Param("member") Member member);

    List<Review> findByShopAndMember (Shop shop, Member member);
    @Query("select r from Review r where r.member = :member order by r.createTime asc")
    List<Review> findByReviewWithMemberWithCreateTimeAsc (@Param("member") Member member);

    @Query("select r from Review r where r.shop.id = :shopid order by r.createTime desc")
    Review findByRecentReview(@Param("shopid")long id) ;


    @Query("select r from Review r where r.shop.id = :shop_id and r.comment is not null order by r.createTime desc") //최신순
    List<Review> findByReviewAndCreateTimeDesc(@Param("shop_id")long shop_id);

    @Query("select r from Review r where r.shop.id = :shop_id and r.comment is not null order by r.createTime asc ") //오래된순
    List<Review> findByReviewAndCreateTimeAsc(@Param("shop_id")long shop_id);
    @Query("select r from Review r where r.shop.id = :shop_id and r.comment is not null order by r.rating desc ") //높은 평점 순
    List<Review> findByReviewAndRatingDesc(@Param("shop_id")long shop_id);
    @Query("select r from Review r where r.shop.id = :shop_id and r.comment is not null order by r.rating asc ") //낮은 평점 순
    List<Review> findByReviewAndRatingAsc(@Param("shop_id")long shop_id);
}
