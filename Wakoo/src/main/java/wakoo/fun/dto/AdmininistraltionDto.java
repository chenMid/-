package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdmininistraltionDto {
    private String username,nickname,password,name,mobile,email,createtime;
    private Integer roleName;
}
