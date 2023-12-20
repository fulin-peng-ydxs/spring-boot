package web.mvc.model.web.request;

import lombok.Data;

/**
 * 请求参数模型
 * @author peng_fu_lin
 * 2023-06-21 15:31
 */
@Data
public abstract class RequestParam <T>{
    private T body;
}