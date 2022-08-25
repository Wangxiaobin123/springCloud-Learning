package micro.spring.zuul.config;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;

/**
 * @Author: shengbin
 * @since: 2019/4/29 下午7:28
 */
@Configuration
@EnableSwagger2Doc
public class SwaggerConfig extends WebMvcConfigurerAdapter {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MF-V4.0")
                .description("MF-V4.0-系统接口文档说明")
                .termsOfServiceUrl("http://localhost:80")
                .version("1.0")
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return new UiConfiguration(null, "list", "alpha", "schema",
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
    }
}
