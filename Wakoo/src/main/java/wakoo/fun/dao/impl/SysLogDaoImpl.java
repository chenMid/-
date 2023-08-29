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
    public List<LogInfo> queryLog(String keyword, Integer roleId,String userName) {
        return sysLogMapper.queryLog(keyword, roleId,userName);
    }
}
