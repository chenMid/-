package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdmininistraltionDto {
    private String username,nickname,password,campusId,mobile,email;
    private Integer roleId;
}
