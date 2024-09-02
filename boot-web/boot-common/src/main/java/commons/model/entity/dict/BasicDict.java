package commons.model.entity.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BasicDict {

	/**
	 * 主键
	 */
	@ApiModelProperty("主键")
	private Long sid;
	/**
	 * 上级ID
	 */
	@ApiModelProperty("上级ID")
	private Long parentId;
	/**
	 * 所属租户
	 */
	@ApiModelProperty("所属租户")
	private Long tenantId;
	/**
	 * 所属应用
	 */
	@ApiModelProperty("所属应用")
	private Long appId;
	/**
	 * 	所属应用名称
	 */
	@ApiModelProperty("所属应用名称")
	private String appName;
	/**
	 * 字典名称
	 */
	@ApiModelProperty("字典名称")
	private String dictName;
	/**
	 * 字典键
	 */
	@ApiModelProperty("字典键")
	private String dictKey;
	/**
	 * 字典值
	 */
	@ApiModelProperty("字典值")
	private String dictValue;
	/**
	 * 是否叶子节点，1是，0否
	 */
	@ApiModelProperty("是否叶子节点，1是，0否")
	private Integer isLeaf;
	/**
	 * 	排序
	 */
	@ApiModelProperty("排序")
	private Integer ordered;
	/**
	 * 是否有效1 是，0 否
	 */
	@ApiModelProperty("是否有效1 是，0 否")
	private Integer status;
}