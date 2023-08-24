package wakoo.fun.controller.agencyOrderController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.common.Log;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dto.OrdersDto;
import wakoo.fun.log.Constants;
import wakoo.fun.pojo.Agent;
import wakoo.fun.pojo.Orders;
import wakoo.fun.service.AdminAdministrationService;
import wakoo.fun.service.OrdersService.OrdersService;
import wakoo.fun.utils.RoleUtils;
import wakoo.fun.vo.AgentIdrId;
import wakoo.fun.vo.MsgVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


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
    @Resource
    private AdminAdministrationService adminAdministrationService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ApiOperation(value = "查询所有代理订单")
    @UserLoginToken
    @GetMapping("/checkAllAgentOrders")
    public MsgVo checkAllAgentOrders(String keyword, Integer pageSize, Integer pageNumber, HttpServletRequest request) {
        // 获取当前用户id
        Object userId = request.getAttribute("userId");
        // 分页
        PageHelper.startPage(pageNumber, pageSize);
        // 查询数据，调用ordersService的getAllAgentInformation方法，传入keyword和userId参数
        List<Orders> allAgentInformation = ordersService.getAllAgentInformation(keyword, (Integer) userId);
        // 遍历allAgentInformation列表
        for (Orders orders : allAgentInformation) {
            if (orders.getExpiry() != null) {
                // 若orders的expiry属性不为null，调用calculateDaysDifference方法计算expiry与当前日期的天数差，并将结果赋值给s
                String s = calculateDaysDifference(orders.getExpiry());
                // 将s设置为orders的expiry属性
                orders.setExpiry(s);
            } else {
                // 若orders的expiry属性为null，将"永久"设置为orders的expiry属性
                orders.setExpiry("永久");
            }
        }
        // 进行分页，将分页后的数据放入PageInfo对象中
        PageInfo<Orders> pageInfo = new PageInfo<>(allAgentInformation);
        // 返回一个包含成功信息和pageInfo的MsgVo对象
        return new MsgVo(200, "查询成功", pageInfo);
    }

    @ApiOperation(value = "人员下拉框")
    @UserLoginToken
    @GetMapping("/addAgentOrder")
    public MsgVo personnelDropDownBox(HttpServletRequest request) {
        try {
            // 从token中获取id
            Object userId = request.getAttribute("userId");
            // 根据当前用户id获取其角色类型（代理或普通人员）
            Integer rId = ordersService.returnsTheParentId((Integer) userId);
            System.out.println(rId);

            int parentId = 0;
            RoleUtils roleUtils = new RoleUtils();
            List<AgentIdrId> roles = adminAdministrationService.getRoles();
            // 根据角色类型获取其上级的角色id
            parentId = roleUtils.getParentId(parentId, rId, roles);

            List<Agent> agents;
            if (parentId == 2) {
                // 如果上级角色id为2，则表示当前用户为代理，获取当前用户的代理信息
                agents = ordersService.acquisitionPersonnel((Integer) userId);
            } else {
                // 如果上级角色id不为2，则需要继续获取上级的代理信息
                Integer thePreviousLevelRid = ordersService.getThePreviousLevelRid(parentId);
                if (thePreviousLevelRid == null || thePreviousLevelRid == 2) {
                    // 若上级角色id为null或为2，则表示当前用户为代理，获取当前用户的代理信息
                    agents = ordersService.acquisitionPersonnel((Integer) userId);
                } else {
                    // 获取上级角色的代理信息
                    agents = ordersService.acquisitionPersonnel(thePreviousLevelRid);
                }
            }

            // 返回一个包含成功信息和agents的MsgVo对象
            return new MsgVo(200, "请求成功", agents);
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
            // 返回一个包含错误信息的MsgVo对象
            return new MsgVo(500, "请求处理失败", null);
        }
    }


    @ApiOperation(value = "添加代理订单")
    @UserLoginToken
    @Log(modul = "代理订单页面-添加订单", type = Constants.INSERT, desc = "操作添加按钮")
    @PostMapping("/addAgentOrder")
    public MsgVo addAgentOrder(@RequestBody OrdersDto ordersDto) {
        if (ordersDto.getExpiry().isEmpty()) {
            ordersDto.setExpiry(null);
        }
        ordersDto.setRemainingOrder(ordersDto.getTotalQuantity());
        Boolean aBoolean = ordersService.addAgentOrder(ordersDto);
        if (aBoolean) {
            return new MsgVo(200, "添加成功", true);
        }
        return new MsgVo(200, "添加失败", false);
    }

    @ApiOperation(value = "修改回显信息")
    @UserLoginToken
    @GetMapping("/modifyAgentOrder")
    public MsgVo modifyAgentOrder(Integer id) {
        OrdersDto orderById = ordersService.getOrderById(id);
        if (orderById.getExpiry() == null) {
            //如果是空那么就是永久的订单
            orderById.setSelect("1");
        } else {
            // 获取当前时间
            orderById.setSelect("0");
            String s = calculateDaysDifference(orderById.getExpiry());
            orderById.setOutExpiry(s);
        }
        return new MsgVo(200, "请求成功", orderById);
    }

    public static String calculateDaysDifference(String expiryDateStr) {
        // 获取当前日期和时间
        LocalDateTime currentDateTime = LocalDateTime.now();
        // 解析订单到期日期和时间
        LocalDateTime expiryDateTime = LocalDateTime.parse(expiryDateStr, FORMATTER);
        // 计算日期差（包括闰年）
        long daysDifference = ChronoUnit.DAYS.between(currentDateTime.toLocalDate(), expiryDateTime.toLocalDate());

        if (daysDifference <= 0) {
            return "已过期 " + Math.abs(daysDifference) + " 天";
        } else {
            return "距离到期还有 " + daysDifference + " 天";
        }
    }

    @ApiOperation(value = "修改订单信息")
    @UserLoginToken
    @Transactional(rollbackFor = Exception.class)
    @Log(modul = "代理订单页面-修改订单", type = Constants.UPDATE, desc = "操作修改按钮")
    @PutMapping("/modifyOrder")
    public MsgVo modifyOrder(@RequestBody OrdersDto ordersDto) {
        if (ordersDto.getExpiry().isEmpty()) {
            ordersDto.setExpiry(null);
        }
        // 修改订单信息
        Boolean aBoolean = ordersService.modifyOrderInformation(ordersDto);
        if (aBoolean) {
            // 返回一个包含成功信息的MsgVo对象
            return new MsgVo(200, "修改成功", true);
        }

        // 返回一个包含失败信息的MsgVo对象
        return new MsgVo(403, "修改失败", false);
    }

    @ApiOperation(value = "删除订单信息")
    @UserLoginToken
    @Log(modul = "代理订单页面-删除订单", type = Constants.DELETE, desc = "操作软删除按钮")
    @DeleteMapping("/modelOrder")
    public MsgVo modelOrder(@RequestBody Map<String, Integer[]> requestBody,HttpServletRequest request) {
        Integer[] ids = requestBody.get("ids");
        Boolean aBoolean = ordersService.delOrder(ids);
        if (aBoolean) {
            return new MsgVo(200, "删除成功", true);
        }
        return new MsgVo(403, "删除失败", false);
    }
}
