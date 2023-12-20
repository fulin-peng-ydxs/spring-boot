package web.mvc.model.entity;


import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 用户实体
 * @author pengshuaifeng
 * 2023/12/20
 */
@Data
public class User {
    @NotNull(message = "姓名不能为空")
    private String name;
    @NotNull(message = "性别不能为空")
    private String sex;
    @Max(value = 18,message = "年龄不能大于18")
    @Min(value = 6,message = "年龄不能小于6")
    @NotNull(message = "年龄不能为空")
    private Integer age;
    @NotNull(message = "地址不能为空")
    private String address;
}
