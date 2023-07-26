package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Advert {
    private Integer id, status, sort;
    @NotNull(message = "广告名称不能为空")
    @NotBlank(message = "广告名称不能为空")
    private String advertName;
    @NotNull(message = "广告简介不能为空")
    @NotBlank(message = "广告简介不能为空")
    private String content;
    private String img, createtime, updatetime;
}
