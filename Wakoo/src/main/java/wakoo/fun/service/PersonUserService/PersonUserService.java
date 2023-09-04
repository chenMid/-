package wakoo.fun.service.PersonUserService;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;
import wakoo.fun.pojo.Agent;
import wakoo.fun.pojo.PersonUser;
import wakoo.fun.vo.PersonUserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/** 用户信息service接口
 * @author HASEE
 */
public interface PersonUserService {
    /**
     *  查询合法用户
     *  @param userId id
     * @param keyword 搜索关键字
     * @return 返回结果
     */
    List<PersonUser> getRegularUsers(String keyword, Integer userId,Integer status,Integer number);

    /**
     *  查询指定信息
     * @param id 成功返回其中一个model
     * @return 返回对象
     */
    Agent getPersonUserByUserIdAnd(Integer id);
    /**
     * 添加普通用户
     * @param personUser 添加信息
     * @return 添加是否成功
     */
    Boolean addCommonUser(@Param("person") PersonUser personUser);
    /**
     *  添加合法用户和其关联信息
     * @param personId 用户id
     * @param commonId 指定id
     * @return 是否添加
     */
    Boolean addAgentsAndHumanRelationships(@Param("personId") Integer personId, @Param("commonId") Integer commonId);

    /**
     *  根据手机号查询是否存在
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
     * @param parentId 父级id
     * @return 对象
     */
    List<Agent> acquireOtherThanPersonnel(Integer userId, Integer parentId,String iphone);

    /**
     *  备注的查询
     * @param personId id
     * @return 对象
     */
    List<Agent> purchaser(Integer personId);

    /**
     * 查询所属课程
     * @param campusId 所属人id
     * @return 对象
     */
    List<PersonUserVo> inquireAboutTheOwnersCourse(Integer campusId,Integer cid);

    /**
     *   添加购买的课程
     * @param personUserVo 购买课程
     * @return 是否成功
     */
    Boolean addPurchaseCourse(PersonUserVo personUserVo,HttpServletRequest request);

    /**
     * 获取已有课程
     * @param campusId 所属人id
     * @return 对象
     */
    List<PersonUserVo> accessExistingCourses(Integer campusId);
    /**
     * 添加审核
     * @param id 用户姓名
     * @param sex 性别
     * @param age   年龄
     * @param userName 用户姓名
     * @param agent 代理
     * @param studentClass 课程
     * @return 是否成功
     */
    Boolean addAudit(Integer id, Integer sex, Integer age, String userName, Integer agent,Integer studentClass, Integer orderId);
    /**
     *  软删除用户
     * @param ids id
     * @param status 状态
     * @return 是否成功
     *
     */
    Boolean softDeleteUser(Integer[] ids,Integer status);

    /**
     *  查询学生列表
     *  @param number 学生
     * @param classname 判断
     * @param iphone 手机号
     * @param agentName 代理
     * @param sex 性别
     * @param age 年龄
     * @return 对象
     */
    List<PersonUser> queryUsersBasedOnMultipleCriteria (String classname,String iphone,String agentName,Integer sex,Integer age,Integer userId,Integer number,Integer status);

}
