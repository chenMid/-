package wakoo.fun.controller.agencyOrderController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dto.AdminAdministraltion;
import wakoo.fun.dto.OrdersDto;
import wakoo.fun.pojo.Agent;
import wakoo.fun.pojo.Orders;
import wakoo.fun.service.OrdersService.OrdersService;
import wakoo.fun.service.VideosService.VideosService;
import wakoo.fun.vo.MsgVo;
import wakoo.fun.vo.SubclassVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ApiOperation(value = "查询所有代理订单")
    @UserLoginToken
    @GetMapping("/checkAllAgentOrders")
    public ResponseEntity<MsgVo> checkAllAgentOrders(String keyword, Integer pageSize, Integer pageNumber, HttpServletRequest request) {
        //获取当前用户id
        Object userId = request.getAttribute("userId");
        System.out.println(userId);
        //分页
        PageHelper.startPage(pageNumber, pageSize);
        //查询数据
        List<Orders> allAgentInformation = ordersService.getAllAgentInformation(keyword, (Integer) userId);
        for (Orders orders:allAgentInformation) {
            if (orders.getExpiry()!=null){
                String s = calculateDaysDifference(orders.getExpiry());
                orders.setExpiry(s);
            }else {
                orders.setExpiry("永久");
            }
        }
        //进行分页
        PageInfo<Orders> pageInfo = new PageInfo<>(allAgentInformation);
        //  响应消息
        return ResponseEntity.ok(new MsgVo(200, "请求成功", pageInfo));
    }

    @ApiOperation(value = "人员下拉框")
    @UserLoginToken
    @GetMapping("/addAgentOrder")
    public ResponseEntity<MsgVo> personnelDropDownBox(HttpServletRequest request) {
        //从token获取id
        Object userId = request.getAttribute("userId");
        // 判断当前用户的类型是代理还是的人员
        Integer rId = ordersService.returnsTheParentId((Integer) userId);
        //  返回代理
        List<Agent> agents;
        if (rId == 3) {
            // 获取当前用户的代理信息
            agents = ordersService.acquisitionPersonnel((Integer) userId);
        } else {
            // 获取当前用户的代理信息
            Integer thePreviousLevelRid = ordersService.getThePreviousLevelRid(rId);
            if (thePreviousLevelRid == null || thePreviousLevelRid == 3) {
                // 当前用户的为代理
                agents = ordersService.acquisitionPersonnel((Integer) userId);
            } else {
                // 获取当前用户的代理信息
                agents = ordersService.acquisitionPersonnel(thePreviousLevelRid);
            }
        }
        return ResponseEntity.ok(new MsgVo(200, "请求成功", agents));
    }


    @ApiOperation(value = "添加代理订单")
    @UserLoginToken
    @PostMapping("/addAgentOrder")
    public ResponseEntity<MsgVo> addAgentOrder(@RequestBody OrdersDto ordersDto) {
        ordersDto.setRemainingOrder(ordersDto.getTotalQuantity());
        Boolean aBoolean = ordersService.addAgentOrder(ordersDto);
        if (aBoolean) {
            return ResponseEntity.ok(new MsgVo(200, "添加成功", true));
        }
        return ResponseEntity.ok(new MsgVo(200, "添加失败", false));
    }

    @ApiOperation(value = "修改回显信息")
    @UserLoginToken
    @GetMapping("/modifyAgentOrder")
    public ResponseEntity<MsgVo> modifyAgentOrder(Integer id) {
        OrdersDto orderById = ordersService.getOrderById(id);
        if (orderById.getExpiry() == null){
            //如果是空那么就是永久的订单
            orderById.setExpiry("永久");
        }else {
            // 获取当前时间
            String s = calculateDaysDifference(orderById.getExpiry());
            orderById.setExpiry(s);
        }
        return ResponseEntity.ok(new MsgVo(200, "请求成功", orderById));
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
    @PutMapping("/modifyOrder")
    public ResponseEntity<MsgVo> modifyOrder(@RequestBody OrdersDto ordersDto) {
        OrdersDto orderById = ordersService.getOrderById(ordersDto.getId());
        if (ordersDto.getExpiry()!=null){
            if (ordersDto.getStatus()==0){
                return ResponseEntity.ok(new MsgVo(403,"状态不能为失效",false));
            }else if (ordersDto.getStatus().equals(orderById.getStatus())&& orderById.getExpiry().equals(ordersDto.getExpiry())){
                return ResponseEntity.ok(new MsgVo(403,"请先修改信息在执行",false));
            }
        }else {
            if (orderById.getExpiry()==null){
                if (ordersDto.getStatus().equals(orderById.getStatus())){
                    return ResponseEntity.ok(new MsgVo(403,"请先修改信息在执行",false));
                }
            }
        }
        Boolean aBoolean = ordersService.modifyOrderInformation(ordersDto);
        if (aBoolean) {
            return ResponseEntity.ok(new MsgVo(200, "修改成功", true));
        }
        return ResponseEntity.ok(new MsgVo(200, "修改失败", false));
    }


}
