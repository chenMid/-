package wakoo.fun.service.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.Vo.AdvertDtoVo;
import wakoo.fun.dto.AdvertDto;
import wakoo.fun.mapper.AdvertMapper;
import wakoo.fun.pojo.Advert;
import wakoo.fun.pojo.Carousel;
import wakoo.fun.service.AdvertService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdvertServiceImpl implements AdvertService {
    @Resource
    private AdvertMapper advertMapper;

    @Override
    public Boolean addAdver(Advert avatar) {
        return advertMapper.addAdver(avatar);
    }

    @Override
    public List<AdvertDto> getAvate(String keyword) {
        return advertMapper.getAvate(keyword);
    }

    @Override
    public AdvertDtoVo getIsAvate(Integer id) {
        return advertMapper.getIsAvate(id);
    }

    @Override
    public Boolean updAvate(AdvertDtoVo advertDtoVo) {
        return advertMapper.updAvate(advertDtoVo);
    }

    @Override
    public Boolean updAvateStatus(Integer id, Integer status) {
        return advertMapper.updAvateStatus(id, status);
    }

    @Override
    public Boolean addCarousel(Carousel carousel) {
        return advertMapper.addCarousel(carousel);
    }

    @Override
    public Integer getOrderNumber(Integer orderNumber) {
        return advertMapper.getOrderNumber(orderNumber);
    }
}
