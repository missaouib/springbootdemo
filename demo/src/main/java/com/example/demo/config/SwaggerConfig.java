package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger配置类
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        //返回文档摘要信息
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo()).enable(true)
                .select()
                //apis： 添加swagger接口提取范围
                .apis(RequestHandlerSelectors.basePackage("com.example.demo"))
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("springboot demo 接口文档")
                .description("springboot demo")
                .contact(new Contact("auth", "auth URL", "auth Email"))
                .version("1.0")
                .build();
    }
}
/**
 * @RestController 相当于@Controller+@ResponseBody 注解，让标识的这个类返回json格式的数据。
 * 类上面加上@Api的注解，说明这个类要生成api文档，并给予描述。相当于可以根据这个类作为类别的划分。在类里面的方法加上@ApiOperation 注解 用来描述这个方法(接口)是用来干嘛的；
 * @ApiImplicitParam 注解是为了描述这个方法之中的参数。其中的name，value属性你应该知道。required 属性是标识在测试接口时，这个参数是否需要传，true为必须传，false为非必须。
 * 需要对多个参数进行属性说明时，需要用到 @ApiImplicitParams，然后里面再用 @ApiImplicitParam
 * @ApiImplicitParam之中的paramType是标识这个参数应该在哪去获取，常用的有以下几种 header-->放在请求头。请求参数的获取注解：@RequestHeader
 * query -->常用于get请求的参数拼接。请求参数的获取注解：@RequestParam
 * path -->(用于restful接口)-->请求参数的获取获取注解：@PathVariable
 * body -->放在请求体。请求参数的获取注解：@RequestBody
 * @ApiImplicitParam之中的dataType 是用来标识这个参数的类型，默认为String，如果是数组类型，需要加上allowMultiple=true，表示是数组类型的数据。也可以是自定义的对象类型。
 */