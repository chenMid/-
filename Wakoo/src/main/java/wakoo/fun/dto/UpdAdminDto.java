package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdAdminDto {
    private Integer userId;
    private Integer roleId;
    private Integer orderId;
    private String username;
    private String email;
    private String mobile;
    private String nickname;
    private String password;
}
