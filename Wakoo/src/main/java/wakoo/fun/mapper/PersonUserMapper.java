package wakoo.fun.mapper;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.pojo.Agent;
import wakoo.fun.pojo.PersonUser;

import java.util.List;

/**
 * @author JCccc
 * @since 2018年5月5日
 * @version 1.0.0
 * @author HASEE
 */
public interface PersonUserMapper {
    /**
     * 查询合法用户
     *
     * @param userId  id
     * @param keyword 搜索关键字
     */
    List<PersonUser> getRegularUsers(String keyword, Integer userId);

    /**
     * 查询指定信息
     *
     * @param id 成功返回其中一个model
     * @return 返回对象
     */
    Agent getPersonUserByUserIdAnd(Integer id);

    /**
     * 添加普通用户
     *
     * @param personUser 添加信息
     * @return 添加是否成功
     */
    Boolean addCommonUser(@Param("person") PersonUser personUser);

    /**
     * 添加合法用户和其关联信息
     *
     * @param personId 用户id
     * @param commonId 指定id
     * @return 是否添加
     */
    Boolean addAgentsAndHumanRelationships(@Param("personId") Integer personId, @Param("commonId") Integer commonId);

    /**
     * 根据手机号查询是否存在
     *
     * @param iphone 手机号
     * @return 电话号码
     */
    String getThePhoneNumber(String iphone);

    /**
     * 修改的 回显信息
     * @param id id
     * @return 对象
     */
    PersonUser theCommandOutputIsModified(Integer id);

    /**
     *  修改角色信息
     * @param personUser 要修改的角色信息
     * @return 是否成功
     */
    Boolean modifyingCommonUser(@Param("person") PersonUser personUser);

    /**
     *  根据手机号查询用户信息
     * @param iphone 手机号
     * @return 用户信息
     */
    PersonUser queryByMobilePhoneNumber(String iphone);

    /**
     *  获取他人不与指定的人关联的信息
     * @param userId 用户id
     * @param iphone 电话号码
     * @return 对象
     */
    List<Agent> acquireOtherThanPersonnel(@Param("userId") Integer userId, @Param("iphone") String iphone);
}
