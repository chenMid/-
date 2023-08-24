package wakoo.fun.mapper;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.pojo.Category;

import java.util.List;

public interface CategoryMapper {
    /**
     * 添加父类
     * @return
     */
    Boolean addCategory(@Param("category") Category category);

    /**
     * 查询所有父类
     * @return
     */
    List<Category> getAllCategory(String keyword);

    /**
     * 查询指定用户
     * @return
     */
    Category getAllById(@Param("id") Integer id);

    /**
     * 修改父类信息
     * @param category
     * @return
     */
    Boolean updCategory(@Param("category") Category category);

    /**
     *  批量删除
     * @param ids 要删除的id
     * @return true删除成功，false删除失败
     */
    Boolean deleteSuperclassesInBatches(Integer[] ids);

    /**
     *  判断指定id的父类是否存在子类
     * @param ids 查询的id
     * @return 判断存在
     */
    List<Category> queryWhetherThereAreSubclasses(Integer[] ids);
}
