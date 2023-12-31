package wakoo.fun.mapper;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.dto.SubclassDto;
import wakoo.fun.pojo.Subclass;

import java.util.List;

public interface SubclassMapper {
    /**
     * 查询所有
     * @return
     */
    List<Subclass> getAllSubclass(@Param("keyword") String keyword);
    /**
     * 添加子类
     * @return
     */
    Boolean insertSubclasss(@Param("subclass") Subclass subclass);

    /**
     * 查询是否查到有这个顺序
     * @return
     */
    Boolean updBySort(@Param("sort") Integer sort, @Param("typeName") String typeName);

    /**
     * 将原有顺序值大于等于2的记录的顺序值加1
     * @return
     */
    Boolean Maxonejia(Integer sort);
    /**
     * 添加子类
     * @return
     */
    Boolean addSubclasss(@Param("subclass") Subclass subclass);

    /**
     * 查询是否有重复的年龄
     * @return
     */
    List<Integer> getAgeList(@Param("id") Integer id, @Param("age") Integer age, @Param("name") String name);
    List<Integer> getTypeIdByName(@Param("id") Integer id, @Param("name") String name);

    /**
     * 修改回显
     * @return
     */
    Subclass getSubclass(@Param("id") Integer id);

    /**
     * 查询所有父类
     * @return
     */
    List<SubclassDto> getfType();

    /**
     * 查询是否存在name
     * @param id
     * @return
     */
    Integer getByid(@Param("id") Integer id, @Param("name") String name);

    /**
     * 修改顺序
     * @return
     */
    Integer updByIdSort(@Param("sort") Integer sort, @Param("id") Integer id);

    /**
     * 修改子类
     * @return
     */
    Boolean updTypeClass(@Param("subclass") Subclass subclass);

    /**
     * 查询最大的sort
     * @return
     */
    Integer getMaxSort(Integer typeId);
    /**
     * 查询指定年龄
     * @return
     */
    Integer getStypeageByid(Integer zid);

    /**
     *  删除子类
     * @param ids ID数组
     * @return 删除数量
     */
    Boolean deleteSubclass(Integer[] ids);

    /**
     *  多条件复合查询
     * @param typeName 子类名称
     * @param name 姓名
     * @param material 描述
     * @param createTime 创建时间
     * @param updateTime 更新时间
     * @param sort 排序
     * @return 返回符合条件的实体数组
     */
    List<Subclass> multipleConditionalQuerySubclass(String typeName,
                                                    String name,
                                                    String material,
                                                    String createTime,
                                                    String updateTime,
                                                    Integer sort);
}
