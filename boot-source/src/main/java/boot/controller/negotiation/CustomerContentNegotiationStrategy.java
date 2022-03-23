package boot.controller.negotiation;

import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: 自定义内容协商协议
 * @date 2022/3/23 0:55
 */
public class CustomerContentNegotiationStrategy implements ContentNegotiationStrategy {

    @Override
    public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException {
        return null;
    }
}
