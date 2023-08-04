package wakoo.fun.controller.agencyOrderController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dto.AdminAdministraltion;
import wakoo.fun.pojo.Orders;
import wakoo.fun.service.OrdersService.OrdersService;
import wakoo.fun.vo.MsgVo;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author 代理页面
 */
@EnableTransactionManagement
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "代理订单")
public class AgencyOrderController {
    @Resource
    private OrdersService ordersService;

    @ApiOperation(value = "查询所有代理订单")
    @UserLoginToken
    @GetMapping("/checkAllAgentOrders")
    public ResponseEntity<MsgVo> checkAllAgentOrders(String keyword, Integer pageSize, Integer pageNumber) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Orders> allAgentInformation = ordersService.getAllAgentInformation(keyword);
        for (Orders ordersr : allAgentInformation) {
            if ("1".equals(ordersr.getExpiry())) {
                ordersr.setExpiry("生效");
            }else {
                ordersr.setExpiry("已过期");
            }
        }
        PageInfo<Orders> pageInfo = new PageInfo<>(allAgentInformation);
        return ResponseEntity.ok(new MsgVo(200, "请求成功", pageInfo));
    }

    @ApiOperation(value = "添加代理订单")
    @UserLoginToken
    @GetMapping("/addAgentOrder")
    public ResponseEntity<MsgVo> addAgentOrder() {
        return null;
    }
}
