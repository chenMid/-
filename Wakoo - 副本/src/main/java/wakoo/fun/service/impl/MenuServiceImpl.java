package wakoo.fun.service.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.mapper.FaAdminMapper;
import wakoo.fun.pojo.SysMenu;
import wakoo.fun.pojo.SysRole;
import wakoo.fun.service.MenuService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private FaAdminMapper faAdminMapper;

    @Override
    public List<SysMenu> getMenu(Integer id) {
        return faAdminMapper.getMenu(id);
    }

    @Override
    public List<SysRole> getRole() {
        return faAdminMapper.getRole();
    }

    @Override
    public List<SysMenu> ListTwoMune(Integer id) {
        return faAdminMapper.ListTwoMune(id);
    }
}
