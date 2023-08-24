package wakoo.fun.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author 视频Vo实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideosVo {
    private Integer id;
    private String classTypeName;
    @NotNull(message = "视频标题不能为空")
    @NotBlank(message = "视频标题不能为空")
    private String title;
    @NotNull(message = "视频顺序不能为空")
    private Integer which;
    private BigDecimal videoLength;
    @NotNull(message = "图片RUL不能为空")
    @NotBlank(message = "图片RUL不能为空")
    private String videoImageUrl;
    @NotNull(message = "视频RUL不能为空")
    @NotBlank(message = "视频RUL不能为空")
    private String videoUrl;
    @NotNull(message = "视频简介不能为空")
    @NotBlank(message = "视频简介不能为空")
    private String videoIntroduction;
}
