package swagger.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户实体
 * @author pengshuaifeng
 * 2023/12/20
 */
@Data
@ApiModel("用户模型")
public class User {

    @ApiModelProperty(value = "姓名",required = true)
    private String name;

    @ApiModelProperty(value = "性别",required = true)
    private String sex;

    @ApiModelProperty(value = "年龄",required = true,example = "1")
    private Integer age;

    @ApiModelProperty(value = "地址")
    private String address;
}
