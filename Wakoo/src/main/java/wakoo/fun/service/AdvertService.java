package wakoo.fun.service;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.vo.AdvertDtoVo;
import wakoo.fun.vo.CarouselVo;
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
     * 删除广告
     * @param id 要删除的id
     * @return 删除成功返回true！false
     */
    Boolean removeSpecifiedAds(@Param("id") Integer id);
    /**
     * 添加轮播图
     * @return
     */
    Boolean addCarousel(@Param("carousel") Carousel carousel);

    /**
     * 查询最大序列号
     * @return
     */
    Integer getOrderNumber();
    /**
     * 查询所有广告
     * @return
     */
    List<CarouselVo> getAllConouselVo(@Param("keyword") String keyword);
    /**
     * 查询指定轮播图
     * @return
     */
    CarouselVo isCaouselVo(@Param("id") Integer id);
    /**
     * 修改指定用户信息
     * @param carousel
     * @return
     */
    Boolean updCaouselVo(@Param("carousel") Carousel carousel);
    /**
     * 修改轮播图的状态
     * @param id
     * @param status
     * @return
     */
    Boolean deleteASpecifiedWheelMap(@Param("id") Integer id);
    /**
     * 通过id查询用户原有顺序
     * @return
     */
    Carousel getCarById(Integer id);

    /**
     * 通过顺序查到用户及顺序
     * @return
     */
    Carousel getNumberById(Integer orderNumber);
    /**
     * 修改另一个用户的顺序
     * @return
     */
    Boolean updCarnumber(Integer id,Integer orderNumber);
    /**
     * 查询所有的顺序
     * @return
     */
    List<Integer> listIntegerGetCa();

}
