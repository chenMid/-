package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdRoleDto {
    private Integer id, rid;
    private String roleName;
    private Integer status;
    private Integer menuId;
    private List<UpdRoleDto> list;
}
