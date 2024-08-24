package commons.model.entity.log;


import com.fasterxml.jackson.annotation.JsonFormat;
import commons.model.enums.GeneralOperateStatus;
import commons.model.enums.GeneralOperateType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
/**
 * sys_log
 * author: fulin-peng
 * 2024-01-24 15:42:45
 */
@ApiModel("系统日志模型")
@Data
public class SysLog {
    //sid
    @ApiModelProperty(value ="sid")
    private Long sid;
    //事件：例如系统登录、异常报错
    @NotNull(message="事件：例如系统登录、异常报错不能为空")
    @ApiModelProperty(value ="事件：例如系统登录、异常报错")
    private String event= GeneralOperateType.SIMPLE_VISIT;
    //事件描述
    @ApiModelProperty(value ="事件描述")
    private String eventDescription;
    //事件时间
    @NotNull(message="事件时间不能为空")
    @ApiModelProperty(value ="事件时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date eventTime;
    //事件用户
    @ApiModelProperty(value ="事件用户")
    private String eventUser;
    //事件用户id
    @ApiModelProperty(value ="事件用户id")
    private String eventUserId;
    //事件用户ip
    @ApiModelProperty(value ="事件用户ip")
    private String eventUserHost;
    //事件用户所属机构
    @ApiModelProperty(value ="事件用户所属机构")
    private String eventUserOrgan;
    //事件用户机构路径
    @ApiModelProperty(value ="事件用户机构路径")
    private String eventUserOrganPath;
    //事件发生服务源
    @NotNull(message="事件发生服务不能为空")
    @ApiModelProperty(value ="事件发生服务")
    private String eventService="unknown";
    //事件扩展字段
    @ApiModelProperty(value ="事件扩展字段")
    private String eventExt;
    @ApiModelProperty(value ="事件状态")
    private String eventStatus= GeneralOperateStatus.SUCCESS;
}
