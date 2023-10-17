package wakoo.fun.dao.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.dao.AuditDao;
import wakoo.fun.mapper.AuditMapper;
import wakoo.fun.pojo.Audit;
import wakoo.fun.pojo.Orders;
import wakoo.fun.vo.AuditVo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class AuditDaoImpl implements AuditDao {
    @Resource
    private AuditMapper auditMapper;

    @Override
    public List<Audit> getAuditInformation(String personName,
                                           String sex,
                                           String age,
                                           String personUser,
                                           String agent,
                                           String studentClass,
                                           String createTime,
                                           String status,Integer roleId,Integer id) {
        return auditMapper.getAuditInformation(personName,
                sex,
                age,
                personUser,
                agent,
                studentClass,
                createTime,
                status,roleId,id);
    }

    @Override
    public Audit getAssignedAudit(Integer userId) {
        return auditMapper.getAssignedAudit(userId);
    }

    @Override
    public Audit getSourceData(Integer userId) {
        return auditMapper.getSourceData(userId);
    }

    @Override
    public Boolean addLessonsToUsers(Integer commonId, Integer videosId) {
        return auditMapper.addLessonsToUsers(commonId, videosId);
    }

    @Override
    public Orders trackTheOrder(Integer ordersId) {
        return auditMapper.trackTheOrder(ordersId);
    }

    @Override
    public Boolean modifyAuditOrder(Integer id, Integer status) {
        return auditMapper.modifyAuditOrder(id, status);
    }

    @Override
    public Boolean deletesASpecifiedUserCourse(Integer userId, Integer studentClass) {
        return auditMapper.deletesASpecifiedUserCourse(userId, studentClass);
    }

    @Override
    public Boolean addTheContractIntermediateTable(AuditVo auditVo) {
        return auditMapper.addTheContractIntermediateTable(auditVo);
    }

    @Override
    public List<Map<String, String>> obtainAContract(Integer auditId) {
        return auditMapper.obtainAContract(auditId);
    }

    @Override
    public Boolean deletePictureContract(String uId) {
        return auditMapper.deletePictureContract(uId);
    }
}
