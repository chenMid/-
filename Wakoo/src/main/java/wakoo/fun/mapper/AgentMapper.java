package wakoo.fun.mapper;

import org.apache.ibatis.annotations.Param;
import sun.management.resources.agent;
import wakoo.fun.dto.AgentDto;
import wakoo.fun.pojo.Advert;
import wakoo.fun.pojo.Agent;

import java.util.List;

public interface AgentMapper {
    /**
     * 查询所有代理
     * @return
     */
    List<AgentDto> listAdcert(@Param("keyword") String keyword, @Param("id") Integer id, @Param("status") Integer status);

    /**
     * 添加代理信息
     * @param agent
     * @return
     */
    Boolean addAgent(@Param("agent") Agent agent);

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
    AgentDto retrieveProxyInfo(@Param("getProxyId") Integer getProxyId);

    /**
     * 修改代理信息
     * @return
     */
    Boolean updRegent(@Param("agent")AgentDto agent);

    /**
     * 添加角色
     * @param userId 用户id
     * @param agentId 代理id
     * @param roleId 角色id
     * @return 是否添加成功
     */
    Boolean addARoleUser(@Param("userId") Integer userId, @Param("agentId") Integer agentId, @Param("roleId") Integer roleId);

    /**
     *  修改代理信息
     * @param agentId 代理id
     * @param roleId 角色id
     * @return 是否修改成功
     */
    Boolean modifyRoleAgent(@Param("agentId") Integer agentId, @Param("roleId") Integer roleId);

    /**
     * 修改 代理状态
     * @param id 代理id
     * @return 是否修改成功
     */
    Boolean alterTheState(@Param("ids") Integer[] ids, @Param("status") Integer status);

    /**
     * 删除代理
     * @param id 代理id
     * @return 是否删除成功
     */
    Boolean destructionAgent(@Param("ids") Integer[] ids);

    /**
     * 销毁中间表
     * @param ids 用户id
     * @return 是否刷新成功
     */
    Boolean destroyIntermediateTable(Integer[] ids);

    /**
     *  多条件查询，变成多条件
     * @param status 状态
     * @param name 代理名称
     * @param contactPhone 联系手机
     * @param address 地
     * @param createTime 创建时间
     * @param roleId 角色id
     * @return 符合条件的代理
     */
    List<AgentDto> agentMultiConditionQuery(@Param("status") Integer status,
                                            @Param("name") String name,
                                            @Param("contactPhone") String contactPhone,
                                            @Param("address") String address,
                                            @Param("createTime") String createTime,
                                            @Param("roleId") String roleId,
                                            @Param("userId") Integer userId);

    /**
     *   多条件查询，变成多条件
     * @param ids 代理id集合
     * @return 符
     */
    Integer getListAgent(List<Integer> ids);
}
