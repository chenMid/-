package wakoo;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@EnableKnife4j
@EnableWebMvc
@MapperScan("wakoo.mapper")
public class WakooApplication {

    public static void main(String[] args) {
        SpringApplication.run(WakooApplication.class, args);
    }

}
