package shiro.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;

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
        Object v =  getRedisTemplate().opsForHash().get((k) this.cacheName, k);
        return (v) v;
    }

    @Override
    public v put(k k, v v) throws CacheException {
        log.debug("存入缓存，key:{},value:{}",k,v);
        getRedisTemplate().opsForHash().put((k) this.cacheName,k.toString(),v);
        return v;
    }

    @Override
    public v remove(k k) throws CacheException {
        log.debug("删除缓存，key:{}",k);
        return (v) getRedisTemplate().opsForHash().delete((k) this.cacheName,k.toString());
    }

    @Override
    public void clear() throws CacheException {
        log.debug("清空缓存");
        getRedisTemplate().delete((k) this.cacheName);
    }

    @Override
    public int size() {
        return getRedisTemplate().opsForHash().size((k) this.cacheName).intValue();
    }

    @Override
    public Set<k> keys() {
        return (Set<k>) getRedisTemplate().opsForHash().keys((k) this.cacheName);
    }

    @Override
    public Collection<v> values() {
        return (Collection<v>) getRedisTemplate().opsForHash().values((k) this.cacheName);
    }

    private RedisTemplate<k,v> getRedisTemplate(){
        return redisTemplate;
    }
}