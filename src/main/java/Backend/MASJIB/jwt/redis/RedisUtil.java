package Backend.MASJIB.jwt.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class RedisUtil {
    private final StringRedisTemplate redisTemplate;

    public RedisUtil(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<String> getTokenToRedis(String key){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return Optional.ofNullable(valueOperations.get(key));
    }

    public void setTokenToRedis(String key, String value, long durationMilliS){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(durationMilliS);
        valueOperations.set(key,value, expireDuration);
    }
    public void deleteTokenToRedis(String key){
        redisTemplate.delete(key);
    }
}
