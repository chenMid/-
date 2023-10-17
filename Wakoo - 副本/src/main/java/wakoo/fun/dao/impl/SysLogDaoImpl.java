package wakoo.fun.dao.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.dao.SysLogDao;
import wakoo.fun.log.LogErrorInfo;
import wakoo.fun.log.LogInfo;
import wakoo.fun.mapper.SysLogMapper;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysLogDaoImpl implements SysLogDao {
    @Resource
    private SysLogMapper sysLogMapper;

    @Override
    public void save(LogInfo logInfo) {
        sysLogMapper.save(logInfo);
    }

    @Override
    public void error(LogErrorInfo logErrorInfo) {
        sysLogMapper.error(logErrorInfo);
    }

    @Override
    public List<LogInfo> queryLog(String keyword,String userName, String module, String type, String ip, String version, String createTime,Integer roleId,Integer userId) {
        return sysLogMapper.queryLog(keyword,userName, module, type, ip, version, createTime,roleId,userId);
    }

    @Override
    public Boolean delLog(String[] ids) {
        return sysLogMapper.delLog(ids);
    }

    @Override
    public List<LogErrorInfo> queryErrorLog(String keyword,Integer roleId,String userName,String ip,String version,String createTime,Integer userId) {
        return sysLogMapper.queryErrorLog( keyword,roleId, userName, ip , version ,createTime,userId);
    }

    @Override
    public Boolean delErrorLog(String[] ids) {
        return sysLogMapper.delErrorLog(ids);
    }

    @Override
    public LogInfo detailLog(String logId) {
        return sysLogMapper.detailLog(logId);
    }

    @Override
    public LogErrorInfo detailExceptionLog(String logErrorId) {
        return sysLogMapper.detailExceptionLog(logErrorId);
    }
}
