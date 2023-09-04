package wakoo.fun.controller.agentController;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.common.Log;
import wakoo.fun.dto.AgentDto;
import wakoo.fun.log.Constants;
import wakoo.fun.vo.AddressOption;
import wakoo.fun.vo.MsgVo;
import wakoo.fun.config.PassToken;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.pojo.Agent;
import wakoo.fun.service.AgentService.AgentService;
import wakoo.fun.service.DistrictService;
import wakoo.fun.utils.District;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@EnableTransactionManagement
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "权限页面")
public class AgentController {
    @Resource
    private AgentService agentService;
    @Resource
    private DistrictService districtService;

    @ApiOperation(value = "查询所有代理及模糊查询分页")
    @UserLoginToken
    @GetMapping("/getAllAgent")
    public MsgVo getAllAgent(HttpServletRequest request, String keyword, Integer pageSize, Integer pageNumber,Integer status) {
        try {
            pageNumber = Math.max(pageNumber, 1);

            PageHelper.startPage(pageNumber, pageSize);
            Object userId = request.getAttribute("userId");
            List<AgentDto> adverts = agentService.listAdcert(keyword, (Integer) userId,status);

            PageInfo<AgentDto> pageInfo = new PageInfo<>(adverts);
            pageInfo.setPageSize(pageSize);
            return new MsgVo(200, "请求成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgVo(500, "请求处理失败", null);
        }
    }

    @ApiOperation(value = "代理多条件查询")
    @UserLoginToken
    @GetMapping("/agentMultiConditionQuery")
    public MsgVo agentMultiConditionQuery(HttpServletRequest request,
                                          Integer pageSize,
                                          Integer pageNumber,
                                          Integer status,
                                          String name,
                                          String contactPhone,
                                          String address,
                                          String createTime,
                                          String roleId){
        Object userId = request.getAttribute("userId");
        pageNumber = Math.max(pageNumber, 1);
        PageHelper.startPage(pageNumber, pageSize);
        try{
            List<AgentDto> agentDtos = agentService.agentMultiConditionQuery(status, name, contactPhone, address, createTime, roleId, (Integer) userId);
            PageInfo<AgentDto> pageInfo = new PageInfo<>(agentDtos);
            pageInfo.setPageSize(pageSize);
            return new MsgVo(200, "请求成功", pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            return new MsgVo(500, "请求失败", false);
        }
    }





    @ApiOperation(value = "添加代理")
    @UserLoginToken
    @Transactional(rollbackFor = Exception.class)
    @Log(modul = "代理页面-添加代理", type = Constants.INSERT, desc = "操作添加按钮")
    @PostMapping("/addAgent")
    public MsgVo addAgent(@Validated @RequestBody Agent agent, BindingResult result, HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            return new MsgVo(403, errorMessage, false);
        }
            //检查手机号是否重复
            Boolean contactPhone = agentService.isContactPhone(agent.getContactPhone());
            if (contactPhone) {
                return new MsgVo(403, "手机号已存在", false);
            }
            // 执行添加代理商操作
            num(agent);
            Boolean aBoolean = agentService.addAgent(agent);
            if (aBoolean){
                agentService.addARoleUser((Integer) userId, agent.getId(), Integer.parseInt(agent.getRoleId()));
            return new MsgVo(200, "添加成功", true);
            }
            return new MsgVo(403, "添加代理失败", false);
    }

    @ApiOperation(value = "获取指定代理数据")
    @UserLoginToken
    @Transactional
    @GetMapping("/fetchProxyData")
    public MsgVo fetchProxyData(Integer getProxyId) {
        AgentDto agent = agentService.retrieveProxyInfo(getProxyId);
        String address = agent.getAddress();
        String[] parts = address.split("/");
        int separatorCount = parts.length - 1;
        if (separatorCount >= 3) {
            String location = String.join("/", Arrays.copyOfRange(parts, 0, 3));
            String building = String.join("/", Arrays.copyOfRange(parts, 3, parts.length));
            agent.setAddress(location);
            agent.setDetailedAddress(building);
        }
        // 当代理信息存在时返回成功提示和代理信息给前端
        return new MsgVo(200, "请求成功", agent);
    }
    /**
     * 从数据库中查询地址数据，并转换为地址选项列表的格式
     * @return 地址选项列表
     */
    public AddressOption generateAddressOptionsFromDB() {
        // 创建根节点
        AddressOption root = new AddressOption("", "请选择");
        // 查询顶层地址
        List<District> districts = districtService.queryProvince();
        for (District address : districts) {
            // 构建地址选项
            AddressOption option = buildAddressOption(address);
            // 将地址选项添加到根节点
            root.addChild(option);
            // 递归添加子地址
            addChildAddresses(option, address);
        }
        // 返回地址选项列表
        return root;
    }

    /**
     * 构建地址选项对象
     * @param address 地址对象
     * @return 地址选项对象
     */
    private AddressOption buildAddressOption(District address) {
        return new AddressOption(address.getCode(), address.getName());
    }

    /**
     * 递归添加子地址
     * @param parentOption 地址选项
     * @param parentAddress 地址对象
     */
    private void addChildAddresses(AddressOption parentOption, District parentAddress) {
        // 查询子地址
        List<District> childAddresses = districtService.queryCity(parentAddress.getCode());
        for (District address : childAddresses) {
            // 构建地址选项
            AddressOption option = buildAddressOption(address);
            // 将地址选项添加到父节点
            parentOption.addChild(option);
            // 递归添加子地址
            addChildAddresses(option, address);
        }
    }


    @PassToken
    @Operation(summary = "地址 选择")
    @GetMapping("/cascadeSelector")
    public MsgVo cascadeSelector(String code) {
        AddressOption addressOption = generateAddressOptionsFromDB();
        return new MsgVo(200, "请求成功",addressOption);
    }
    @PassToken
    @Operation(summary = "查询所有省份")
    @GetMapping("/province")
    public MsgVo province() {
        List<District> districts = districtService.queryProvince();
        return new MsgVo(200, "请求成功", districts);
    }
    @PassToken
    @Operation(summary = "查询城市或地区")
    @GetMapping("/city")
    public MsgVo city(@Parameter(description = "地区编码") @RequestParam("code") String code) {
        List<District> districts1 = districtService.queryDistrictList();
        System.out.println(districts1);
        List<District> districts = districtService.queryCity(code);
        return new MsgVo(200, "请求成功", districts);
    }

    @PassToken
    @Operation(summary = "查询城市或地区")
    @GetMapping("/queryCity")
    public MsgVo queryCity(@Parameter(description = "地区编码") @RequestParam("code") String code) {
        List<District> districts = districtService.queryCity(code);
        return new MsgVo(200, "请求成功", districts);
    }

    @ApiOperation(value = "代理修改")
    @UserLoginToken
    @Transactional(rollbackFor = Exception.class)
    @Log(modul = "代理页面-修改代理", type = Constants.UPDATE, desc = "操作修改按钮")
    @PutMapping("/updAgent")
    public MsgVo updAgent(@Validated @RequestBody AgentDto agent, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            return new MsgVo(403, errorMessage, false);
        }

        AgentDto agent1 = agentService.retrieveProxyInfo(agent.getId());
        if (!agent.getContactPhone().equals(agent1.getContactPhone())) {
            Boolean isContactPhoneDuplicated = agentService.isContactPhone(agent.getContactPhone());
            if (isContactPhoneDuplicated) {
                return new MsgVo(403, "手机号已存在", false);
            }
        }
        // 执行添加代理商操作
        if (agent.getDetailedAddress() == null || "".equals(agent.getDetailedAddress())) {
            int lastSeparatorIndex = agent.getAddress().lastIndexOf("/");
            if (lastSeparatorIndex != -1) {
                agent.getAddress().substring(0, lastSeparatorIndex);
            }
        } else {
            String s = agent.getAddress() + "/" + agent.getDetailedAddress();
            agent.setAddress(s);
        }
            Boolean aBoolean = agentService.updRegent(agent);
            if (aBoolean){
                agentService.modifyRoleAgent(agent.getId(),Integer.parseInt(agent.getRoleId()));
                return new MsgVo(200, "修改成功", true);
            } else {
                return new MsgVo(403, "更新代理失败", false);
            }
    }

