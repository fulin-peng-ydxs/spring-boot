package shiro.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class RedisCache<k,v> implements Cache<k,v> {

    private final String cacheName;

    private final RedisTemplate<k,v> redisTemplate;

    public RedisCache(String cacheName,RedisTemplate<k,v> redisTemplate) {
        this.cacheName = cacheName;
        this.redisTemplate=redisTemplate;
    }

    @Override
    public v get(k k) throws CacheException {
        log.debug("获取缓存，key:{}",k);
        String key = this.cacheName + "_" + k;
        Object v =  getRedisTemplate().opsForValue().get(key);
        //缓存续期
        if(v!=null){
            Long expire = getRedisTemplate().getExpire((k) key, TimeUnit.MINUTES);
            //缓存有效时间是否小于10分钟
            if(expire==null  ||expire.intValue()<=10){
                getRedisTemplate().expire((k) key, Duration.ofMinutes(60));
            }
        }
        return (v) v;
    }

    @Override
    public v put(k k, v v) throws CacheException {
        log.debug("存入缓存，key:{},value:{}",k,v);
        //存入缓存(默认缓存时间：一个小时)
        getRedisTemplate().opsForValue().set((k) ( this.cacheName+"_"+k),v, Duration.ofMinutes(60));
        return v;
    }

    @Override
    public v remove(k k) throws CacheException {
        log.debug("删除缓存，key:{}",k);
        String key = this.cacheName + "_" + k;
        return (v) getRedisTemplate().opsForHash().delete((k)key);
    }

    @Override
    public void clear() throws CacheException {
        log.debug("清空缓存");
        //批量删除
        getRedisTemplate().delete((k) ("*"+this.cacheName+"*"));
    }

    @Override
    public int size() {
        return getRedisTemplate().keys((k) ("*"+this.cacheName+"*")).size();
    }

    @Override
    public Set<k> keys() {
        return getRedisTemplate().keys((k) ("*"+this.cacheName+"*"));
    }

    @Override
    public Collection<v> values() {
        return keys().stream()
                .map(key -> getRedisTemplate().opsForValue().get(key))
                .collect(Collectors.toList());
    }

    private RedisTemplate<k,v> getRedisTemplate(){
        return redisTemplate;
    }
}