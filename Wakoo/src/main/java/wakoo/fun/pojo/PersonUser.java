package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 普通用户实体类
 * @author HASEE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonUser {
    private Integer id;
    private String classname;
    private String password;
    private Integer sex;
    private Integer age;

    @Pattern(regexp = "^(?:(?:\\+|00)86)?1[3-9]\\d{9}$", message = "手机号格式有误")
    @Size(min = 11, max = 11, message = "手机号只能为{11}位")
    private String iphone;

    private String image;
    private String createTime;
    private String agentName;
}
