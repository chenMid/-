package wakoo.fun.mapper;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.pojo.Advert;
import wakoo.fun.pojo.Agent;

import java.util.List;

public interface AgentMapper {
    /**
     * 查询所有代理
     * @return
     */
    List<Agent> listAdcert(@Param("keyword") String keyword);

    /**
     * 添加代理信息
     * @param agent
     * @return
     */
    Boolean addAgent(Agent agent);

    /**
     * 查询是否重复邮箱
     * @param email
     * @return
     */
    Integer isEmailDuplicated(@Param("email") String email);

    /**
     * 查询代理名称是否重复
     * @param name
     * @return
     */
    Integer isUsernameDuplicated(@Param("name") String name);

    /**
     * 查询是否重复手机号
     * @param phone
     * @return
     */
    Integer isContactPhone(@Param("phone") String phone);
    /**
     * 通过id查询指定代理数据
     * @param getProxyId
     * @return
     */
    Agent retrieveProxyInfo(@Param("getProxyId") Integer getProxyId);

    /**
     * 修改代理信息
     * @return
     */
    Boolean updRegent(@Param("agent") Agent agent);
}
