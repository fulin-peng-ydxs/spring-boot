package commons.model.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 请求参数模型
 * @author peng_fu_lin
 * 2023-06-21 15:31
 */
@Data
@ApiModel(value = "请求模型",discriminator = "param")
public  class RequestParam <T>{

    @ApiModelProperty(value = "请求实体")
    private T body;
}