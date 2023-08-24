package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAdministraltion {
    private Integer id;
    private String username, nickname, roleName,name, email, mobile, status, logintime,agentId,roleFather;
}
