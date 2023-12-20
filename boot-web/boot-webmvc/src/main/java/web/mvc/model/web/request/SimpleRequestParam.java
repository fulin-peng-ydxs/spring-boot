package web.mvc.model.web.request;

import lombok.Data;
import lombok.ToString;

/**
 * 简单的请求参数模型
 * @author fulin-peng
 * 2023-11-20  13:49
 */
@Data
@ToString(callSuper = true)
public class SimpleRequestParam<T> extends RequestParam<T>{ }
