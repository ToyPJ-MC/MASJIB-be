package Backend.MASJIB.es.service;

import Backend.MASJIB.es.document.Shop;
import Backend.MASJIB.es.document.ShopLog;
import Backend.MASJIB.es.dto.RealTimeKeywordDto;
import Backend.MASJIB.es.repository.ShopDocumentRepository;
import Backend.MASJIB.es.repository.ShopLogRepository;
import Backend.MASJIB.shop.repository.ShopRepository;

import co.elastic.clients.json.JsonData;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;

@Service
public class ShopDocumentService {
    private final ShopDocumentRepository shopDocumentRepository;
    private final ShopRepository shopRepository;
    private final ShopLogRepository shopLogRepository;
    private final RestHighLevelClient client;
    @Autowired
    public ShopDocumentService(ShopDocumentRepository shopDocumentRepository, ShopRepository shopRepository, ShopLogRepository shopLogRepository, RestHighLevelClient client) {
        this.shopDocumentRepository = shopDocumentRepository;
        this.shopRepository = shopRepository;
        this.shopLogRepository = shopLogRepository;
        this.client = client;
    }
    public List<String> test(String keyword) throws IOException { //검색어 자동완성을 위한 메서드
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // Fuzzy 쿼리 생성
        FuzzyQueryBuilder fuzzyQuery = QueryBuilders.fuzzyQuery("name.keyword", keyword)
                .fuzziness(Fuzziness.ONE);

        // Prefix 쿼리 생성
        PrefixQueryBuilder prefixQuery = QueryBuilders.prefixQuery("name.keyword", keyword);

        // Bool 쿼리에 should 절 추가
        boolQuery.should(fuzzyQuery);
        boolQuery.should(prefixQuery);

        // 검색 요청 생성
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(boolQuery);
        SearchRequest searchRequest = new SearchRequest("idx_es_shop").source(sourceBuilder);

        // Elasticsearch에 검색 요청 전송
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 결과 처리
        // 예시로서 결과를 출력하는 코드를 추가하거나 필요에 따라 결과를 객체로 변환하여 반환할 수 있습니다.
        System.out.println("Search response: " + searchResponse.toString());

        return new ArrayList<>();
    }
    public List<RealTimeKeywordDto> getRealTimeKeyword() throws IOException { //실시간 순위를 제공하는 메서드
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // 현재 시간의 정각을 구함
        LocalDateTime startOfCurrentHour = now.truncatedTo(ChronoUnit.HOURS);
        // 다음 시간의 정각을 구함
        LocalDateTime startOfNextHour = startOfCurrentHour.plusHours(1);
        //ex) 현재 17:30분이라면 17:00 정각부터 ~ 18:00 사이를 구하기 때문에

        // 결과 출_
        // DateTimeFormatter를 사용하여 초까지 출력
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String formatGte = startOfCurrentHour.format(formatter);
        String formatLte = startOfNextHour.format(formatter);

        TermsAggregationBuilder agg = new TermsAggregationBuilder("keywords_per_hour")
                .field("keyword.keyword")
                .size(10);
        BoolQueryBuilder queryBuilders = QueryBuilders.boolQuery()
                .must(rangeQuery("createTime")
                        .gte(formatGte)
                        .lte(formatLte));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(queryBuilders)
                .aggregation(agg);

        LocalDate today = LocalDate.now();
        SearchRequest searchRequest = new SearchRequest("log-"+today)
                .source(searchSourceBuilder);
        SearchResponse searchResponse =client.search(searchRequest, RequestOptions.DEFAULT);
        RestStatus status = searchResponse.status();
        // 집계 결과 가져오기
        List<RealTimeKeywordDto> shopLogs = new ArrayList<>();
        if(status==RestStatus.OK){
            Aggregations aggregations = searchResponse.getAggregations();
            ParsedStringTerms keywordAggregation = aggregations.get("keywords_per_hour");
            shopLogs = keywordAggregation.getBuckets().stream().map(RealTimeKeywordDto::from).collect(Collectors.toList());
        }
        return shopLogs;
    }
    public void convertToDocument(){  //shop entity -> document 객체로 변환
        int pageSize = 1000; // 한 번에 처리할 데이터 양
        int pageNumber = 0;  // 첫 번째 페이지 번호
        Page<Backend.MASJIB.shop.entity.Shop> shopPage;

        do {
            // 현재 페이지의 데이터를 가져옵니다.
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            shopPage = shopRepository.findAll(pageable);

            for (Backend.MASJIB.shop.entity.Shop shop : shopPage) {
                Shop addShop = Shop.builder()
                        .id(shop.getId())
                        .name(shop.getName())
                        .address(shop.getAddress())
                        .x(shop.getX())
                        .y(shop.getY())
                        .kind(shop.getKind())
                        .build();
                shopDocumentRepository.save(addShop);
            }

            // 다음 페이지로 이동하기 위해 페이지 번호를 증가시킵니다.
            pageNumber++;
        } while (shopPage.hasNext());
    }

    public List<Shop> SearchShop(String keyword){ //keyword로 검색을 통해 shop을 return
        shopLogRepository.save(ShopLog.from(keyword));
        return shopDocumentRepository.findByName(keyword);
    }

}