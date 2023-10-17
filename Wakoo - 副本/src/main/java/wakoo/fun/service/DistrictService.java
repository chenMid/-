package wakoo.fun.service;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.utils.District;

import java.util.List;

public interface DistrictService {

    /**
     * 查询所有省份
     * @return
     */
    List<District> queryProvince();

    /**
     * 查询城市或地区
     * @param code
     * @return
     */
    List<District> queryCity(String code);

    /**
     * 通过code查询地区信息
     * @param code
     * @return
     */
    District queryByCode(String code);

    /**
     * 查询所有地址信息
     * @return
     */
    List<District> queryDistrictList();

    /**
     * 根据编码集合查询
     * @param codeList
     * @return
     */
    List<District> queryByCodeList(@Param("codeList") List<String> codeList);

    /**
     *查询省和市
     * @param codeList
     * @return
     */
    List<District> queryProvinceAndCity(@Param("codeList") List<String> codeList);

    /**
     *查询上级地区查下级
     * @param codeList
     * @return
     */
    List<District> queryByParentCodes(@Param("codeList") List<String> codeList);

    /**
     * 查询省份下第一个城市
     * @param code
     * @return
     */
    District queryFirstCity(@Param("code") String code);


    /**
     * 新增
     * @param district 新增
     */
    void insertSelective(District district);

    /**
     * 更新
     * @param district
     */
    void updateSelective(District district);

    /**
     * 删除
     * @param code 编码
     */
    void delByCode(String code);


    /**
     * 根据名称获取名称地区信息（精准匹配）
     * @param districtNames 地区名称
     * @return 结果
     */
    List<District> allDistrictByNames(@Param("districtNames") List<String> districtNames);
}
