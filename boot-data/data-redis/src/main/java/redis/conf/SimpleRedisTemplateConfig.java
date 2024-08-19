package redis.conf;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis-template配置类
 * author: pengshuaifeng
 * 2023/10/22
 */
@Configuration
public class SimpleRedisTemplateConfig {


    /**
     * 配置redis操作对象：string-object
     * 2023/10/22 19:29
     * @author pengshuaifeng
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //1。配置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //2。配置访问方式
         // 用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        redisTemplate.setValueSerializer(jsonSerializer());
        redisTemplate.setHashValueSerializer(jsonSerializer());
         // 使用StringRedisSerializer来序列化和反序列化redis的key值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        //3。初始化调用：主要用来适配操作对象访问时的初始化校验机制
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 配置redis-json序列化对象：jackson
     * 2023/10/22 19:29
     * @author pengshuaifeng
     */
    private Jackson2JsonRedisSerializer<Object> jsonSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        //设置类型信息化存储，用于准确反序列化对象（例如java的多态机制），不需要将final类型的对象做处理,解释如下：
         /*
            在 Jackson 库中，.NON_FINAL 是一种默认类型信息处理的设置，它只包括非最终类（final 类）的类型信息。这种设置通常用于避免在序列化和反序列化过程中包含最终类的类型信息，因为最终类不能被继承，所以不需要类型信息来区分不同的子类。
            以下是为什么要选择 .NON_FINAL 值的一些原因：
            1.性能优化： 最终类无法被继承，因此在类型信息中包含它们通常是不必要的，而且可能引入不必要的开销。通过仅包含非最终类的类型信息，可以减少序列化和反序列化操作的负担，从而提高性能。
            2.类型信息的清晰性： 包含所有最终类的类型信息可能会导致数据中的冗余信息，降低了数据的清晰性。通过只包括非最终类的类型信息，可以使数据更加紧凑和易读。
            3.一般性的需求： 大多数情况下，只需要知道非最终类的类型信息，因为这通常足够进行多态操作和对象还原。只有在特殊情况下，需要包含最终类的类型信息。
            总之，选择 .NON_FINAL 值是为了提高性能和数据清晰性，并且在一般情况下通常足够满足多态操作和对象还原的需求。但在特殊情况下，如果需要包含最终类的类型信息，可以选择其他类型信息处理设置。
          */
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 只根据字段进行序列化
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }
}
