package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleButtonDto {
    private Integer id;
    private Integer fId;
    private String name;
    private Integer[] list;
    private Integer status;
}
