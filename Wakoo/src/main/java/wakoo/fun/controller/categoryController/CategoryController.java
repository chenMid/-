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
import wakoo.fun.vo.MsgVo;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.pojo.Category;
import wakoo.fun.service.CategoryService.CategoryService;
import wakoo.fun.utils.QiniuUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

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
    @PostMapping("/addCategory")
    public ResponseEntity<MsgVo> addCategory(@RequestBody Category category) {
        try {
            // 上传到七牛云
            MsgVo msgVo = QiniuUtils.uploadAvatar(category.getFile(), accessKey, secretKey, bucketName);
            if (msgVo.getCode() == 200) {
                category.setParentImage((String) msgVo.getData());
                boolean isAdded = categoryService.addCategory(category);
                if (isAdded) {
                    return ResponseEntity.ok(new MsgVo(200, "添加成功", true));
                } else {
                    return ResponseEntity.ok(new MsgVo(500, "添加失败", false));
                }
            } else {
                return ResponseEntity.ok(new MsgVo(msgVo.getCode(), "七牛云上传错误", false));
            }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "服务器错误", false));
        }
    }

    @ApiOperation(value = "查询所有及模糊查询")
    @UserLoginToken
    @GetMapping("/getAllCategory")
    public ResponseEntity<MsgVo> getAllCategory(String keyword, Integer pageSize, Integer pageNumber) {
        pageSize = (pageSize == null || pageSize <= 0) ? 10 : pageSize; // 默认每页显示10条数据
        pageNumber = (pageNumber == null || pageNumber <= 0) ? 1 : pageNumber; // 默认显示第一页

        PageHelper.startPage(pageNumber, pageSize);
        List<Category> allCategory = categoryService.getAllCategory(keyword);
        PageInfo<Category> pageInfo = new PageInfo<>(allCategory);

        if (allCategory.isEmpty()) {
            return ResponseEntity.ok(new MsgVo(204, "未找到相关数据", null));
        } else {
            return ResponseEntity.ok(new MsgVo(200, "请求成功", pageInfo));
        }
    }

    @ApiOperation(value = "查询指定父类信息")
    @UserLoginToken
    @GetMapping("/getByIdCategory")
    public ResponseEntity<MsgVo> getByIdCategory(Integer id) {
        Category allById = categoryService.getAllById(id);
        return ResponseEntity.ok(new MsgVo(200, "请求成功", allById));
    }

    @ApiOperation(value = "修改父类信息")
    @UserLoginToken
    @PutMapping("/updCategory")
    public ResponseEntity<MsgVo> updCategory(@RequestBody Category category) throws IOException {
        try {
            MsgVo msgVo = QiniuUtils.uploadAvatar(category.getFile(), accessKey, secretKey, bucketName);
            if (msgVo.getCode() == 200) {
                category.setParentImage((String) msgVo.getData());
                Boolean aBoolean = categoryService.updCategory(category);
                if (aBoolean) {
                    return ResponseEntity.ok(new MsgVo(200, "修改成功", true));
                } else {
                    return ResponseEntity.ok(new MsgVo(500, "修改失败", false));
                }
            } else {
                return ResponseEntity.ok(new MsgVo(msgVo.getCode(), "七牛云上传错误", false));
            }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "服务器错误", false));
        }
    }
}
