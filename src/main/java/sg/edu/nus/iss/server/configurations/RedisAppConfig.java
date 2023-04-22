package sg.edu.nus.iss.server.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisAppConfig {

    // REDIS DATABASE CONNECTION
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private Integer redisPort;
    @Value("${spring.redis.database}")
    private Integer redisDatabase;
    @Value("${spring.redis.username}")
    private String redisUsername;
    @Value("${spring.redis.password}")
    private String redisPassword;

    // SET UP BEAN 
    @Bean("redislab")
    public RedisTemplate<String,String> initRedisTemplate() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        // Set all data for the Redis configuration
        redisConfig.setHostName(redisHost);
        redisConfig.setPort(redisPort);
        redisConfig.setDatabase(redisDatabase);
        redisConfig.setUsername(redisUsername);
        redisConfig.setPassword(redisPassword);
        
        // Create an instance of Jedis driver 
        JedisClientConfiguration jedisConfig = JedisClientConfiguration.builder().build();

        // Create a factory of Jedis connection
        JedisConnectionFactory jedisFac = new JedisConnectionFactory(redisConfig, jedisConfig);
        jedisFac.afterPropertiesSet();
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisFac);
        // Makes the key and value from UTF-16 -> UTF-8
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
