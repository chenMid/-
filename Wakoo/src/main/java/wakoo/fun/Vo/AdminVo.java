package wakoo.fun.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminVo {
    private Integer userId;
    private Integer id;
    private String roleName;
    private Integer agentId;
    private String name;
    private String username;
    private String email;
    private String mobile;
    private String nickname;
}
