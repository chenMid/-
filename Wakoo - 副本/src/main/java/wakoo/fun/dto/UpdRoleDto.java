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
    private String menus;
    private Integer[] list;

    public UpdRoleDto(Integer rid,Integer status, String menus) {
        this.rid = rid;
        this.status = status;
        this.menus = menus;
    }
}
