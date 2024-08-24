package commons.model.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 用户实体
 * @author pengshuaifeng
 * 2023/12/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户模型")
public class User {

    @ApiModelProperty(value = "姓名",required = true)
    @NotNull(message = "姓名不能为空")
    private String name;

    @NotNull(message = "性别不能为空")
    @ApiModelProperty(value = "性别",required = true)
    private String sex;

    @Max(value = 18,message = "年龄不能大于18")
    @Min(value = 6,message = "年龄不能小于6")
    @NotNull(message = "年龄不能为空")
    @ApiModelProperty(value = "年龄",required = true,example = "1")
    private Integer age;

    @ApiModelProperty(value = "地址")
    @NotNull(message = "地址不能为空")
    private String address;

    @ApiModelProperty(value = "汽车")
    private Car car;
}
