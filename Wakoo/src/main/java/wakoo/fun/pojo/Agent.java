package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agent {
    private Integer id;

    @NotBlank(message = "名称不能为空")
    private String name;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{1,4}[- ]?\\d{1,4}[- ]?\\d{1,9}$", message = "联系电话格式不正确")
    private String contactPhone;
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "地址不能为空")
    private String address;
    private String detailedAddress;

    private String status;

    private String creationTime;

    private String updateTime;
    private BigDecimal longitude;
    private BigDecimal  latitude;
}