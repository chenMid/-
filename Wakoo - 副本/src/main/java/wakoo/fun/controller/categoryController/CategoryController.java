package wakoo.fun.controller.categoryController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.common.Log;
import wakoo.fun.log.Constants;
import wakoo.fun.vo.MsgVo;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.pojo.Category;
import wakoo.fun.service.CategoryService.CategoryService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@EnableTransactionManagement//数据库事务管理
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "父类管理")
public class CategoryController {
    @Value("${qiniu.access-key}")
    private String accessKey;
    @Value("${qiniu.secret-key}")
    private String secretKey;
    @Value("${qiniu.bucket-name}")
    private String bucketName;

    @Resource
    private CategoryService categoryService;

    @ApiOperation(value = "添加父类")
    @UserLoginToken
    @Log(modul = "父类页面-添加父类", type = Constants.INSERT, desc = "操作添加按钮")
    @PostMapping("/addCategory")
    public MsgVo addCategory(@RequestBody Category category) {
            // 上传到七牛云
            boolean isAdded = categoryService.addCategory(category);
            if (isAdded) {
                return new MsgVo(200, "添加成功", true);
            } else {
                return new MsgVo(500, "添加失败", false);
            }
    }

    @ApiOperation(value = "查询所有及模糊查询")
    @UserLoginToken
    @GetMapping("/getAllCategory")
    public MsgVo getAllCategory(String keyword, Integer pageSize, Integer pageNumber) {
        pageNumber = Math.max(pageNumber, 1);
        PageHelper.startPage(pageNumber, pageSize);
        List<Category> allCategory = categoryService.getAllCategory(keyword);
        PageInfo<Category> pageInfo = new PageInfo<>(allCategory);
        pageInfo.setPageSize(pageSize);
        if (allCategory.isEmpty()) {
            return new MsgVo(203, "未找到相关数据", null);
        } else {
            return new MsgVo(200, "请求成功", pageInfo);
        }
    }

    @ApiOperation(value = "查询指定父类信息")
    @UserLoginToken
    @GetMapping("/getByIdCategory")
    public MsgVo getByIdCategory(Integer id) {
        Category allById = categoryService.getAllById(id);
        return new MsgVo(200, "请求成功", allById);
    }

    @ApiOperation(value = "修改父类信息")
    @UserLoginToken
    @Log(modul = "父类页面-修改父类", type = Constants.UPDATE, desc = "操作修改按钮")
    @PutMapping("/updCategory")
    public MsgVo updCategory(@RequestBody Category category) throws IOException {
            Boolean aBoolean = categoryService.updCategory(category);
            if (aBoolean) {
                return new MsgVo(200, "修改成功", true);
            } else {
                return new MsgVo(500, "修改失败", false);
            }
    }

    @ApiOperation(value = "删除父类信息")
    @UserLoginToken
    @Log(modul = "父类页面-删除父类", type = Constants.DELETE, desc = "操作删除按钮")
    @DeleteMapping("/deleteTheSuperclassInformation")
    public MsgVo deleteTheSuperclassInformation(@RequestBody Map<String, Integer[]> requestBody) {
        Integer[] ids = requestBody.get("ids");
        List<Category> categories = categoryService.queryWhetherThereAreSubclasses(ids);
        if (categories.size() == 0) {
            Boolean aBoolean = categoryService.deleteSuperclassesInBatches(ids);
            if (aBoolean) {
                return new MsgVo(200, "删除成功", categories);
            }
            return new MsgVo(403, "删除失败", false);
        } else {
            return new MsgVo(403, "请先删除子类后再删除", false);
        }
    }
}
