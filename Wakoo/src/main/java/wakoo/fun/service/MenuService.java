package wakoo.fun.service;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.dto.MenuDto;
import wakoo.fun.pojo.SysMenu;
import wakoo.fun.pojo.SysRole;

import java.util.List;

public interface MenuService {
    /**
     * 查询该用户下可以观看的所有菜单
     */
    List<SysMenu> getMenu(Integer id);

    /**
     * 查询所有角色
     * @return
     */
    List<SysRole> getRole();
    /**
     * 通过一级id查询二级路径
     * @return
     */
    List<SysMenu> ListTwoMune(@Param("id") Integer id);

}
