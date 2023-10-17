package wakoo.fun.service.CategoryService.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.mapper.CategoryMapper;
import wakoo.fun.pojo.Category;
import wakoo.fun.service.CategoryService.CategoryService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Boolean addCategory(Category category) {
        return categoryMapper.addCategory(category);
    }

    @Override
    public List<Category> getAllCategory(String keyword) {
        return categoryMapper.getAllCategory(keyword);
    }

    @Override
    public Category getAllById(Integer id) {
        return categoryMapper.getAllById(id);
    }

    @Override
    public Boolean updCategory(Category category) {
        return categoryMapper.updCategory(category);
    }

    @Override
    public Boolean deleteSuperclassesInBatches(Integer[] ids) {
        return categoryMapper.deleteSuperclassesInBatches(ids);
    }

    @Override
    public List<Category> queryWhetherThereAreSubclasses(Integer[] ids) {
        return categoryMapper.queryWhetherThereAreSubclasses(ids);
    }
}
