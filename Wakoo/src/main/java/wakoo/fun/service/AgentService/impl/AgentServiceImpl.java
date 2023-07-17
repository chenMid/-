package wakoo.fun.service.AgentService.impl;

import org.springframework.stereotype.Service;
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
    public List<Agent> listAdcert(String keyword) {
        return agentMapper.listAdcert(keyword);
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
    public Agent retrieveProxyInfo(Integer getProxyId) {
        return agentMapper.retrieveProxyInfo(getProxyId);
    }

    @Override
    public Boolean updRegent(Agent agent) {
        return agentMapper.updRegent(agent);
    }
}
