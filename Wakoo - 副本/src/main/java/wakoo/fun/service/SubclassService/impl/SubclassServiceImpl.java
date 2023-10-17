package wakoo.fun.service.SubclassService.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.dto.SubclassDto;
import wakoo.fun.mapper.SubclassMapper;
import wakoo.fun.pojo.Subclass;
import wakoo.fun.service.SubclassService.SubclassService;

import javax.annotation.Resource;
import java.util.List;
@Service
public class SubclassServiceImpl implements SubclassService {
    @Resource
    private SubclassMapper subclassMapper;

    @Override
    public List<Subclass> getAllSubclass(String keyword) {
        return subclassMapper.getAllSubclass(keyword);
    }

    @Override
    public Boolean insertSubclasss(Subclass subclass) {
        return subclassMapper.insertSubclasss(subclass);
    }

    @Override
    public Boolean getBySort(Integer sort,String typeName) {
        return subclassMapper.updBySort(sort, typeName);
    }

    @Override
    public Boolean Maxonejia(Integer sort) {
        return subclassMapper.Maxonejia(sort);
    }

    @Override
    public Boolean addSubclasss(Subclass subclass) {
        return subclassMapper.addSubclasss(subclass);
    }

    @Override
    public List<Integer> getAgeList(Integer id,Integer age,String name) {
        return subclassMapper.getAgeList(id,age,name);
    }

    @Override
    public List<Integer> getTypeIdByName(Integer id, String name) {
        return subclassMapper.getTypeIdByName(id, name);
    }

    @Override
    public Subclass getSubclass(Integer id) {
        return subclassMapper.getSubclass(id);
    }

    @Override
    public List<SubclassDto> getfType() {
        return subclassMapper.getfType();
    }

    @Override
    public Integer getByid(Integer id, String name) {
        return subclassMapper.getByid(id, name);
    }

    @Override
    public Boolean updByIdSort(Integer sort, Integer id) {
        return subclassMapper.updByIdSort(sort, id)>0;
    }

    @Override
    public Boolean updTypeClass(Subclass subclass) {
        return subclassMapper.updTypeClass(subclass);
    }

    @Override
    public Integer getMaxSort(Integer typeId) {
        return subclassMapper.getMaxSort(typeId);
    }

    @Override
    public Integer getStypeageByid(Integer zid) {
        return subclassMapper.getStypeageByid(zid);
    }

    @Override
    public Boolean deleteSubclass(Integer[] ids) {
        return subclassMapper.deleteSubclass(ids);
    }

    @Override
    public List<Subclass> multipleConditionalQuerySubclass(String typeName, String name, String material, String createTime, String updateTime, Integer sort) {
        return subclassMapper.multipleConditionalQuerySubclass(typeName, name, material, createTime, updateTime, sort);
    }


}
