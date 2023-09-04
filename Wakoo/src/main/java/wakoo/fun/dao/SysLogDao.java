package wakoo.fun.dao;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.log.LogErrorInfo;
import wakoo.fun.log.LogInfo;

import java.util.List;

/**
 * @author HASEE
 */
public interface SysLogDao {
    /**
     * 保存系统日志实体 *
     * @param logInfo 日志实体
     **/
    void save(LogInfo logInfo);

    /**
     * 记录错误信息
     *
     * @param logErrorInfo 错误日志信息
     */
    void error(@Param("sysLog") LogErrorInfo logErrorInfo);

    /**
     * 查询后台日志
     *
     * @return 结果集
     */
    List<LogInfo> queryLog(String userName,String module,String type,String ip,String version,String createTime,Integer roleId,Integer userId);
    /**
     * 删除日志
     *
     * @param ids 所需删除的日志id
     * @return Boolean
     */
    Boolean delLog(String[] ids);

    /**
     * 查询后台日志
     *
     * @param roleId   角色id
     * @param userName 用户名
     * @return 结果集
     */
    List<LogErrorInfo> queryErrorLog(Integer roleId,String userName,String ip,String version,String createTime,Integer userId);

    /**
     * 删除日志
     *
     * @param ids 所需删除的日志id
     * @return Boolean
     */
    Boolean delErrorLog(@Param("ids") String[] ids);

    /**
     * 根据id查询日志对象
     *
     * @param logId 日志id
     * @return 日志实体
     */
    LogInfo detailLog(@Param("logId") String logId);

    /**
     * 根据id查询日志对象
     *
     * @param logErrorId 日志id
     * @return 日志实体
     */
    LogErrorInfo detailExceptionLog(@Param("logErrorId") String logErrorId);
}
