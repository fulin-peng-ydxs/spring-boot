package commons.model.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 汽车实体
 * @author pengshuaifeng
 * 2023/12/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("汽车模型")
public class Car {

    @ApiModelProperty(value = "名字",required = true)
    @NotNull(message = "名字不能为空")
    private String name;

    @NotNull(message = "颜色不能为空")
    @ApiModelProperty(value = "颜色",required = true)
    private String  color;
}
