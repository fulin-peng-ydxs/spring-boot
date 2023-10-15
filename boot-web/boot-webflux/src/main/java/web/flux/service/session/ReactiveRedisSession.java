package web.flux.service.session;

import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;
/**
 * redis-session服务
 * author: pengshuaifeng
 * 2023/10/15
 */
@Service
public class ReactiveRedisSession {

    @Resource(name = "sessionRepository")
    ReactiveSessionRepository<Session> reactiveSessionRepository;

    /**
     * 获取session信息
     * @author fulin peng
     * 2023/7/20 0020 14:47
     */
    public Mono<Session> getSession(String sessionId){
        return reactiveSessionRepository.findById(sessionId);
    }

    /**
     * 获取session属性
     * @author fulin peng
     * 2023/7/20 0020 18:24
     */
    public Mono<Object> getSessionAttribute(String sessionId, String attributeName){
        return reactiveSessionRepository.findById(sessionId)
                .flatMap(session -> {
                    Object attribute = session.getAttribute(attributeName);
                    if(attribute==null)
                        return Mono.empty();
                    else return Mono.just(attribute);
                });
    }

    /**
     * 保存session属性
     * @author fulin peng
     * 2023/7/26 0026 19:08
     */
    public Mono<String> saveSessionAttribute(String sessionId,String attributeName,Object attributeValue){
        return getSession(sessionId)
                .flatMap(redisSession-> saveSessionAttribute(redisSession,attributeName,attributeValue)
                        .then(Mono.just("保存成功")))
                .switchIfEmpty(Mono.just("非法的sessionId:"+sessionId));
    }

    public Mono<Void> saveSessionAttribute(Session session,String attributeName,Object attributeValue){
        session.setAttribute(attributeName,attributeValue);
        return reactiveSessionRepository.save(session);
    }

    /**
     * 保存session
     * @author fulin peng
     * 2023/7/26 0026 19:08
     */
    public Mono<Void> saveSession(Session session){
        return reactiveSessionRepository.save(session);
    }

    /**
     * 删除session
     * @author fulin peng
     * 2023/7/26 0026 19:25
     */
    public Mono<Void> deleteSession(String sessionId){
        return reactiveSessionRepository.deleteById(sessionId);
    }
}
