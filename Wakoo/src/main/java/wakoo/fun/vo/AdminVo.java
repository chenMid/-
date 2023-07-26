package wakoo.fun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminVo {
    private Integer userId;
    private Integer roleName;
    private Integer name;
    private String username;
    private String email;
    private String mobile;
    private String nickname;
}
