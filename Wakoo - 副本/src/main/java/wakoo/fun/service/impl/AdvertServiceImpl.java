package wakoo.fun.service.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.vo.AdvertDtoVo;
import wakoo.fun.vo.CarouselVo;
import wakoo.fun.dto.AdvertDto;
import wakoo.fun.mapper.AdvertMapper;
import wakoo.fun.pojo.Advert;
import wakoo.fun.pojo.Carousel;
import wakoo.fun.service.AdvertService;
import wakoo.fun.vo.DeleteVo;

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
    public Boolean removeSpecifiedAds(Integer[] ids) {
        return advertMapper.removeSpecifiedAds(ids);
    }

    @Override
    public Boolean addCarousel(Carousel carousel) {
        return advertMapper.addCarousel(carousel);
    }

    @Override
    public Integer getOrderNumber() {
        return advertMapper.getOrderNumber();
    }

    @Override
    public List<CarouselVo> getAllConouselVo(String keyword) {
        return advertMapper.getAllConouselVo(keyword);
    }

    @Override
    public CarouselVo isCaouselVo(Integer id) {
        return advertMapper.isCaouselVo(id);
    }

    @Override
    public Boolean updCaouselVo(Carousel carousel) {
        return advertMapper.updCaouselVo(carousel);
    }

    @Override
    public Boolean deleteASpecifiedWheelMap(Integer[] ids) {
        return advertMapper.deleteASpecifiedWheelMap(ids);
    }

    @Override
    public Carousel getCarById(Integer id) {
        return advertMapper.getCarById(id);
    }

    @Override
    public Carousel getNumberById(Integer orderNumber) {
        return advertMapper.getNumberById(orderNumber);
    }

    @Override
    public Boolean updCarnumber(Integer id, Integer orderNumber) {
        return advertMapper.updCarnumber(id, orderNumber);
    }

    @Override
    public List<Integer> listIntegerGetCa() {
        return advertMapper.listIntegerGetCa();
    }

}
