package Backend.MASJIB.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchHighLevelClientConfig {
    @Value("${spring.data.elasticsearch.username}")
    private String username;
    @Value("${spring.data.elasticsearch.password}")
    private String password;
    @Value("${spring.data.elasticsearch.host}")
    private String host;
    @Value("${spring.data.elasticsearch.port}")
    private int port;
    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));

        RestClientBuilder builder = RestClient.builder(
                        new HttpHost(host, port))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider));

        return new RestHighLevelClient(builder);
    }
}
