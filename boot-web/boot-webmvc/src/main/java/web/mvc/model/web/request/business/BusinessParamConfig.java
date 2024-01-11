package web.mvc.model.web.request.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "业务参数配置模型")
public  class BusinessParamConfig<I> {

    @ApiModelProperty(value = "业务id集合")
    private List<I> ids;

}