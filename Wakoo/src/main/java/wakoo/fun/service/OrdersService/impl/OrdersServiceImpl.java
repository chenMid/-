package wakoo.fun.service.OrdersService.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.mapper.OrdersMapper;
import wakoo.fun.pojo.Orders;
import wakoo.fun.service.OrdersService.OrdersService;

import javax.annotation.Resource;
import java.util.List;
/**
 * @author Service实现层Impl
 */
@Service
public class OrdersServiceImpl implements OrdersService {
    @Resource
    private OrdersMapper ordersMapper;

    @Override
    public List<Orders> getAllAgentInformation(String keyword) {
        return ordersMapper.getAllAgentInformation(keyword);
    }
}