    private void num(@RequestBody @Validated Agent agent) {
        StringBuilder num = new StringBuilder();
        List<District> districts = districtService.queryByCodeList(agent.getAddress());
        for (District district : districts) {
            num.append(district.getName()).append("/");
        }
        num.append(agent.getDetailedAddress());
        agent.setDetailedAddress(String.valueOf(num));
    }


    @ApiOperation(value = "删除代理软删除")
    @UserLoginToken
    @Log(modul = "代理页面-删除", type = Constants.UPDATE, desc = "操作删除按钮")
    @SuppressWarnings("unchecked")
    @DeleteMapping("/deleteProxySoftDelete")
    public MsgVo deleteProxySoftDelete(@RequestBody Map<String, Object> requestBody) {
        List<Integer> idsList = (List<Integer>) requestBody.get("ids");
        Integer status = (Integer) requestBody.get("status");

        Integer[] ids = idsList.toArray(new Integer[0]);

        Boolean success = false;
        success = agentService.alterTheState(ids, status);
        if (success) {
            return new MsgVo(200, "操作成功", true);
        } else {
            return new MsgVo(403, "操作失败", false);
        }
    }

    @ApiOperation(value = "销毁代理")
    @UserLoginToken
    @Transactional(rollbackFor = Exception.class)
    @Log(modul = "代理页面-销毁代理", type = Constants.DELETE, desc = "操作销毁按钮")
    @DeleteMapping ("destructionAgent")
    public MsgVo destructionAgent(@RequestBody Map<String, Integer[]> requestBody){
        Integer[] ids = requestBody.get("ids");
        Boolean aBoolean=false;
        if (ids!=null){
            aBoolean = agentService.destructionAgent(ids);
        }
        if (aBoolean) {
            Boolean aBoolean1 = agentService.destroyIntermediateTable(ids);
            if (aBoolean1){
                return new MsgVo(200, "销毁成功", true);
            }
        } else {
            return new MsgVo(403, "销毁失败", false);
        }
        return null;
    }
}
