package wakoo.fun.service.PersonUserService.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import wakoo.fun.mapper.PersonUserMapper;
import wakoo.fun.pojo.Agent;
import wakoo.fun.pojo.Orders;
import wakoo.fun.pojo.PersonUser;
import wakoo.fun.service.PersonUserService.PersonUserService;
import wakoo.fun.vo.PersonUserVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * PersonUser实现类
 * @author HASEE
 */
@Service
public class PersonUserServiceImpl implements PersonUserService {
    private static Lock lock = new ReentrantLock();
    @Resource
    private PersonUserMapper personUserMapper;

    @Override
    public List<PersonUser> getRegularUsers(String keyword, Integer userId,Integer status,Integer number) {
        return personUserMapper.getRegularUsers(keyword, userId,status,number);
    }

    @Override
    public Agent getPersonUserByUserIdAnd(Integer id) {
        return personUserMapper.getPersonUserByUserIdAnd(id);
    }

    @Override
    public Boolean addCommonUser(PersonUser personUser) {
        return personUserMapper.addCommonUser(personUser);
    }

    @Override
    public Boolean addAgentsAndHumanRelationships(Integer personId, Integer commonId) {
        return personUserMapper.addAgentsAndHumanRelationships(personId, commonId );
    }

    @Override
    public String getThePhoneNumber(String iphone) {
        return personUserMapper.getThePhoneNumber(iphone);
    }

    @Override
    public PersonUser theCommandOutputIsModified(Integer id) {
        return personUserMapper.theCommandOutputIsModified(id);
    }

    @Override
    public Boolean modifyingCommonUser(PersonUser personUser) {
        return personUserMapper.modifyingCommonUser(personUser);
    }

    @Override
    public PersonUser queryByMobilePhoneNumber(String iphone) {
        return personUserMapper.queryByMobilePhoneNumber(iphone);
    }

    @Override
    public List<Agent> acquireOtherThanPersonnel(Integer userId, Integer parentId,String iphone) {
        return personUserMapper.acquireOtherThanPersonnel(userId, parentId,iphone);
    }

    @Override
    public List<Agent> purchaser(Integer personId) {
        return personUserMapper.purchaser(personId);
    }

    @Override
    public List<PersonUserVo> inquireAboutTheOwnersCourse(Integer campusId,Integer cid) {
        return personUserMapper.inquireAboutTheOwnersCourse(campusId,cid);
    }
    @Override
    @Transactional
    public Boolean addPurchaseCourse(PersonUserVo personUserVo,HttpServletRequest request) {
        Boolean aBoolean = false;
        lock.lock();
        try {
            aBoolean = personUserMapper.addPurchaseCourse(personUserVo);
            if (aBoolean){
                Orders orders = personUserMapper.checkOrderStatus(personUserVo.getId());
                request.setAttribute("orderId", orders.getId());
                int qty=orders.getNumberOfUse()+1;
                int number=orders.getTotalQuantity();
                int rqty=number-qty;

//                if (rqty==0 || rqty< 0){
//                    personUserMapper.modifyOrderStatusOr(orders.getId());
//                }

                Boolean aBoolean1 = personUserMapper.modifyOrderStatus(number,qty,rqty, orders.getId());
                if (aBoolean1 == null || !aBoolean1) {
                    // 手动触发事务回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    aBoolean = false;
                }
            }
        } catch (Exception e) {
            // 手动触发事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            aBoolean = false;
        } finally {
            lock.unlock();
        }
        return aBoolean;
        }

    @Override
    public List<PersonUserVo> accessExistingCourses(Integer campusId) {
        return personUserMapper.accessExistingCourses(campusId);
    }

    @Override
    public Boolean addAudit(Integer id, Integer sex, Integer age, String userName, Integer agent, Integer studentClass,Integer orderId) {
        return personUserMapper.addAudit(id,sex,age,userName,agent,studentClass,orderId);
    }

    @Override
    public Boolean softDeleteUser(Integer[] ids,Integer status) {
        return personUserMapper.softDeleteUser(ids,status);
    }

    @Override
    public List<PersonUser> queryUsersBasedOnMultipleCriteria(String classname, String iphone, String agentName, Integer sex, Integer age, Integer userId,Integer number,Integer status) {
        return personUserMapper.queryUsersBasedOnMultipleCriteria(classname, iphone, agentName, sex, age, userId,number,status);
    }

}
