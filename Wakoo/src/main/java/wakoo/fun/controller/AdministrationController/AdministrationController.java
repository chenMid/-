package wakoo.fun.controller.AdministrationController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dto.AdminAdministraltion;
import wakoo.fun.dto.AdmininistraltionDto;
import wakoo.fun.dto.MsgVo;
import wakoo.fun.pojo.FaAdmin;
import wakoo.fun.service.AdminAdministrationService;
import wakoo.fun.service.FaAdminService;
import wakoo.fun.utils.MsgUtils;

import javax.annotation.Resource;
import java.util.List;

@EnableTransactionManagement//数据库事务管理
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "Administration")
public class AdministrationController {

    @Resource
    private AdminAdministrationService adminAdministrationService;
    @Resource
    private FaAdminService faAdminService;

    @ApiOperation(value = "管理员管理查询")
    @ApiResponses({
            @ApiResponse(responseCode = "500",description = "请联系管理员"),
            @ApiResponse(responseCode = "200",description = "响应成功")
    })
    @UserLoginToken
    @GetMapping("Administration")
    public MsgVo Administration(String keyword,Integer pageSize,Integer pageNumber){
        PageHelper.startPage(pageNumber, pageSize);

        List<AdminAdministraltion> allAdministraltion = adminAdministrationService.getAllAdministraltion(keyword);

        PageInfo<AdminAdministraltion> pageInfo=new PageInfo<>(allAdministraltion);
        return new MsgVo(200,"请求成功",pageInfo);
    }


    @ApiOperation(value = "管理员管理添加")
    @ApiResponses({
            @ApiResponse(responseCode = "500",description = "请联系管理员"),
            @ApiResponse(responseCode = "200",description = "响应成功")
    })
    @UserLoginToken
    @GetMapping("getRole")
    public MsgVo getRole(){
        return new MsgVo(MsgUtils.SUCCESS,adminAdministrationService.getRole());
    }

    @ApiOperation(value = "管理员管理添加")
    @ApiResponses({
            @ApiResponse(responseCode = "500",description = "请联系管理员"),
            @ApiResponse(responseCode = "200",description = "响应成功")
    })
    @UserLoginToken
    @GetMapping("getOrderQuant")
    public MsgVo getOrderQuant(){
        return new MsgVo(MsgUtils.SUCCESS,adminAdministrationService.getOrderQ());
    }

    @ApiOperation(value = "管理员管理添加")
    @ApiResponses({
            @ApiResponse(responseCode = "500",description = "请联系管理员"),
            @ApiResponse(responseCode = "200",description = "响应成功")
    })
    @Transactional
    @UserLoginToken
    @PostMapping("addAdminUser")
    public MsgVo addAdminUser(@RequestBody AdmininistraltionDto admininistraltionDto){
        try{
            Boolean userAdmin = adminAdministrationService.isUserAdmin(admininistraltionDto);
            if (userAdmin){
                List<FaAdmin> faAdmins = faAdminService.faAdmin(admininistraltionDto.getUsername());
                if (faAdmins.size()!=0){
                    adminAdministrationService.isUserRoleOrder(faAdmins.get(0).getId(), admininistraltionDto.getRoleId(),admininistraltionDto.getCampusId());
                    return new MsgVo(MsgUtils.SUCCESS,userAdmin);
                }
            }
            return new MsgVo(MsgUtils.FAILED);
        }catch (Exception e) {
            // 在发生异常时进行事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new MsgVo(MsgUtils.FAILED);
        }
    }

    @ApiOperation(value = "管理员管理修改状态")
    @ApiResponses({
            @ApiResponse(responseCode = "500",description = "请联系管理员"),
            @ApiResponse(responseCode = "200",description = "响应成功")
    })
    @UserLoginToken
    @PutMapping("Updstatus")
    public MsgVo Updstatus(Integer id,String status){
        Boolean aBoolean = adminAdministrationService.UpdStatus(id, status);
        return new MsgVo(MsgUtils.SUCCESS,aBoolean);
    }
}
