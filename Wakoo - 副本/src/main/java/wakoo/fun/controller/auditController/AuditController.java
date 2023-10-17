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
import wakoo.fun.vo.AuditVo;
import wakoo.fun.vo.MsgVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

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
                                 String status, Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        Object userId = request.getAttribute("userId");
        int parentId = 0;
        RoleUtils roleUtils = new RoleUtils();
        List<AgentIdrId> roles = adminAdministrationService.getRoles();
        // 根据角色类型获取其上级的角色id
        Integer rId = ordersService.returnsTheParentId((Integer) userId);
        List<Audit> list;
        parentId = roleUtils.getParentId(parentId, rId, roles);
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
        //从数据库中拿到用户的合同图片路径
        List<Map<String, String>> maps = auditDao.obtainAContract(auditId);
        //创建一个fileList1集合用来存储循环结果
        List<Map<String, String>> fileList1= new ArrayList<>();
        //创建一个map用来存储fileList1
        Map<String, Object> fileList = new HashMap<>(50);
        for (Map<String, String> map : maps) {
            System.out.println(map);
            //拿到值
            String auditPath = map.get("auditPaht");
            //进行值的分割
            String fileName = auditPath.substring(auditPath.lastIndexOf("/") + 1);
            //创建个map用来存储前端需要的格式
            Map<String, String> fileItem = new HashMap<>(50);
            fileItem.put("uId", map.get("uId"));
            fileItem.put("name", fileName);
            fileItem.put("url", "https://" + auditPath);
            //存储到fileList1里面
            fileList1.add(fileItem);
        }
        //把用户信息以及前端需要的格式内容
        fileList.put("assignedAudit",assignedAudit);
        fileList.put("fileList", fileList1);
        return new MsgVo(200, "查询成功", fileList);
    }


    @ApiOperation(value = "合同添加")
    @UserLoginToken
    @Transactional(rollbackFor = Exception.class)
    @Log(modul = "审核页面-合同添加", type = Constants.SPECIAL, desc = "操作添加合同")
    @PostMapping("/contractAddition")
    public MsgVo contractAddition(@RequestParam(value = "file") MultipartFile file, Integer id,String uId) throws IOException {
        MsgVo msgVo = QiniuUtils.uploadAvatar(file, accessKey, secretKey, bucketName, "2023.6.21编程分类视频及图片/test");
        AuditVo auditVo=new AuditVo(id, (String) msgVo.getData(),uId);
        Boolean aBoolean = auditDao.addTheContractIntermediateTable(auditVo);
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
    public MsgVo auditErrorCallbacks(@RequestBody Audit audit) throws IOException {
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
                        return new MsgVo(200, "修改成功", true);
                    }
                } else {
                    return new MsgVo(403,"修改失败",false);
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
                        return new MsgVo(200, "修改成功", true);
                    }
                }else {
                    return new MsgVo(403,"修改失败",false);
                }
            }
        }
        return null;
    }

    @ApiOperation(value = "删除图片")
    @UserLoginToken
    @Transactional(rollbackFor = Exception.class)
    @Log(modul = "审核页面-删除图片合同", type = Constants.UPDATE, desc = "操作删除图片按钮")
    @DeleteMapping("/deletePicture")
    public MsgVo deletePicture(@RequestBody Map<String, String> requestBody){
        String uId = requestBody.get("uId");
        Boolean aBoolean = auditDao.deletePictureContract(uId);
        if (aBoolean){
            return new MsgVo(200,"删除成功",true);
        }else {
            return new MsgVo(403,"删除失败",false);
        }
    }
}
