package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carousel {
    private Integer id;

    private Integer orderNumber;

    private Integer isActive;
    @NotNull(message = "标题不能为空")
    private String title;

    private String imageUrl;
    @NotNull(message = "描述不能为空")
    private String description;

    private String linkUrl;

    @NotNull(message = "图片不能为空")
    private MultipartFile file;
}
