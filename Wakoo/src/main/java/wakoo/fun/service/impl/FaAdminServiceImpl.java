package wakoo.fun.service.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.mapper.FaAdminMapper;
import wakoo.fun.pojo.FaAdmin;
import wakoo.fun.pojo.FaAdminLogin;
import wakoo.fun.service.FaAdminService;
import wakoo.fun.vo.FadminVo;

import javax.annotation.Resource;
import java.util.List;
@Service
public class FaAdminServiceImpl implements FaAdminService {

    @Resource
    private FaAdminMapper faAdminMapper;

    @Override
    public List<FaAdmin> faAdmin(String userName) {
        return faAdminMapper.faAdmin(userName);
    }

    @Override
    public Boolean UpdToken(String Token, String userName) {
        return faAdminMapper.UpdToken(Token, userName);
    }

    @Override
    public FaAdmin getFaAdmin(Integer id) {
        return faAdminMapper.getFaAdmin(id);
    }

    @Override
    public FaAdminLogin UserNameFaAdmin(String userName) {
        return faAdminMapper.getUserNameFaAdmin(userName);
    }

    @Override
    public FaAdminLogin ListFadmin(String username) {
        return faAdminMapper.ListFadmin(username);
    }

    @Override
    public Boolean modifyPersonalInformation(FadminVo fadminVo) {
        return faAdminMapper.modifyPersonalInformation(fadminVo);
    }
}
