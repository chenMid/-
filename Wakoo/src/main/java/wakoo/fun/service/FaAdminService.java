package wakoo.fun.service;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.pojo.FaAdmin;
import wakoo.fun.pojo.FaAdminLogin;

import java.util.List;

public interface FaAdminService {
    /**
     * 查询指定用户，没有指定用户查询所有
     * @param userName
     * @return
     */
    List<FaAdmin> faAdmin(String userName);

    /**
     * 存储token
     * @param Token
     * @param userName
     * @return
     */
    Boolean UpdToken(String Token,String userName);

    /**
     * 查询指定用户通过id
     * @param id
     * @return
     */
    FaAdmin getFaAdmin(Integer id);
    /**
     * 查询指定用户
     * userName用户名(账号)
     */
    FaAdminLogin UserNameFaAdmin(@Param("userName") String userName);

    /**
     * 登录查询身份以及信息
     * @param username
     * @return
     */
    FaAdminLogin ListFadmin(@Param("username") String username);

}