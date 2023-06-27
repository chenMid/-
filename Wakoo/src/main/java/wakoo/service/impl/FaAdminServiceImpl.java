package wakoo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wakoo.mapper.FaAdminMapper;
import wakoo.pojo.FaAdmin;
import wakoo.service.FaAdminService;

import javax.annotation.Resource;
import java.util.List;
@Service
public class FaAdminServiceImpl implements FaAdminService {

    @Resource
    private FaAdminMapper faAdminMapper;

    @Override
    public List<FaAdmin> faAdmin(String account) {
        return faAdminMapper.faAdmin(account);
    }

    @Override
    public Boolean UpdToken(String Token, Integer id) {
        return faAdminMapper.UpdToken(Token, id);
    }
}
