package Backend.MASJIB.es.repository;

import Backend.MASJIB.es.document.Shop;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ShopDocumentRepository extends ElasticsearchRepository<Shop,Long> {
    @Query("{\"bool\": {\"should\": [" +
            "{\"wildcard\": {\"name\": \"*?0*\"}}," +
            "{\"wildcard\": {\"address\": \"*?0*\"}}," +
            "{\"wildcard\": {\"kind\": \"*?0*\"}}" +
            "]}}")
    List<Shop> findByName(String name);
}
