package wakoo.fun.dao;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import wakoo.fun.pojo.Audit;
import wakoo.fun.pojo.Orders;
import wakoo.fun.vo.AuditVo;

import java.util.List;
import java.util.Map;

/**
 * @author HASEE
 */
public interface AuditDao {

    /**
     *  获取审核信息列表
     * @param personName 需要查询的姓名
     * @param sex 性别
     * @param age 年龄
     * @param personUser 用户名
     * @param agent 所属人
     * @param studentClass   学位
     * @param createTime 上传时间
     * @param status 审核状态
     * @param roleId 角色id
     * @return List<Audit> 审核实体列表
     */
    List<Audit> getAuditInformation(String personName,
                                    String sex,
                                    String age,
                                    String personUser,
                                    String agent,
                                    String studentClass,
                                    String createTime,
                                    String status,Integer roleId,Integer id);

    /**
     *  获取当前发起的审核
     * @param userId 审核id
     * @return Audit 审核实体
     */
    Audit getAssignedAudit(Integer userId);


    /**
     *  获取当前发起的审核
     * @param userId 审核id
     * @return Audit 审核实体
     */
    Audit getSourceData(Integer userId);

    /**
     *  给用户添加课程
     * @param commonId 用户
     * @param videosId 视频id
     * @return 是否添加成功
     */
    Boolean addLessonsToUsers(Integer commonId,Integer videosId);


    /**
     *  查询指定订单
     * @param ordersId 订单id
     * @return OrderSum 订单信息
     */
    Orders trackTheOrder(Integer ordersId);

    /**
     *  修改指定的订单
     * @param id 订单id
     * @return OrderSum 订单实体
     */
    Boolean modifyAuditOrder(@Param("id") Integer id, @Param("status") Integer status);

    /**
     *  删除指定的一个用户课程
     * @param userId 用户id
     * @param studentClass 学生的班级
     * @return 是否删除成功
     */
    Boolean deletesASpecifiedUserCourse(@Param("userId") Integer userId, @Param("studentClass") Integer studentClass);

    /**
     *  添加合同表
     * @param  auditVo 路径
     * @return 是否添加成功
     */
    Boolean addTheContractIntermediateTable(AuditVo auditVo);

    /**
     *  获取合同表的数据
     * @param auditId 审核id
     * @return   List<Map<String, String>> 合同列表
     */
    @MapKey("fileList")
    List<Map<String,String>> obtainAContract(Integer auditId);

    /**
     *  删除合同表
     * @param uId  用户id
     * @return 是否删除成功
     */
    Boolean deletePictureContract(String uId);
}
