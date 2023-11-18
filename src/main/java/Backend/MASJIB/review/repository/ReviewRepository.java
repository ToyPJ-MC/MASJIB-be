package Backend.MASJIB.review.repository;

import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.review.entity.Review;
import Backend.MASJIB.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Optional<Review> findByIdAndMemberId (long id, long memberId);

    Optional<Review> findByShopAndMember (Shop shop, Member member);
}
