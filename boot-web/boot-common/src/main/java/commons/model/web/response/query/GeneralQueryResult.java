package commons.model.web.response.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 通用查询结果模型
 *
 * @author fulin-peng
 * 2024-01-17  14:26
 */
@ApiModel(value = "通用查询结果模型")
@Data
@AllArgsConstructor
public class GeneralQueryResult<T> {

    @ApiModelProperty(value = "查询响应数据")
    private T data;

    @ApiModelProperty(value = "查询响应数据总数")
    private Number total;
}
