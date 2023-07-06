package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaAdminLogin {
    private Integer id;
    private String userName,nickName,password,avatar,updatetime,email,token;
    private String roles;
}
