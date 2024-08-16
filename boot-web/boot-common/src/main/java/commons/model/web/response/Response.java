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
public class Response<T> {

	@ApiModelProperty(value = "响应状态",required = true)
	private String status;  //响应状态
	@ApiModelProperty(value = "响应消息",required = true)
	private String message; //响应消息
	@ApiModelProperty(value = "响应内容")
	private T body; //响应内容

	public Response(String status, String msg, T body) {
		this.status = status;
		this.message = msg;
		this.body = body;
	}

	public Response(ResponseStatus responseStatus,T body) {
		this.status = responseStatus.getStatus();
		this.message = responseStatus.getMessage();
		this.body = body;
	}

	/**成功响应
	 * 2023/5/10 0010-14:07
	 * @author pengfulin
	*/
	public static <T> Response<T> success(T body){
		return new Response<>(ResponseStatus.SUCCESS, body);
	}

	public static Response<?> success(){
		return new  Response<>(ResponseStatus.SUCCESS, null);
	}

	public static  <T> Response<GeneralQueryResult<T>> success(T body,Number total){
		return new Response<>(ResponseStatus.SUCCESS,new GeneralQueryResult<>(body,total));
	}

	/**失败响应
	 * 2023/5/10 0010-14:12
	 * @author pengfulin
	*/
	public static  <T> Response<T> failure(T body){
		return new Response<>(ResponseStatus.ERROR,body);
	}

	public static Response<?> failure(){
		return new Response<>(ResponseStatus.ERROR,null);
	}

	public static Response<?> failure(String msg){
		return new Response<>(ResponseStatus.ERROR.getStatus(),msg,null);
	}

	public static <T> Response<T> business(T body){
		return new Response<>(ResponseStatus.BUSINESS_FAILURE,body);
	}

	public static Response<?> business(){
		return new Response<>(ResponseStatus.BUSINESS_FAILURE,null);
	}

	/**
	 * 自定义响应
	 * 2023/12/20 21:42
	 * @author pengshuaifeng
	 */
	public static <T> Response<T> custom(ExceptionType exceptionType, T body){
		return new Response<>(exceptionType.getValue(),exceptionType.getMessage(),body);
	}

	public static <T> Response<T> custom(ExceptionType exceptionType,String message,T body){
		return new Response<>(exceptionType.getValue(),message,body);
	}

	public static <T> Response<T> custom(ResponseStatus responseStatus,T body){
		return new Response<>(responseStatus,body);
	}

	public static <T> Response<T> custom(ResponseStatus responseStatus,String message,T body){
		return new Response<>(responseStatus.getStatus(),message,body);
	}
}
