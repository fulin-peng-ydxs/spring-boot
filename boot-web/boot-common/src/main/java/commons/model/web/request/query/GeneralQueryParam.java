package commons.model.web.request.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "通用查询模型")
public  class GeneralQueryParam<I> {

    @ApiModelProperty(value = "业务id集")
    private List<I> ids;

}