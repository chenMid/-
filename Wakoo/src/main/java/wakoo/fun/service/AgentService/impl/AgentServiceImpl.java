package wakoo.fun.service.AgentService.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.dto.AgentDto;
import wakoo.fun.mapper.AgentMapper;
import wakoo.fun.pojo.Advert;
import wakoo.fun.pojo.Agent;
import wakoo.fun.service.AgentService.AgentService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AgentServiceImpl implements AgentService {
    @Resource
    private AgentMapper agentMapper;

    @Override
    public List<AgentDto> listAdcert(String keyword,Integer id,Integer status) {
        return agentMapper.listAdcert(keyword,id,status);
    }

    @Override
    public Boolean addAgent(Agent agent) {
        return agentMapper.addAgent(agent);
    }

    @Override
    public Boolean isEmailDuplicated(String email) {
        return agentMapper.isEmailDuplicated(email) >0;
    }

    @Override
    public Boolean isUsernameDuplicated(String name) {
        return agentMapper.isUsernameDuplicated(name) >0;
    }

    @Override
    public Boolean isContactPhone(String phone) {
        return agentMapper.isContactPhone(phone)>0;
    }

    @Override
    public AgentDto retrieveProxyInfo(Integer getProxyId) {
        return agentMapper.retrieveProxyInfo(getProxyId);
    }

    @Override
    public Boolean updRegent(AgentDto agent) {
        return agentMapper.updRegent(agent);
    }

    @Override
    public Boolean addARoleUser(Integer userId, Integer agentId, Integer roleId) {
        return agentMapper.addARoleUser(userId, agentId, roleId );
    }

    @Override
    public Boolean modifyRoleAgent(Integer agentId, Integer roleId) {
        return agentMapper.modifyRoleAgent(agentId, roleId);
    }

    @Override
    public Boolean alterTheState(Integer id, Integer status) {
        return agentMapper.alterTheState(id, status);
    }

    @Override
    public Boolean destructionAgent(Integer id) {
        return agentMapper.destructionAgent(id);
    }

    @Override
    public Boolean destroyIntermediateTable(Integer userId) {
        return agentMapper.destroyIntermediateTable(userId);
    }
}
