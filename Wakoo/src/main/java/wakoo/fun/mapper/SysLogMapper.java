package wakoo.fun.mapper;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.log.LogErrorInfo;
import wakoo.fun.log.LogInfo;

import java.util.List;

/**
 * @author HASEE
 */
public interface SysLogMapper {
    /**
     * 保存系统日志实体 *
     *
     * @param logInfo 日志实体
     **/
    void save(@Param("sysLog") LogInfo logInfo);

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
    List<LogInfo> queryLog(@Param("keyword") String keyword,@Param("userName") String userName, @Param("module") String module, @Param("type") String type, @Param("ip") String ip, @Param("version") String version, @Param("createTime") String createTime, @Param("roleId") Integer roleId, @Param("userId") Integer userId);

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
    List<LogErrorInfo> queryErrorLog(@Param("keyword") String keyword, @Param("roleId") Integer roleId, @Param("userName") String userName, @Param("ip") String ip, @Param("version") String version, @Param("createTime") String createTime, @Param("userId") Integer userId);

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
