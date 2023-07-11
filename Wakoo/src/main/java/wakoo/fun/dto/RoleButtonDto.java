package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wakoo.fun.Vo.RoleVo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleButtonDto {
    private Integer id;
    private Integer fId;
    private String name;
    private List<RoleVo> list;
}
