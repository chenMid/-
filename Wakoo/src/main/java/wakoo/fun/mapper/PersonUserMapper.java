package wakoo.fun.mapper;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.dto.OrdersDto;
import wakoo.fun.pojo.Agent;
import wakoo.fun.pojo.Orders;
import wakoo.fun.pojo.PersonUser;
import wakoo.fun.vo.PersonUserVo;

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
     * @param parentId 父级id
     * @return 对象
     */
    List<Agent> acquireOtherThanPersonnel(@Param("userId") Integer userId, @Param("parentId") Integer parentId);

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
    List<PersonUserVo> inquireAboutTheOwnersCourse(Integer campusId);

    /**
     *   添加购买的课程
     * @param personUserVo 购买课程
     * @return 是否成功
     */
    Boolean addPurchaseCourse(@Param("person") PersonUserVo personUserVo);

    /**
     *  检查订单状态
     * @param campusId campusId
     * @return  订单对象
     */
    Orders checkOrderStatus(@Param("campusId") Integer campusId);

    /**
     *  修改订单数量
     * @param number 订单号
     * @param qty 订单数量
     * @param rqty 输入的订单数量
     * @param userId 用户id
     * @return 是否成功
     */
    Boolean modifyOrderStatus(@Param("number") Integer number, @Param("qty") Integer qty, @Param("rqty") Integer rqty, @Param("userId") Integer userId);

    /**
     *  订单状态或者被修改订单状态，自动也要改
     * @param id 订单号
     * @return 是否成功
     */
    Boolean modifyOrderStatusOr(Integer id);

    /**
     * 获取已有课程
     * @param campusId 所属人id
     * @return 对象
     */
    List<PersonUserVo> accessExistingCourses(Integer campusId);

}
