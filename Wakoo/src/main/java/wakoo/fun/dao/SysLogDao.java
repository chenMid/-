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
     * @param roleId  角色id
     * @param keyword 关键词
     * @return 结果集
     */
    List<LogInfo> queryLog(String keyword, Integer roleId,String userName);
}
