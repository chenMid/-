package wakoo.fun.controller.personalCenterController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wakoo.fun.common.Log;
import wakoo.fun.log.Constants;
import wakoo.fun.vo.AdvertDtoVo;
import wakoo.fun.vo.CarouselVo;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.vo.DeleteVo;
import wakoo.fun.vo.MsgVo;
import wakoo.fun.dto.AdvertDto;
import wakoo.fun.pojo.Advert;
import wakoo.fun.pojo.Carousel;
import wakoo.fun.service.AdvertService;
import wakoo.fun.utils.QiniuUtils;
import wakoo.fun.service.PersonalCenterService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@EnableTransactionManagement//数据库事务管理
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "Centre")
public class PersonalCenterController {
    @Autowired
    private PersonalCenterService personalCenterService;
    @Resource
    private AdvertService advertService;


    @Value("${qiniu.access-key}")
    private String accessKey;
    @Value("${qiniu.secret-key}")
    private String secretKey;
    @Value("${qiniu.bucket-name}")
    private String bucketName;

    @ApiOperation(value = "个人简介")
    @UserLoginToken
    @PostMapping("/uploadAvatar")
    public MsgVo uploadAvatar(@RequestPart MultipartFile file, @RequestParam Integer id) throws IOException {
        try {
            // 上传头像到七牛云
            MsgVo msgVo = QiniuUtils.uploadAvatar(file, accessKey, secretKey, bucketName,"2023.6.21编程分类视频及图片/test");
            if (msgVo.getCode() == 200) {
                // 保存用户头像信息
                personalCenterService.avatar(id, (String) msgVo.getData());
                // 返回成功消息
                return new MsgVo(200, "上传成功", true);
            } else {
                // 上传失败
                return new MsgVo(403, "上传失败", false);
            }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return new MsgVo(500, "服务器错误", false);
        }
    }

    @ApiOperation(value = "首页广告添加")
    @UserLoginToken
    @Log(modul = "广告页面-广告添加", type = Constants.INSERT, desc = "操作添加按钮")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @PostMapping("/addAvatar")
    public MsgVo addAvatar(@Validated @RequestBody Advert avatar, BindingResult result) throws IOException {
            if (result.hasErrors()) {
                String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
                return new MsgVo(403, errorMessage, false);
            }
            Boolean aBoolean = advertService.addAdver(avatar);
            if (aBoolean){
                return new MsgVo(200, "添加成功", true);
            }else {
                return new MsgVo(500, "添加失败", false);
            }
    }

    @ApiOperation(value = "首页广告查询")
    @UserLoginToken
    @GetMapping("/selectAvatar")
    public MsgVo selectAvatar(String keyword, Integer pageSize, Integer pageNumber) {
        pageNumber = Math.max(pageNumber, 1);

        PageHelper.startPage(pageNumber, pageSize);
            List<AdvertDto> avate = advertService.getAvate(keyword);
            PageInfo<AdvertDto> pageInfo = new PageInfo<>(avate);
            pageInfo.setPageSize(pageSize);
            // 请求成功，返回分页结果
            return new MsgVo(200, "请求成功", pageInfo);
    }


    @ApiOperation(value = "指定广告查询")
    @UserLoginToken
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @GetMapping("/getIpAdvert")
    public MsgVo getIpAdvert(Integer id) {
        try {
            // 调用 advertService.getIsAvate() 方法获取广告信息
            AdvertDtoVo isAvate = advertService.getIsAvate(id);

            if (isAvate != null) {
                // 请求成功，返回广告信息
                return new MsgVo(200, "请求成功", isAvate);
            } else {
                // 广告不存在或获取失败，返回错误消息
                return new MsgVo(403, "请求失败", isAvate);
            }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return new MsgVo(500, "服务器错误", null);
        }
    }

    @ApiOperation(value = "首页广告修改")
    @UserLoginToken
    @Log(modul = "广告页面-广告修改", type = Constants.UPDATE, desc = "操作修改按钮")
    @PutMapping("/updAdvert")
    public MsgVo updAdvert(@Validated @RequestBody AdvertDtoVo advertDtoVo,BindingResult result) {
        try {
            if (result.hasErrors()) {
                String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
                return new MsgVo(403, errorMessage, false);
            }
            Boolean aBoolean = advertService.updAvate(advertDtoVo);
                if (aBoolean) {
                    // 修改成功，返回成功消息
                    return new MsgVo(200, "修改成功", true);
                } else {
                    // 修改失败，返回错误消息
                    return new MsgVo(403, "修改失败", false);
                }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return new MsgVo(500, "服务器错误", null);
        }
    }


