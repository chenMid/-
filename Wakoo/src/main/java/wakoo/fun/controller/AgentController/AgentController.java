package wakoo.fun.controller.AgentController;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.Vo.MsgVo;
import wakoo.fun.config.PassToken;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dto.AdvertDto;
import wakoo.fun.mapper.AgentMapper;
import wakoo.fun.pojo.Advert;
import wakoo.fun.pojo.Agent;
import wakoo.fun.service.AgentService.AgentService;
import wakoo.fun.service.DistrictService;
import wakoo.fun.utils.District;

import javax.annotation.Resource;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@EnableTransactionManagement//数据库事务管理
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
    public ResponseEntity<MsgVo> getAllAgent(String keyword, Integer pageSize, Integer pageNumber) {
        try {
            if (pageSize == null || pageSize <= 0) {
                pageSize = 10; // 默认每页显示10条数据
            }
            if (pageNumber == null || pageNumber <= 0) {
                pageNumber = 1; // 默认显示第一页
            }
            PageHelper.startPage(pageNumber, pageSize);
            List<Agent> adverts = agentService.listAdcert(keyword);
            PageInfo<Agent> pageInfo = new PageInfo<>(adverts);
            return ResponseEntity.ok(new MsgVo(200, "请求成功", pageInfo));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "请求处理失败", null));
        }
    }

    @ApiOperation(value = "添加代理")
    @UserLoginToken
    @PostMapping("/addAgent")
    @Transactional
    public ResponseEntity<MsgVo> addAgent(@Validated Agent agent, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            MsgVo response = new MsgVo(403, errorMessage, false);
            return ResponseEntity.ok(response);
        }
        try {
            // 检查用户名是否重复
            Boolean isUsernameDuplicated = agentService.isUsernameDuplicated(agent.getName());
        if (isUsernameDuplicated) {
                return ResponseEntity.ok(new MsgVo(403, "用户名已经存在", false));
            }
            //检查手机号是否重复
            Boolean contactPhone = agentService.isContactPhone(agent.getContactPhone());
        if (contactPhone){
            return ResponseEntity.ok(new MsgVo(403, "手机号已存在", false));
        }
            // 检查邮箱是否重复
            Boolean isEmailDuplicated = agentService.isEmailDuplicated(agent.getEmail());
        if (isEmailDuplicated) {
                return ResponseEntity.ok(new MsgVo(403, "邮箱已经存在", false));
            }
            // 执行添加代理商操作
            if (agent.getDetailedAddress()!=null){
                String s = agent.getAddress() + "/" + agent.getDetailedAddress();
                agent.setAddress(s);
            }
            Boolean aBoolean = agentService.addAgent(agent);
            System.out.println(aBoolean);

            return ResponseEntity.ok(new MsgVo(200, "添加成功", true));
        } catch (Exception e) {
            return ResponseEntity.ok(new MsgVo(500, "添加代理失败", false));
        }
    }

    @ApiOperation(value = "获取指定代理数据")
    @UserLoginToken
    @Transactional
    @GetMapping("/fetchProxyData")
    public ResponseEntity<MsgVo> fetchProxyData(Integer getProxyId) {
        Agent agent = agentService.retrieveProxyInfo(getProxyId);
        String address = agent.getAddress();
        String[] parts = address.split("/");
        int separatorCount = parts.length - 1;
        if (separatorCount >= 3) {
            String location = String.join("/", Arrays.copyOfRange(parts, 0, 3));
            String building = String.join("/", Arrays.copyOfRange(parts, 3, parts.length));
            agent.setAddress(location);
            agent.setDetailedAddress(building);
        }
        if (agent == null) {
            // 当代理信息不存在时返回错误提示给前端
            MsgVo errorResponse = new MsgVo(403, "代理信息不存在", null);
            return ResponseEntity.badRequest().body(errorResponse);
        } else {
            // 当代理信息存在时返回成功提示和代理信息给前端
            MsgVo successResponse = new MsgVo(200, "请求成功", agent);
            return ResponseEntity.ok(successResponse);
        }
    }

    @PassToken
    @Operation(summary = "查询所有省份")
    @GetMapping("/province")
    public ResponseEntity<MsgVo> queryProvince() {
        List<District> districts = districtService.queryProvince();
        return ResponseEntity.ok(new MsgVo(200,"请求成功",districts));
    }
    @PassToken
    @Operation(summary = "查询城市或地区")
    @GetMapping("/city")
    public ResponseEntity<MsgVo> queryCity(@Parameter(description = "地区编码")@RequestParam("code") String code) {
        List<District> districts = districtService.queryCity(code);
        return ResponseEntity.ok(new MsgVo(200,"请求成功",districts));
    }

    @ApiOperation(value = "代理修改")
    @UserLoginToken
    @Transactional
    @PutMapping("/updAgent")
    public ResponseEntity<?> updAgent(@Validated Agent agent, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            MsgVo response = new MsgVo(403, errorMessage, false);
            return ResponseEntity.ok(response);
        }

        Agent agent1 = agentService.retrieveProxyInfo(agent.getId());

        if (!agent1.getName().equals(agent.getName())) {
            Boolean isUsernameDuplicated = agentService.isUsernameDuplicated(agent.getName());
            if (isUsernameDuplicated) {
                return ResponseEntity.ok(new MsgVo(403, "用户名已经存在", false));
            }
        }

        if (!agent.getContactPhone().equals(agent1.getContactPhone())) {
            Boolean isContactPhoneDuplicated = agentService.isContactPhone(agent.getContactPhone());
            if (isContactPhoneDuplicated) {
                return ResponseEntity.ok(new MsgVo(403, "手机号已存在", false));
            }
        }

        if (!agent.getEmail().equals(agent1.getEmail())) {
            Boolean isEmailDuplicated = agentService.isEmailDuplicated(agent.getEmail());
            if (isEmailDuplicated) {
                return ResponseEntity.ok(new MsgVo(403, "邮箱已经存在", false));
            }
        }
        if (agent.getDetailedAddress() == null || agent.getDetailedAddress().equals("")) {
            int lastSeparatorIndex = agent.getAddress().lastIndexOf("/");
            if (lastSeparatorIndex != -1) {
                String modifiedAddress = agent.getAddress().substring(0, lastSeparatorIndex);
                System.out.println(modifiedAddress); // 输出：河北省/廊坊市/三河市
            }
        } else {
            String s = agent.getAddress() + "/" + agent.getDetailedAddress();
            agent.setAddress(s);
        }
        System.out.println(agent.getDetailedAddress());
        try {
            Boolean aBoolean = agentService.updRegent(agent);
            if (aBoolean) {
                return ResponseEntity.ok(new MsgVo(200, "请求成功", true));
            } else {
                return ResponseEntity.ok(new MsgVo(500, "更新代理失败", false));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new MsgVo(500, "内部服务器错误", false));
        }
    }
}
