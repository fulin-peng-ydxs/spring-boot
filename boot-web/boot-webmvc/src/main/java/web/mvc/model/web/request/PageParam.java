package web.mvc.model.web.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

/**
 * 分页请求模型
 * @author peng_fu_lin
 * 2023-06-21 15:28
 */
@Data
@ToString(callSuper = true)
@ApiModel(value = "分页请求模型",discriminator = "param")
public class PageParam<T>  extends RequestParam<T>{

    private int page;

    private int pageSize;
}