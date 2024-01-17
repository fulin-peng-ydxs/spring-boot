package commons.model.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 分页请求模型
 * @author peng_fu_lin
 * 2023-06-21 15:28
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "分页请求模型",discriminator = "param")
public class PageRequestParam<B>  extends RequestParam<B>{

    @ApiModelProperty(value = "页数",required = true,example = "1")
    private int page=1;

    @ApiModelProperty(value = "页大小",required = true,example = "10")
    private int pageSize=10;
}