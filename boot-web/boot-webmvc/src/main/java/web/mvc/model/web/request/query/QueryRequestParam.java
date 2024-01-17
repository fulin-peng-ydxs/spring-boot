package web.mvc.model.web.request.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import web.mvc.model.web.request.PageRequestParam;

/**
 * 查询请求模型
 *
 * @author fulin-peng
 * 2024-01-16  11:42
 */
@Data
@ApiModel(value = "查询请求模型")
public class QueryRequestParam<B,Q> {

    @ApiModelProperty(value = "分页参数")
    private PageRequestParam<B> page;

    @ApiModelProperty(value = "查询参数")
    private Q query;
}
