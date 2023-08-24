package wakoo.fun.service.OrdersService.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.dto.OrdersDto;
import wakoo.fun.mapper.OrdersMapper;
import wakoo.fun.pojo.Agent;
import wakoo.fun.pojo.Orders;
import wakoo.fun.service.OrdersService.OrdersService;
import wakoo.fun.vo.SubclassVo;

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
    public List<Orders> getAllAgentInformation(String keyword,Integer userid) {
        return ordersMapper.getAllAgentInformation(keyword,userid);
    }

    @Override
    public List<Agent> acquisitionPersonnel(Integer userId) {
        return ordersMapper.acquisitionPersonnel(userId);
    }

    @Override
    public Integer returnsTheParentId(Integer userId) {
        return ordersMapper.returnsTheParentId(userId);
    }

    @Override
    public Integer getThePreviousLevelRid(Integer userId) {
        return ordersMapper.getThePreviousLevelRid(userId);
    }

    @Override
    public Integer querySubclassId(String typeName, Integer typeAge) {
        return ordersMapper.querySubclassId(typeName, typeAge);
    }

    @Override
    public Boolean addAgentOrder(OrdersDto orders) {
        return ordersMapper.addAgentOrder(orders);
    }

    @Override
    public OrdersDto getOrderById(Integer id) {
        return ordersMapper.getOrderById(id);
    }

    @Override
    public Boolean modifyOrderInformation(OrdersDto ordersDto) {
        return ordersMapper.modifyOrderInformation(ordersDto);
    }

    @Override
    public Boolean delOrder(Integer[] ids) {
        return ordersMapper.delOrder(ids);
    }
}
