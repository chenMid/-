package wakoo.fun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 个人信息实体类
 * @author HASEE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FadminVo {
    private Integer id;
    private String userName,nickName,avatar,email,mobile, password;
}
