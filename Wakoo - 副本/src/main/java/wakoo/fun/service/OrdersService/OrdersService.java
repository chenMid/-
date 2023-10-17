package wakoo.fun.service.OrdersService;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.dto.OrdersDto;
import wakoo.fun.pojo.Agent;
import wakoo.fun.pojo.Orders;
import wakoo.fun.vo.SubclassVo;

import java.util.List;

/**
 * @author 代理Service层
 */
public interface OrdersService {
    /**
     * 查询所有代理订单信息
     * @param keyword 模糊查询字段
     * @return 成功返回，不成功返回null
     */
    List<Orders> getAllAgentInformation(@Param("keyword") String keyword, @Param("userId") Integer userId, @Param("status") Integer status, Integer number);

    /**
     * 获取人员列表
     * @param userId 用户id
     * @return 返回人员信息
     */
    List<Agent> acquisitionPersonnel(Integer userId);

    /**
     * 返回 父级id
     * @param userId 用户id
     * @return 返回父级id
     */
    Integer returnsTheParentId(Integer userId);

    /**
     * 如果没有达到要求就进行查找rid指向的角色
     * @param userId 用户id
     * @return rid
     */
    Integer getThePreviousLevelRid(Integer userId);

    /**
     * 查询子类id
     * @param typeName 代理类型
     * @param typeAge 代理年龄
     * @return 子类id
     */
    Integer querySubclassId(@Param("typeName") String typeName, @Param("typeAge") Integer typeAge);

    /**
     * 添加代理订单
     * @param orders 代理订单信息
     * @return 返回成功或失败
     */
    Boolean addAgentOrder(@Param("orders") OrdersDto orders);

    /**
     * 根据id查询订单详情
     * @param id 订单id
     * @return 订单详情
     */
    OrdersDto getOrderById(Integer id);

    /**
     * 修改订单信息
     * @param ordersDto 订单信息 o
     * @return 返回对象
     */
    Boolean modifyOrderInformation(@Param("ordersDto") OrdersDto ordersDto);

    /**
     *  删除订单
     * @param ids 订单id
     * @return 返回对象
     */
    Boolean delOrder(Integer[] ids,Integer status);

    /**
     * 多条件查询
     *
     * @param name         名称
     * @param subclassName 子类名称
     * @param createTime   创建时间
     * @param status       状态
     * @param userId       用户id
     * @return 返回列表
     */
    List<Orders> multiConditionQuery(@Param("name") String name,
                                     @Param("subclassName") String subclassName,
                                     @Param("createTime") String createTime,
                                     @Param("status") String status,
                                     @Param("userId") Integer userId);
}
