package wakoo.fun.service.OrdersService;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.pojo.Orders;

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
    List<Orders> getAllAgentInformation(@Param("keyword") String keyword);
}
