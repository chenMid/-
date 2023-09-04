package wakoo.fun.controller.auditController;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wakoo.fun.common.Log;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dao.AuditDao;
import wakoo.fun.log.Constants;
import wakoo.fun.mapper.PersonUserMapper;
import wakoo.fun.pojo.Audit;
import wakoo.fun.pojo.Orders;
import wakoo.fun.service.AdminAdministrationService;
import wakoo.fun.service.OrdersService.OrdersService;
import wakoo.fun.utils.QiniuUtils;
import wakoo.fun.utils.RoleUtils;
import wakoo.fun.vo.AgentIdrId;
import wakoo.fun.vo.MsgVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户审核
 *
 * @author HASEE
 */
@EnableTransactionManagement
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "权限页面")
public class AuditController {
    @Value("${qiniu.access-key}")// 七牛云存储的访问密钥
    private String accessKey;
    @Value("${qiniu.secret-key}")// 七牛云存储的秘钥
    private String secretKey;
    @Value("${qiniu.bucket-name}")// 存储空间名称
    private String bucketName;

    @Resource
    private AuditDao auditDao;

    @Resource
    private AdminAdministrationService adminAdministrationService;

    @Resource
    private OrdersService ordersService;

    @Resource
    private PersonUserMapper personUserMapper;

    @ApiOperation(value = "查询所有审核")
    @UserLoginToken
    @GetMapping("/queryAllReviews")
    public MsgVo queryAllReviews(HttpServletRequest request,
                                 String personName,
                                 String sex,
                                 String age,
                                 String personUser,
                                 String agent,
                                 String studentClass,
                                 String createTime,
                                 String status, Integer pageNum, Integer pageSize) {
        pageNum = Math.max(pageNum, 1);
        PageHelper.startPage(pageNum, pageSize);
        Object userId = request.getAttribute("userId");
        int parentId = 0;
        RoleUtils roleUtils = new RoleUtils();
        List<AgentIdrId> roles = adminAdministrationService.getRoles();
        // 根据角色类型获取其上级的角色id
        Integer rId = ordersService.returnsTheParentId((Integer) userId);
        List<Audit> list;
        parentId = roleUtils.getParentId(parentId, rId, roles);
        System.out.println(parentId);
        if (parentId == 2) {
            list = auditDao.getAuditInformation(personName, sex, age, personUser, agent, studentClass, createTime, status, 3, (Integer) userId);
        } else if (parentId == 3) {
            list = auditDao.getAuditInformation(personName, sex, age, personUser, agent, studentClass, createTime, status, 4, (Integer) userId);
        } else {
            list = auditDao.getAuditInformation(personName, sex, age, personUser, agent, studentClass, createTime, status, 1, (Integer) userId);
        }
        PageInfo<Audit> page = new PageInfo<>(list);
        page.setPageSize(pageSize);
        return new MsgVo(200, "查询成功", page);
    }

    @ApiOperation(value = "审核修改回显")
    @UserLoginToken
    @GetMapping("/reviewTheModifiedOutput")
    public MsgVo reviewTheModifiedOutput(Integer auditId) {
        Audit assignedAudit = auditDao.getAssignedAudit(auditId);
        return new MsgVo(200, "查询成功", assignedAudit);
    }


    @ApiOperation(value = "合同添加")
    @UserLoginToken
    @Log(modul = "审核页面-合同添加", type = Constants.INSERT, desc = "操作添加合同")
    @PostMapping("/contractAddition")
    public MsgVo contractAddition(@RequestPart MultipartFile file, String folderPath, Integer id) throws IOException {
        MsgVo msgVo = QiniuUtils.uploadAvatar(file, accessKey, secretKey, bucketName, folderPath);
        Boolean aBoolean = auditDao.additionContract((String) msgVo.getData(), id);
        if (aBoolean) {
            return new MsgVo(200, "添加合同成功", true);
        } else {
            return new MsgVo(403, "添加合同失败", false);
        }
    }

    @ApiOperation(value = "审核错误回调")
    @UserLoginToken
    @Transactional(rollbackFor = Exception.class)
    @Log(modul = "审核页面-审核修改回调", type = Constants.UPDATE, desc = "操作修改回调按钮")
    @PutMapping("/auditErrorCallbacks")
    public MsgVo auditErrorCallbacks(@RequestBody Audit audit) {
        //获取审核的id
        Audit assignedAudit = auditDao.getSourceData(audit.getId());
        if (audit.getStatus().equals(assignedAudit.getStatus())) {
            return new MsgVo(200, "修改成功", true);
        } else {
            //获取订单信息
            Orders orders = auditDao.trackTheOrder(assignedAudit.getOrderId());
            if (audit.getStatus() == 1) {
                if (orders!=null) {
                    int qty = orders.getNumberOfUse() + 1;
                    int number = orders.getTotalQuantity();
                    int rqty = number - qty;
                    auditDao.addLessonsToUsers(Integer.parseInt(assignedAudit.getPersonName()), Integer.parseInt(assignedAudit.getStudentClass()));
                    personUserMapper.modifyOrderStatus(number, qty, rqty, assignedAudit.getOrderId());
                    Boolean aBoolean = auditDao.modifyAuditOrder(audit.getId(), audit.getStatus());
                    if (aBoolean) {
                        return new MsgVo(200, "数据回调成功", true);
                    }
                } else {
                    return new MsgVo(403,"订单已失效无法回调",false);
                }
            }else {
                if (orders!=null){
                    int qty = orders.getNumberOfUse() - 1;
                    int number = orders.getTotalQuantity();
                    int rqty = number - qty;
                    personUserMapper.modifyOrderStatus(number, qty, rqty, assignedAudit.getOrderId());
                    auditDao.deletesASpecifiedUserCourse(Integer.parseInt(assignedAudit.getAgent()), Integer.parseInt(assignedAudit.getStudentClass()));
                    Boolean aBoolean = auditDao.modifyAuditOrder(audit.getId(), 0);
                    if (aBoolean) {
                        return new MsgVo(200, "数据回调成功", true);
                    }
                }else {
                    return new MsgVo(403,"订单已失效无法回调",false);
                }
            }
        }
        return null;
    }
}
