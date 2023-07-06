package wakoo.fun.service.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.mapper.FaAdminMapper;
import wakoo.fun.service.PersonalCenterService;

import javax.annotation.Resource;

@Service
public class PersonalCenterServiceImpl implements PersonalCenterService {

    @Resource
    private FaAdminMapper faAdminMapper;

    @Override
    public Boolean avatar(Integer id, String avatarPath) {
        return faAdminMapper.avatar(id, avatarPath);
    }
}
