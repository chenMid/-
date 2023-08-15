package wakoo.fun.service.PersonUserService.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.mapper.PersonUserMapper;
import wakoo.fun.pojo.Agent;
import wakoo.fun.pojo.PersonUser;
import wakoo.fun.service.PersonUserService.PersonUserService;

import javax.annotation.Resource;
import java.util.List;

/**
 * PersonUser实现类
 * @author HASEE
 */
@Service
public class PersonUserServiceImpl implements PersonUserService {
    @Resource
    private PersonUserMapper personUserMapper;

    @Override
    public List<PersonUser> getRegularUsers(String keyword, Integer userId) {
        return personUserMapper.getRegularUsers(keyword, userId );
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
    public List<Agent> acquireOtherThanPersonnel(Integer userId, String iphone) {
        return personUserMapper.acquireOtherThanPersonnel(userId, iphone );
    }
}
