package commons.model.web.response;

import commons.model.exception.ExceptionType;
import commons.model.web.response.query.GeneralQueryResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 请求响应模型
 */
@Data
@ApiModel(value = "请求响应模型")
public class Response {

	@ApiModelProperty(value = "响应状态",required = true)
	private String status;  //响应状态
	@ApiModelProperty(value = "响应消息",required = true)
	private String message; //响应消息
	@ApiModelProperty(value = "响应内容")
	private Object body; //响应内容

	public Response(String status, String msg, Object body) {
		this.status = status;
		this.message = msg;
		this.body = body;
	}

	public Response(ResponseStatus responseStatus,Object body) {
		this.status = responseStatus.getStatus();
		this.message = responseStatus.getMessage();
		this.body = body;
	}

	public Response(ResponseStatus responseStatus,Object body,Number total) {
		this.status = responseStatus.getStatus();
		this.message = responseStatus.getMessage();
		this.body = new GeneralQueryResult(body,total);
	}

	/**成功响应
	 * 2023/5/10 0010-14:07
	 * @author pengfulin
	*/
	public static  Response success(Object body){
		return new Response(ResponseStatus.SUCCESS, body);
	}

	public static Response success(){
		return new Response(ResponseStatus.SUCCESS, null);
	}

	public static  Response success(Object body,Number total){
		return new Response(ResponseStatus.SUCCESS, body,total);
	}

	/**失败响应
	 * 2023/5/10 0010-14:12
	 * @author pengfulin
	*/
	public static Response failure(Object body){
		return new Response(ResponseStatus.ERROR,body);
	}

	public static Response failure(){
		return new Response(ResponseStatus.ERROR,null);
	}

	public static Response business(Object body){
		return new Response(ResponseStatus.BUSINESS_FAILURE,body);
	}

	public static Response business(){
		return new Response(ResponseStatus.BUSINESS_FAILURE,null);
	}

	/**
	 * 自定义响应
	 * 2023/12/20 21:42
	 * @author pengshuaifeng
	 */
	public static Response custom(ExceptionType exceptionType, Object body){
		return new Response(exceptionType.getValue(),exceptionType.getMessage(),body);
	}

	public static Response custom(ExceptionType exceptionType,String message,Object body){
		return new Response(exceptionType.getValue(),message,body);
	}

	public static Response custom(ResponseStatus responseStatus,Object body){
		return new Response(responseStatus,body);
	}

	public static Response custom(ResponseStatus responseStatus,String message,Object body){
		return new Response(responseStatus.getStatus(),message,body);
	}
}
