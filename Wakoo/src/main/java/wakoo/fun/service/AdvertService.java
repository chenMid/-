package wakoo.fun.service;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.Vo.AdvertDtoVo;
import wakoo.fun.dto.AdvertDto;
import wakoo.fun.pojo.Advert;
import wakoo.fun.pojo.Carousel;

import java.util.List;

public interface AdvertService {
    /**
     * 添加广告
     * @param avatar
     * @return
     */
    Boolean addAdver(@Param("avatar") Advert avatar);
    /**
     * 查询所有广告
     * @return
     */
    List<AdvertDto> getAvate(String keyword);

    /**
     *查询指定广告信息
     * @return
     */
    AdvertDtoVo getIsAvate(@Param("id") Integer id);
    /**
     * 修改广告信息
     * @return
     */
    Boolean updAvate(@Param("Advert") AdvertDtoVo advertDtoVo);
    /**
     * 修改广告状态
     * @param id
     * @param status
     * @return
     */
    Boolean updAvateStatus(@Param("id") Integer id, @Param("status") Integer status);
    /**
     * 添加轮播图
     * @return
     */
    Boolean addCarousel(@Param("carousel") Carousel carousel);

    /**
     * 查询顺序不能有重复的
     * @return
     */
    Integer getOrderNumber(Integer orderNumber);
}
