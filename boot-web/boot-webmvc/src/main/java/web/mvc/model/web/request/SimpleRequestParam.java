package web.mvc.model.web.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

/**
 * 简单的请求参数模型
 * @author fulin-peng
 * 2023-11-20  13:49
 */
@Data
@ToString(callSuper = true)
@ApiModel(value = "简单请求模型",discriminator = "param")
public class SimpleRequestParam<T> extends RequestParam<T>{ }
