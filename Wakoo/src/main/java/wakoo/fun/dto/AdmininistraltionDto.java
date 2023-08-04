package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdmininistraltionDto {
    @NotBlank(message = "用户名不能为空")
    private String nickname;
    private String password,createtime;
    private String agentId;
    @NotNull(message = "手机号不能为空")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^(?:(?:\\+|00)86)?1[3-9]\\d{9}$", message = "手机号格式有误")
    @Size(min = 11, max = 11, message = "手机号只能为{11}位")
    private String mobile;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^(?:\\w+@(?:qq|163)\\.com)$", message = "邮箱格式错误")
    private String email;
    private Integer roleName,id;
    private Object name;
}
