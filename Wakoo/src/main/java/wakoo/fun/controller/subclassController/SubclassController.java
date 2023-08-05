package wakoo.fun.controller.subclassController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.qiniu.storage.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wakoo.fun.vo.MsgVo;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dto.FileInformationDto;
import wakoo.fun.pojo.Subclass;
import wakoo.fun.service.SubclassService.SubclassService;
import wakoo.fun.utils.MsgUtils;
import wakoo.fun.utils.QiniuUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EnableTransactionManagement//数据库事务管理
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "Role")
public class SubclassController {
    @Value("${qiniu.access-key}")
    private String accessKey;
    @Value("${qiniu.secret-key}")
    private String secretKey;
    @Value("${qiniu.bucket-name}")
    private String bucketName;

    @Resource
    private SubclassService subclassService;

    @ApiOperation(value = "查询所有")
    @UserLoginToken
    @GetMapping("/getAllsubclass")
    public ResponseEntity<MsgVo> getAllsubclass(String keyword, Integer pageSize, Integer pageNumber) {
        pageSize = (pageSize == null || pageSize <= 0) ? 10 : pageSize;
        // 默认每页显示10条数据
        pageNumber = (pageNumber == null || pageNumber <= 0) ? 1 : pageNumber;
        // 默认显示第一页

        PageHelper.startPage(pageNumber, pageSize);
        List<Subclass> allSubclass = subclassService.getAllSubclass(keyword);
        PageInfo<Subclass> pageInfo = new PageInfo<>(allSubclass);
        return ResponseEntity.ok(new MsgVo(200, "请求成功", pageInfo));
    }


    @ApiOperation(value = "上传")
    @UserLoginToken
    @PostMapping("/addImg")
    public ResponseEntity<MsgVo> addImg(@RequestPart MultipartFile file) throws IOException {
        try {
            // 上传头像到七牛云
            MsgVo msgVo = QiniuUtils.uploadAvatar(file, accessKey, secretKey, bucketName,null);
            if (msgVo.getCode() == 200) {
                // 返回成功消息
                return ResponseEntity.ok(new MsgVo(200, "上传成功", msgVo.getData()));
            } else {
                // 上传失败
                return ResponseEntity.ok(new MsgVo(500, "上传失败", false));
            }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "服务器错误", false));
        }
    }


    @ApiOperation(value = "添加子类")
    @UserLoginToken
    @Transactional
    @PostMapping("/addsubclass")
    public ResponseEntity<MsgVo> addSubclass(@RequestBody Subclass subclass) {
        List<Integer> ageList = subclassService.getAgeList(subclass.getId(), subclass.getTypeAge(), subclass.getName());
        if (!ageList.isEmpty()) {
            return ResponseEntity.ok(new MsgVo(403, "年龄重复", false));
        }
        List<Integer> typeIdList = subclassService.getTypeIdByName(subclass.getId(), subclass.getName());
        if (typeIdList == null || typeIdList.isEmpty()) {
            subclass.setSort(1);
        } else {
            subclass.setSort(typeIdList.get(0));
        }
        String[] fruits = subclass.getInageImage().split(",");
        String[] fruits1 = subclass.getAgeImage().split(",");
        int startAge = subclass.getTypeAge();
        for (int i = 0; i < fruits.length; i++) {
            Subclass subclass1 = new Subclass(subclass.getId(), subclass.getName(), startAge + i, fruits[i], fruits1[i], subclass.getMaterial(), subclass.getSort());
            subclassService.addSubclasss(subclass1);
        }
        return ResponseEntity.ok(new MsgVo(200, "请求成功", true));
    }

    @ApiOperation(value = "修改回显")
    @UserLoginToken
    @GetMapping("/getUpdSubclass")
    public ResponseEntity<MsgVo> getUpdSubclass(Integer id) {
        Subclass subclass = subclassService.getSubclass(id);
        return ResponseEntity.ok(new MsgVo(200, "请求成功", subclass));
    }

    @ApiOperation(value = "父类回显")
    @UserLoginToken
    @GetMapping("/getfType")
    public ResponseEntity<MsgVo> getfType() {
        return ResponseEntity.ok(new MsgVo(200, "请求成功", subclassService.getfType()));
    }

    @ApiOperation(value = "修改子类")
    @UserLoginToken
    @Transactional
    @PutMapping("/updSubclass")
    public ResponseEntity<MsgVo> updSubclass(@RequestBody Subclass subclass) {
        try {
            Integer stypeageByid = subclassService.getStypeageByid(subclass.getZId());

            if (subclass.getTypeAge().equals(stypeageByid)) {
                List<Integer> ageList = subclassService.getAgeList(subclass.getId(), subclass.getTypeAge(), subclass.getName());

                if (!ageList.isEmpty()) {
                    return ResponseEntity.ok(new MsgVo(403, "年龄重复", false));
                }
            }
            subclassService.getBySort(subclass.getSort(), subclass.getName());
            subclassService.updTypeClass(subclass);

            return ResponseEntity.ok(new MsgVo(200, "请求成功", null));
        } catch (Exception e) {
            // 处理异常，例如记录日志或返回错误信息
            return ResponseEntity.ok(new MsgVo(500, "服务器错误", null));
        }
    }


    @ApiOperation(value = "修改状态接口")
    @UserLoginToken
    @Transactional
    @PutMapping("/updSubclassStatus")
    public ResponseEntity<MsgVo> updSubclassStatus(Integer id, Integer status) {
        Boolean setstatussubclass = subclassService.setstatussubclass(id, status);
        return ResponseEntity.ok(new MsgVo(200, "修改成功", setstatussubclass));
    }


}

