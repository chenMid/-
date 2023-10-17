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
import wakoo.fun.common.Log;
import wakoo.fun.log.Constants;
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
import java.util.Map;

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
    public MsgVo getAllsubclass(String keyword, Integer pageSize, Integer pageNumber) {
        pageNumber = Math.max(pageNumber, 1);
        PageHelper.startPage(pageNumber, pageSize);
        List<Subclass> allSubclass = subclassService.getAllSubclass(keyword);
        PageInfo<Subclass> pageInfo = new PageInfo<>(allSubclass);
        pageInfo.setPageSize(pageSize);
        return new MsgVo(200, "请求成功", pageInfo);
    }

    @ApiOperation(value = "多条件查询子类")
    @UserLoginToken
    @GetMapping("/multipleConditionalQuerySubclass")
    public MsgVo multipleConditionalQuerySubclass(Integer pageSize,
                                                  Integer pageNumber,
                                                  String typeName,
                                                  String name,
                                                  String material,
                                                  String createTime,
                                                  String updateTime,
                                                  Integer sort
    ){
        pageNumber = Math.max(pageNumber, 1);
        PageHelper.startPage(pageNumber, pageSize);
        List<Subclass> subclasses = subclassService.multipleConditionalQuerySubclass(typeName, name, material, createTime, updateTime, sort);
        PageInfo<Subclass> pageInfo = new PageInfo<>(subclasses);
        return new MsgVo(200, "请求成功", pageInfo);
    }


    @ApiOperation(value = "上传")
    @UserLoginToken
    @PostMapping("/addImg")
    public MsgVo addImg(@RequestPart MultipartFile file) throws IOException {
        try {
            // 上传头像到七牛云
            MsgVo msgVo = QiniuUtils.uploadAvatar(file, accessKey, secretKey, bucketName, null);
            if (msgVo.getCode() == 200) {
                // 返回成功消息
                return new MsgVo(200, "上传成功", msgVo.getData());
            } else {
                // 上传失败
                return new MsgVo(403, "上传失败", false);
            }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return new MsgVo(500, "服务器错误", false);
        }
    }


    @ApiOperation(value = "添加子类")
    @UserLoginToken
    @Transactional(rollbackFor = Exception.class)
    @Log(modul = "子类页面-子类添加", type = Constants.INSERT, desc = "操作添加按钮")
    @PostMapping("/addsubclass")
    public MsgVo addSubclass(@RequestBody Subclass subclass) {
        List<Integer> ageList = subclassService.getAgeList(Integer.parseInt(subclass.getTypeName()), subclass.getTypeAge(), subclass.getName());
        if (!ageList.isEmpty()) {
            new MsgVo(403, "年龄重复", false);
        }
        List<Integer> typeIdList = subclassService.getTypeIdByName(Integer.parseInt(subclass.getTypeName()),subclass.getName());
        if (typeIdList == null || typeIdList.isEmpty()) {
            subclass.setSort(1);
        } else {
            subclass.setSort(typeIdList.get(0));
        }
        String[] fruits = subclass.getInageImage().split(",");
        String[] fruits1 = subclass.getAgeImage().split(",");

        int startAge = subclass.getTypeAge();
        for (int i = 0; i < fruits.length; i++) {
            Subclass subclass1 = new Subclass(subclass.getTypeName(),subclass.getName(), startAge + i, fruits[i], fruits1[i], subclass.getMaterial(), subclass.getSort());
            subclassService.addSubclasss(subclass1);
        }
        return new MsgVo(200, "添加成功", true);
    }

    @ApiOperation(value = "年龄标记图")
    @UserLoginToken
    @GetMapping("/ageMarkChart")
    public MsgVo ageMarkChart(Integer typeAge, Integer typeAges,String inageImage){
        return getMsgVo(typeAge, typeAges, inageImage);
    }

    @ApiOperation(value = "年龄封面图")
    @UserLoginToken
    @GetMapping("/ageCover")
    public MsgVo ageCover(Integer typeAge, Integer typeAges,String ageImage){
        return getMsgVo(typeAge, typeAges, ageImage);
    }

    private MsgVo getMsgVo(Integer typeAge, Integer typeAges, String imageImage) {
        int num=0;
        for (int i = typeAge; i <= typeAges ; i++) {
            num++;
        }
        String[] fruits = imageImage.split(",");
        if (num < fruits.length) {
            return new MsgVo(200, "图片数量有误",false);
        }else if (num > fruits.length){
            return new MsgVo(200, "缺少图片请检查",false);
        }
        return new MsgVo(200, "",true);
    }

    @ApiOperation(value = "修改回显")
    @UserLoginToken
    @GetMapping("/getUpdSubclass")
    public MsgVo getUpdSubclass(Integer id) {
        Subclass subclass = subclassService.getSubclass(id);
        return new MsgVo(200, "请求成功", subclass);
    }

    @ApiOperation(value = "父类回显")
    @UserLoginToken
    @GetMapping("/getfType")
    public MsgVo getfType() {
        return new MsgVo(200, "请求成功", subclassService.getfType());
    }

    @ApiOperation(value = "修改子类")
    @UserLoginToken
    @Transactional(rollbackFor = Exception.class)
    @Log(modul = "子类页面-子类修改", type = Constants.UPDATE, desc = "操作修改按钮")
    @PutMapping("/updSubclass")
    public MsgVo updSubclass(@RequestBody Subclass subclass) {
            Integer stypeageByid = subclassService.getStypeageByid(subclass.getId());

            if (subclass.getTypeAge().equals(stypeageByid)) {
                List<Integer> ageList = subclassService.getAgeList(subclass.getId(), subclass.getTypeAge(), subclass.getName());

                if (!ageList.isEmpty()) {
                    return new MsgVo(403, "年龄重复", false);
                }
            }
            subclassService.getBySort(subclass.getSort(), subclass.getName());
            subclassService.updTypeClass(subclass);

            return new MsgVo(200, "修改成功", null);
    }


    @ApiOperation(value = "删除子类")
    @UserLoginToken
    @Log(modul = "子类页面-子类删除", type = Constants.DELETE, desc = "操作删除按钮")
    @DeleteMapping ("/updSubclassStatus")
    public MsgVo updSubclassStatus(@RequestBody Map<String, Integer[]> requestBody) {
        Integer[] ids = requestBody.get("ids");
        Boolean aBoolean = subclassService.deleteSubclass(ids);
        if (aBoolean) {
            return new MsgVo(200, "删除成功", true);
        }
        return new MsgVo(403, "删除失败", false);
    }
}

