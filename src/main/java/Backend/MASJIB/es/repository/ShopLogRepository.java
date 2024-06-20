package Backend.MASJIB.es.repository;

import Backend.MASJIB.es.document.ShopLog;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShopLogRepository extends ElasticsearchRepository<ShopLog,Long> {
}
