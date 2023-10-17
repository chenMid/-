package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author HASEE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentDto {
    private Integer id;

    @NotBlank(message = "名称不能为空")
    private String name;

    @Pattern(regexp = "^(?:(?:\\+|00)86)?1[3-9]\\d{9}$", message = "手机号格式有误")
    @Size(min = 11, max = 11, message = "手机号只能为{11}位")
    private String contactPhone;
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "邮箱格式不正确")
    private String email;

    private String address;
    private String detailedAddress;

    private String status;
    private String roleId;
    private String createTime;
    private String deleteTime;

    private String updateTime;
    private BigDecimal longitude;
    private BigDecimal  latitude;
}