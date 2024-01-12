package web.mvc.model.web.request.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import web.mvc.model.web.request.PageParam;

/**
 * 业务分页请求模型
 *
 * @author fulin-peng
 * 2024-01-09  19:59
 */
@Data
@ApiModel(value = "业务分页请求模型")
public class BusinessPageRequestParam<T,I> {

    @ApiModelProperty(value = "业务基础参数")
    private PageParam<T> pageRequestParam;

    @ApiModelProperty(value = "业务配置参数")
    private BusinessParamConfig<I> businessParam;
}
