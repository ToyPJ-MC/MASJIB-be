package Backend.MASJIB.service;

import Backend.MASJIB.image.repository.ImageRepository;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.member.repository.MemberRepository;
import Backend.MASJIB.repository.ReviewRepositoryTest;
import Backend.MASJIB.review.entity.Review;
import Backend.MASJIB.review.service.ReviewService;
import Backend.MASJIB.shop.entity.Shop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewServiceTest {
   /* @InjectMocks
    private ReviewService reviewService;
    @Autowired
    private MemberRepository memberRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceTest.class);

    @BeforeEach
    void setUp(){
        Member member = Member
                .builder()
                .name("지우")
                .email("test@test.com")
                .nickname("관동지역 마스터")
                .createTime(LocalDateTime.now())
                .shops(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("Transfer image segmentation Test")
    public void 사진_분할_전송_테스트(){
        try {
            Path path = Paths.get("/Users/moon/Downloads/이력서 사진.jpeg"); //"/Users/moon/Downloads/dev-jeans.png"

            double bytes = Files.size(path);
            double megabyte = Math.ceil(bytes/(1024*1024));
            int mb = (int) megabyte;
            int currentChunk = 0;
            while(currentChunk<mb){
                if(currentChunk==0) System.out.println("진행률 "+currentChunk + "%");
                else System.out.println("진행률 "+currentChunk*100/mb + "%");
                reviewService.chunkUpload(new MockMultipartFile("dev."+path.getFileName(), String.valueOf(path.getFileName()),"image/jpeg",new FileInputStream(path.toFile().getAbsolutePath())),currentChunk,mb);
                currentChunk++;
            }
           System.out.println("진행률 100%");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Send Image Multi-Divisions Test")
    public void 사진_다수_분할_전송_테스트(){
        try {
            Optional<Member> findMember = memberRepository.findByName("지우");
            findMember.orElseThrow(RuntimeException::new);

            String [] pathList = {"/Users/moon/Downloads/dev-jeans.png","/Users/moon/Downloads/1.jpg"}; //"/Users/moon/Downloads/dev-jeans.png","/Users/moon/Downloads/1.jpg"
            for(int i=0;i<pathList.length;i++){
                Path path = Paths.get(pathList[i]);
                double bytes = Files.size(path);
                double megabyte = Math.ceil(bytes/(1024*1024));
                int mb = (int) megabyte;
                int currentChunk = 0;
                while(currentChunk<mb){
                    if(currentChunk==0) System.out.println(i+1+" 번째 "+"진행률 "+currentChunk + "%");
                    else System.out.println(i+1+" 번째 "+"진행률 "+currentChunk*100/mb + "%");
                    //reviewService.chunkUpload(new MockMultipartFile("dev."+path.getFileName(), String.valueOf(path.getFileName()),"image/png",new FileInputStream(path.toFile().getAbsolutePath())),currentChunk,mb,"이집음식굳",findMember.get(),pathList);
                    currentChunk++;
                }
                System.out.println(i+1+" 번째 "+"진행률 100%");
            }
            ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}
