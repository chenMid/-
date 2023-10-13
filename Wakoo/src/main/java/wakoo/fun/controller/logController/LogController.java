package wakoo.fun.controller.logController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.common.Log;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dao.SysLogDao;
import wakoo.fun.dto.AdminAdministraltion;
import wakoo.fun.log.Constants;
import wakoo.fun.log.LogErrorInfo;
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
import java.util.Map;
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
    public MsgVo getLog(HttpServletRequest request,String keyword,Integer pageSize, Integer pageNumber,String module,String type,String userName,String ip,String version,String createTime){
        return getLogInfo(request,keyword, pageSize, pageNumber, false,module, type , userName , ip, version , createTime);
    }


    @ApiOperation(value = "获取异常日志")
    @UserLoginToken
    @GetMapping("/getErrorLog")
    public MsgVo getErrorLog(HttpServletRequest request,String keyword,Integer pageSize, Integer pageNumber,String module,String type,String userName,String ip,String version,String createTime){
        return getLogInfo(request, keyword,pageSize, pageNumber, true, module, type , userName , ip, version , createTime );
    }

    private MsgVo getLogInfo(HttpServletRequest request ,String keyword,Integer pageSize, Integer pageNumber, boolean isErrorLog,String module,String type,String userName,String ip,String version,String createTime) {
        // 分页配置
        pageNumber = Math.max(pageNumber, 1);

        PageHelper.startPage(pageNumber, pageSize);
        // 获取登录用户信息
        Integer userId = (Integer) request.getAttribute("userId");
        FaAdmin faAdmin = adminAdministrationService.getFaAdmin(userId);
        List<AgentIdrId> roles = adminAdministrationService.getRoles();
        Integer roleId = adminAdministrationService.getsTheIdOfTheRole(userId);
        RoleUtils roleUtils = new RoleUtils();
        int parentId = roleUtils.getParentId(0, roleId, roles);

        // 查询日志或异常日志
        List<?> logList;
        if (isErrorLog) {
            logList = sysLogDao.queryErrorLog(keyword,parentId,userName,ip,version,createTime,userId);
        } else {
            logList = sysLogDao.queryLog(keyword,userName, module, type, ip, version, createTime,parentId,userId);
        }

        PageInfo<?> pageInfo = new PageInfo<>(logList);
        pageInfo.setPageSize(pageSize);

        return new MsgVo(200, "查询成功", pageInfo);
    }

    @ApiOperation(value = "删除日志")
    @UserLoginToken
    @DeleteMapping("/delLog")
    public MsgVo delLog(@RequestBody Map<String, String[]> requestBody) {
        String[] ids = requestBody.get("ids");
        Boolean aBoolean = sysLogDao.delLog(ids);
        if (aBoolean) {
            return new MsgVo(200, "删除成功", true);
        }
        return new MsgVo(403, "删除失败", false);
    }

    @ApiOperation(value = "删除异常日志")
    @UserLoginToken
    @DeleteMapping("/delErrorLog")
    public MsgVo delErrorLog(@RequestBody Map<String, String[]> requestBody) {
        String[] ids = requestBody.get("ids");
        Boolean aBoolean = sysLogDao.delErrorLog(ids);
        if (aBoolean) {
            return new MsgVo(200, "删除成功", true);
        }
        return new MsgVo(403, "删除失败", false);
    }

    @ApiOperation(value = "详情日志")
    @UserLoginToken
    @GetMapping("/detailLog")
    public MsgVo detailLog(String logId) {
        LogInfo logInfo = sysLogDao.detailLog(logId);
        return new MsgVo(200, "查询成功", logInfo);
    }

    @ApiOperation(value = "详情异常日志")
    @UserLoginToken
    @GetMapping("/detailExceptionLog")
    public MsgVo detailExceptionLog(String logErrorId) {
        LogErrorInfo logErrorInfo = sysLogDao.detailExceptionLog(logErrorId);
        return new MsgVo(200, "查询成功", logErrorInfo);
    }
}
