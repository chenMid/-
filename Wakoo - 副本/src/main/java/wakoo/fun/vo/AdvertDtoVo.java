package wakoo.fun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertDtoVo {
    private Integer id;
    @NotNull(message = "广告名称不能为空")
    @NotBlank(message = "广告名称不能为空")
    private String advertName;
    @NotNull(message = "图片不能为空")
    private String content;
    private String img;
    private Integer sort;
}
