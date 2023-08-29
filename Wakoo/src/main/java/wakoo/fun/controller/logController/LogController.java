package wakoo.fun.controller.logController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wakoo.fun.common.Log;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dao.SysLogDao;
import wakoo.fun.dto.AdminAdministraltion;
import wakoo.fun.log.Constants;
import wakoo.fun.log.LogInfo;
import wakoo.fun.pojo.FaAdmin;
import wakoo.fun.service.AdminAdministrationService;
import wakoo.fun.service.OrdersService.OrdersService;
import wakoo.fun.utils.RoleUtils;
import wakoo.fun.vo.AgentIdrId;
import wakoo.fun.vo.MsgVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * 日志
 * @author HASEE
 */
@EnableTransactionManagement
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "管理员日志")
public class LogController {

    @Resource
    private SysLogDao sysLogDao;
    @Resource
    private AdminAdministrationService adminAdministrationService;

    @ApiOperation(value = "获取日志")
    @UserLoginToken
    @GetMapping("/getLog")
    public MsgVo getLog(HttpServletRequest request, String keyword, Integer pageSize, Integer pageNumber){
        PageHelper.startPage(pageNumber, pageSize);
        RoleUtils roleUtils = new RoleUtils();
        Object userId = request.getAttribute("userId");
        List<AgentIdrId> roles = adminAdministrationService.getRoles();
        Integer role = adminAdministrationService.getsTheIdOfTheRole((Integer) userId);
        FaAdmin faAdmin = adminAdministrationService.getFaAdmin((Integer) userId);
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("Generated ID: " + id);
        int parentId=0;
        // 根据角色类型获取其上级的角色id
        parentId = roleUtils.getParentId(parentId, role, roles);
        List<LogInfo> queryLog = sysLogDao.queryLog(keyword, parentId,faAdmin.getUserName());
        PageInfo<LogInfo> pageInfo = new PageInfo<>(queryLog);
        pageInfo.setPageSize(pageSize);
        return new MsgVo(200,"查询成功",pageInfo);
    }

}
