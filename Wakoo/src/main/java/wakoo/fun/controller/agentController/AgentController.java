package wakoo.fun.controller.agentController;

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
import wakoo.fun.dto.AgentDto;
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
    public ResponseEntity<MsgVo> getAllAgent(HttpServletRequest request, String keyword, Integer pageSize, Integer pageNumber,Integer status) {
        try {
            PageHelper.startPage(pageNumber, pageSize);
            Object userId = request.getAttribute("userId");
            System.out.println(userId);
            List<AgentDto> adverts = agentService.listAdcert(keyword, (Integer) userId,status);

            PageInfo<AgentDto> pageInfo = new PageInfo<>(adverts);
            pageInfo.setPageSize(pageSize);
            return ResponseEntity.ok(new MsgVo(200, "请求成功", pageInfo));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "请求处理失败", null));
        }
    }

    @ApiOperation(value = "添加代理")
    @UserLoginToken
    @Transactional
    @PostMapping("/addAgent")
    public ResponseEntity<MsgVo> addAgent(@Validated @RequestBody Agent agent, BindingResult result, HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MsgVo(403, errorMessage, false));
        }
        try {
            //检查手机号是否重复
            Boolean contactPhone = agentService.isContactPhone(agent.getContactPhone());
            if (contactPhone) {
                return ResponseEntity.ok(new MsgVo(403, "手机号已存在", false));
            }
            // 执行添加代理商操作
            num(agent);
            Boolean aBoolean = agentService.addAgent(agent);
            if (aBoolean){
                agentService.addARoleUser((Integer) userId, agent.getId(), Integer.parseInt(agent.getRoleId()));
            return ResponseEntity.ok(new MsgVo(200, "添加成功", true));
            }
    } catch (Exception e) {
            return ResponseEntity.ok(new MsgVo(500, "添加代理失败", false));
        }
        return ResponseEntity.ok(new MsgVo(403, "添加代理失败", false));
    }

    @ApiOperation(value = "获取指定代理数据")
    @UserLoginToken
    @Transactional
    @GetMapping("/fetchProxyData")
    public ResponseEntity<MsgVo> fetchProxyData(Integer getProxyId) {
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
        MsgVo successResponse = new MsgVo(200, "请求成功", agent);
        return ResponseEntity.ok(successResponse);
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
    public ResponseEntity<MsgVo> cascadeSelector(String code) {
        AddressOption addressOption = generateAddressOptionsFromDB();
        return ResponseEntity.ok(new MsgVo(200, "请求成功",addressOption));
    }
    @PassToken
    @Operation(summary = "查询所有省份")
    @GetMapping("/province")
    public ResponseEntity<MsgVo> province() {
        List<District> districts = districtService.queryProvince();
        return ResponseEntity.ok(new MsgVo(200, "请求成功", districts));
    }
    @PassToken
    @Operation(summary = "查询城市或地区")
    @GetMapping("/city")
    public ResponseEntity<MsgVo> city(@Parameter(description = "地区编码") @RequestParam("code") String code) {
        List<District> districts1 = districtService.queryDistrictList();
        System.out.println(districts1);
        List<District> districts = districtService.queryCity(code);
        return ResponseEntity.ok(new MsgVo(200, "请求成功", districts));
    }

    @PassToken
    @Operation(summary = "查询城市或地区")
    @GetMapping("/queryCity")
    public ResponseEntity<MsgVo> queryCity(@Parameter(description = "地区编码") @RequestParam("code") String code) {
        List<District> districts = districtService.queryCity(code);
        return ResponseEntity.ok(new MsgVo(200, "请求成功", districts));
    }

    @ApiOperation(value = "代理修改")
    @UserLoginToken
    @Transactional
    @PutMapping("/updAgent")
    public ResponseEntity<MsgVo> updAgent(@Validated @RequestBody AgentDto agent, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            MsgVo response = new MsgVo(403, errorMessage, false);
            return ResponseEntity.ok(response);
        }

        AgentDto agent1 = agentService.retrieveProxyInfo(agent.getId());
        if (!agent.getContactPhone().equals(agent1.getContactPhone())) {
            Boolean isContactPhoneDuplicated = agentService.isContactPhone(agent.getContactPhone());
            if (isContactPhoneDuplicated) {
                return ResponseEntity.ok(new MsgVo(403, "手机号已存在", false));
            }
        }
        // 执行添加代理商操作
        if (agent.getDetailedAddress() == null || "".equals(agent.getDetailedAddress())) {
            int lastSeparatorIndex = agent.getAddress().lastIndexOf("/");
            if (lastSeparatorIndex != -1) {
                String modifiedAddress = agent.getAddress().substring(0, lastSeparatorIndex);
            }
        } else {
            String s = agent.getAddress() + "/" + agent.getDetailedAddress();
            agent.setAddress(s);
        }
        try {
            Boolean aBoolean = agentService.updRegent(agent);
            if (aBoolean){
                agentService.modifyRoleAgent(agent.getId(),Integer.parseInt(agent.getRoleId()));
                return ResponseEntity.ok(new MsgVo(200, "修改成功", true));
            } else {
                return ResponseEntity.ok(new MsgVo(500, "更新代理失败", false));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new MsgVo(500, "内部服务器错误", false));
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
    @PutMapping("/deleteProxySoftDelete")
    public ResponseEntity<MsgVo> deleteProxySoftDelete(Integer id,Integer status){
        Boolean aBoolean=false;
        if (id!=null){
            aBoolean = agentService.alterTheState(id,status);
        }
        if (aBoolean) {
            return ResponseEntity.ok(new MsgVo(200, "操作成功", true));
        } else {
            return ResponseEntity.ok(new MsgVo(403, "操作失败", false));
        }
    }

    @ApiOperation(value = "销毁代理")
    @UserLoginToken
    @Transactional
    @DeleteMapping ("destructionAgent")
    public ResponseEntity<MsgVo> destructionAgent(Integer id){
        Boolean aBoolean=false;
        try {
        if (id!=null){
            aBoolean = agentService.destructionAgent(id);
        }
        if (aBoolean) {
            Boolean aBoolean1 = agentService.destroyIntermediateTable(id);
            if (aBoolean1){
                return ResponseEntity.ok(new MsgVo(200, "销毁成功", true));
            }
        } else {
            return ResponseEntity.ok(new MsgVo(200, "销毁失败", false));
        }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.ok(new MsgVo(500, "内部服务器错误", false));
        }
        return null;
    }
}
