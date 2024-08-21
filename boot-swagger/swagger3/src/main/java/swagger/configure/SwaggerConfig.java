package swagger.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
//@EnableOpenApi //开启swagger-web支持（boot里可加也可不加，有自动配置处理）
public class SwaggerConfig {

    /**
     * swagger文档配置实例
     * 2024/1/14 18:56
     * @author pengshuaifeng
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo()) //配置swagger文档信息
                .select()  //配置api的公开范围
                .apis(RequestHandlerSelectors.basePackage("swagger.controller"))  //设置需要公开的api接口包范围
                //设置指定包范围内的所有端点（路径）都开放。（省略也一样）
                //*如果你想筛选哪些端点（路径）被在文档中，可以使用 PathSelectors 的其他方法，比如 PathSelectors.ant("/api/**") 来只包含匹配特定模式的端点
                .paths(PathSelectors.any())
                .build()
                //设置全局参数
                .globalRequestParameters(setRequestParameter());
    }

    /**
     * 接口文档信息对象
     * 2024/1/14 18:51
     * @author pengshuaifeng
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("系统管理服务-API接口文档")  //文档标题
                .description("提供系统管理服务可用的接口列表及接口功能详情") //文档描述
                .termsOfServiceUrl("http://www.cnblogs.com/irving") //文档提供团队地址
                .version("1.0") //文档版本
                .build();
    }

    private List<RequestParameter> setRequestParameter() {
        RequestParameterBuilder tokenPar = new RequestParameterBuilder();
        List<RequestParameter> pars = new ArrayList<>();
        tokenPar.name("token").description("认证token").in(ParameterType.HEADER).required(true).query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)));
        pars.add(tokenPar.build());
        return pars;
    }

}