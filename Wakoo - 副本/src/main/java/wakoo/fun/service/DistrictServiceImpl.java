package wakoo.fun.service;

import org.springframework.stereotype.Service;
import wakoo.fun.mapper.DistrictMapper;
import wakoo.fun.utils.District;

import javax.annotation.Resource;
import java.util.List;
@Service
public class DistrictServiceImpl implements DistrictService {
    @Resource
    private DistrictMapper districtMapper;

    @Override
    public List<District> queryProvince() {
        return districtMapper.queryProvince();
    }

    @Override
    public List<District> queryCity(String code) {
        return districtMapper.queryCity(code);
    }

    @Override
    public District queryByCode(String code) {
        return districtMapper.queryByCode(code);
    }

    @Override
    public List<District> queryDistrictList() {
        return districtMapper.queryDistrictList();
    }

    @Override
    public List<District> queryByCodeList(List<String> codeList) {
        return districtMapper.queryByCodeList(codeList);
    }

    @Override
    public List<District> queryProvinceAndCity(List<String> codeList) {
        return districtMapper.queryProvinceAndCity(codeList);
    }

    @Override
    public List<District> queryByParentCodes(List<String> codeList) {
        return districtMapper.queryByParentCodes(codeList);
    }

    @Override
    public District queryFirstCity(String code) {
        return districtMapper.queryFirstCity(code);
    }

    @Override
    public void insertSelective(District district) {

    }

    @Override
    public void updateSelective(District district) {

    }

    @Override
    public void delByCode(String code) {

    }

    @Override
    public List<District> allDistrictByNames(List<String> districtNames) {
        return districtMapper.allDistrictByNames(districtNames);
    }

}
