package wakoo.fun.controller.menuController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.vo.MsgVo;
import wakoo.fun.pojo.SysMenu;
import wakoo.fun.pojo.SysRole;
import wakoo.fun.service.MenuService;
import wakoo.fun.utils.MenuTree;
import wakoo.fun.utils.MsgUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "Menu")
public class MenuController {
    @Resource
    private MenuService menuService;

    @ApiOperation(value = "menu")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken

    @PostMapping("/menu")
    public MsgVo menu(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        List<SysMenu> menu = menuService.getMenu((Integer) userId);
        List<SysMenu> menuList = new MenuTree(menu).buildTree();
        Map<String, Object> map = new HashMap<>();
        map.put("menus", menuList);
        return new MsgVo(200, "菜单列表", map);
    }

    @UserLoginToken
    @PostMapping("/Role")
    public MsgVo Role(HttpServletRequest request) {
        List<SysRole> role = menuService.getRole();
        if (role != null) {
            return new MsgVo(200, "查询成功", role);
        } else {
            return new MsgVo(403, "查询失败", null);
        }
    }


}
