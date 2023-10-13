package wakoo.fun.mapper;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.pojo.FaAdmin;
import wakoo.fun.pojo.FaAdminLogin;
import wakoo.fun.pojo.SysMenu;
import wakoo.fun.pojo.SysRole;
import wakoo.fun.vo.FadminVo;

import java.util.List;


public interface FaAdminMapper {
    /**
     * 登录处理
     * @param userName
     * @return
     */
    List<FaAdmin> faAdmin(@Param("userName") String userName);

    Boolean UpdToken(@Param("Token") String Token, @Param("userName") String userName);

    Boolean avatar(@Param("id") Integer id, @Param("avatarPath") String avatarPath);

    FaAdmin getFaAdmin(Integer id);

    FaAdminLogin getUserNameFaAdmin(@Param("userName") String userName);
    //查询携带身份的用户信息
    FaAdminLogin ListFadmin(@Param("username") String username);
    //获取身份

    /**
     * 菜单处理
     */
    List<SysMenu> getMenu(@Param("id") Integer id);

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

    /**
     *  保存个人信息
     * @param fadminVo FadminVo
     * @return Boolean
     */

    Boolean modifyPersonalInformation(@Param("fadminVo") FadminVo fadminVo);

    String getPassword(Integer id);
}