    @ApiOperation(value = "广告删除")
    @UserLoginToken
    @Log(modul = "广告页面-广告删除", type = Constants.DELETE, desc = "操作删除按钮")
    @DeleteMapping("/removeSpecifiedAds/{ids}")
    public MsgVo removeSpecifiedAds(@PathVariable Integer[] ids) {
            Boolean aBoolean = advertService.removeSpecifiedAds(ids);
            if (aBoolean) {
                return new MsgVo(200, "删除成功", true);
            } else {
                return new MsgVo(500, "删除失败", false);
            }
    }

    @ApiOperation(value = "轮播图添加")
    @UserLoginToken
    @Transactional(rollbackFor = Exception.class)
    @Log(modul = "轮播图页面-轮播图添加", type = Constants.INSERT, desc = "操作添加按钮")
    @PostMapping("/addSlideshow")
    public MsgVo addSlideshow(@Validated @RequestBody Carousel carousel,BindingResult result) throws IOException {
            if (result.hasErrors()) {
                String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
                return new MsgVo(403, errorMessage, false);
            }
            Integer orderNumber = advertService.getOrderNumber();
            carousel.setOrderNumber(orderNumber+1);
            // 添加轮播图
            Boolean isSuccess = advertService.addCarousel(carousel);

            if (isSuccess) {
                // 返回成功消息
                return new MsgVo(200, "添加成功", true);
            } else {
                // 添加失败
                return new MsgVo(403, "添加失败", false);
            }
    }

    @ApiOperation(value = "轮播图查询")
    @UserLoginToken
    @GetMapping("/getAllCarousel")
    public MsgVo getAllCarousel(String keyword, Integer pageSize, Integer pageNumber) {
        pageNumber = Math.max(pageNumber, 1);

        PageHelper.startPage(pageNumber, pageSize);
            List<CarouselVo> allConouselVo = advertService.getAllConouselVo(keyword);
            PageInfo<CarouselVo> pageInfo = new PageInfo<>(allConouselVo);

            // 请求成功，返回分页结果
            return new MsgVo(200, "请求成功", pageInfo);
    }
    @ApiOperation(value = "获取顺序")
    @UserLoginToken
    @GetMapping("/fetchSequence")
    public MsgVo fetchSequence(){
        return new MsgVo(200,"请求成功",advertService.listIntegerGetCa());
    }
    @ApiOperation(value = "查询指定轮播图")
    @UserLoginToken
    @GetMapping("/ipCarousel")
    public MsgVo ipCarousel(Integer id) {
        try {
            CarouselVo carouselVo = advertService.isCaouselVo(id);
            // 返回请求成功消息和轮播信息
            return new MsgVo(200, "请求成功", carouselVo);
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return new MsgVo(500, "服务器错误", null);
        }
    }

    @ApiOperation(value = "修改指定轮播图")
    @UserLoginToken
    @Transactional(rollbackFor = Exception.class)
    @Log(modul = "轮播图页面-轮播图修改", type = Constants.UPDATE, desc = "操作修改按钮")
    @PutMapping("/updCarousel")
    public MsgVo updCarousel(@Validated @RequestBody Carousel carousel,BindingResult result) {
        try {
            if (result.hasErrors()) {
                String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
                return new MsgVo(403, errorMessage, false);
            }
            Carousel carById = advertService.getCarById(carousel.getId());
            Carousel numberById = advertService.getNumberById(carousel.getOrderNumber());
        if (numberById != null && !carById.getOrderNumber().equals(numberById.getOrderNumber())){
                Boolean aBoolean = advertService.updCarnumber(numberById.getId(), carById.getOrderNumber());
            }
            Boolean aBoolean = advertService.updCaouselVo(carousel);
            // 根据返回结果判断是否更新成功
            if (aBoolean) {
                // 更新成功
                return new MsgVo(200, "更新成功", true);
            } else {
                // 更新失败
                return new MsgVo(500, "更新失败", false);
            }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return new MsgVo(500, "服务器错误", false);
        }
    }

    @ApiOperation(value = "删除轮播图")
    @UserLoginToken
    @Log(modul = "轮播图页面-轮播图删除", type = Constants.INSERT, desc = "操作删除按钮")
    @DeleteMapping("/deleteASpecifiedWheelMap")
    public MsgVo deleteASpecifiedWheelMap(@RequestBody Map<String, Integer[]> requestBody) {
        Integer[] ids = requestBody.get("ids");
        boolean isSuccess = advertService.deleteASpecifiedWheelMap(ids);
        if (isSuccess) {
            return new MsgVo(200,"删除成功",true);
        } else {
            return new MsgVo(403,"删除失败",false);
        }
    }
}
