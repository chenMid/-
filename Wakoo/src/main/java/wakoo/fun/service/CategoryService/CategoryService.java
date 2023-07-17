package wakoo.fun.service.CategoryService;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.pojo.Category;

import java.util.List;

public interface CategoryService {
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
}
