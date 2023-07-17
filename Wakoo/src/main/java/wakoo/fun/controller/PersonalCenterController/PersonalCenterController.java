package wakoo.fun.controller.PersonalCenterController;

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
import wakoo.fun.Vo.AdvertDtoVo;
import wakoo.fun.Vo.CarouselVo;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.Vo.MsgVo;
import wakoo.fun.dto.AdvertDto;
import wakoo.fun.pojo.Advert;
import wakoo.fun.pojo.Carousel;
import wakoo.fun.pojo.Role;
import wakoo.fun.service.AdvertService;
import wakoo.fun.utils.QiniuUtils;
import wakoo.fun.service.PersonalCenterService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

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
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "1000", description = "响应成功")})
    @PostMapping("/uploadAvatar")
    public ResponseEntity<MsgVo> uploadAvatar(@RequestPart MultipartFile file, @RequestParam Integer id) throws IOException {
        try {
            // 上传头像到七牛云
            MsgVo msgVo = QiniuUtils.uploadAvatar(file, accessKey, secretKey, bucketName);
            if (msgVo.getCode() == 200) {
                // 保存用户头像信息
                personalCenterService.avatar(id, (String) msgVo.getData());

                // 返回成功消息
                return ResponseEntity.ok(new MsgVo(200, "上传成功", true));
            } else {
                // 上传失败
                return ResponseEntity.ok(new MsgVo(500, "上传失败", false));
            }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "服务器错误", false));
        }
    }

    @ApiOperation(value = "首页广告添加")
    @UserLoginToken
    @Transactional
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @PostMapping("/addAvatar")
    public ResponseEntity<MsgVo> addAvatar(@Validated @ModelAttribute Advert avatar, BindingResult result) throws IOException {
        try {
            if (result.hasErrors()) {
                String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
                MsgVo response = new MsgVo(403, errorMessage, false);
                return ResponseEntity.ok(response);
            }
            // 上传头像到七牛云
            MsgVo msgVo = QiniuUtils.uploadAvatar(avatar.getFile(), accessKey, secretKey, bucketName);
            if (msgVo.getCode() == 200) {
                String imageUrl = (String) msgVo.getData();
                avatar.setImg(imageUrl);
                // 添加广告
                advertService.addAdver(avatar);
                // 返回成功消息
                return ResponseEntity.ok(new MsgVo(200, "添加成功", true));
            } else {
                // 上传失败
                return ResponseEntity.ok(new MsgVo(500, "添加失败", false));
            }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "服务器错误", false));
        }
    }

    @ApiOperation(value = "首页广告查询")
    @UserLoginToken
    @Transactional
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @GetMapping("/selectAvatar")
    public ResponseEntity<MsgVo> selectAvatar(String keyword, Integer pageSize, Integer pageNumber) {
        try {
            if (pageSize == null || pageSize <= 0) {
                pageSize = 10; // 默认每页显示10条数据
            }
            if (pageNumber == null || pageNumber <= 0) {
                pageNumber = 1; // 默认显示第一页
            }

            PageHelper.startPage(pageNumber, pageSize);
            List<AdvertDto> avate = advertService.getAvate(keyword);
            PageInfo<AdvertDto> pageInfo = new PageInfo<>(avate);

            // 请求成功，返回分页结果
            return ResponseEntity.ok(new MsgVo(200, "请求成功", pageInfo));
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常，返回错误消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "服务器错误", null));
        }
    }


    @ApiOperation(value = "指定广告查询")
    @UserLoginToken
    @Transactional
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @GetMapping("/getIpAdvert")
    public ResponseEntity<MsgVo> getIpAdvert(Integer id) {
        try {
            // 调用 advertService.getIsAvate() 方法获取广告信息
            AdvertDtoVo isAvate = advertService.getIsAvate(id);

            if (isAvate != null) {
                // 请求成功，返回广告信息
                return ResponseEntity.ok(new MsgVo(200, "请求成功", isAvate));
            } else {
                // 广告不存在或获取失败，返回错误消息
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "服务器错误", null));
        }
    }

    @ApiOperation(value = "首页广告修改")
    @UserLoginToken
    @Transactional
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @PutMapping("/updAdvert")
    public ResponseEntity<MsgVo> updAdvert(@Validated AdvertDtoVo advertDtoVo,BindingResult result) {
        try {
            if (result.hasErrors()) {
                String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
                MsgVo response = new MsgVo(403, errorMessage, false);
                return ResponseEntity.ok(response);
            }
            // 调用 advertService.updAvate() 方法进行广告修改
            MsgVo msgVo = QiniuUtils.uploadAvatar(advertDtoVo.getFile(), accessKey, secretKey, bucketName);
                advertDtoVo.setImg((String) msgVo.getData());
                Boolean aBoolean = advertService.updAvate(advertDtoVo);
                if (aBoolean) {
                    // 修改成功，返回成功消息
                    return ResponseEntity.ok(new MsgVo(200, "修改成功", aBoolean));
                } else {
                    // 修改失败，返回错误消息
                    return ResponseEntity.badRequest().body(new MsgVo(400, "修改失败", aBoolean));
                }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "服务器错误", null));
        }
    }


    @ApiOperation(value = "广告状态修改")
    @UserLoginToken
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @PutMapping("/updAdvertStatus")
    public MsgVo updAdvertStatus(Integer id, Integer status) {
        try {
            Boolean aBoolean = advertService.updAvateStatus(id, status);
            if (aBoolean) {
                return new MsgVo(200, "广告状态修改成功", aBoolean);
            } else {
                return new MsgVo(500, "广告状态修改失败，请重试", aBoolean);
            }
        } catch (Exception e) {
            return new MsgVo(500, "出现错误，请重试", null);
        }
    }

    @ApiOperation(value = "轮播图添加")
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @UserLoginToken
    @PostMapping("/addSlideshow")
    public ResponseEntity<MsgVo> addSlideshow(@Validated Carousel carousel,BindingResult result) throws IOException {
        try {
            if (result.hasErrors()) {
                String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
                MsgVo response = new MsgVo(403, errorMessage, false);
                return ResponseEntity.ok(response);
            }
            Integer orderNumber = advertService.getOrderNumber();
            carousel.setOrderNumber(orderNumber+1);
            // 上传轮播图图片到七牛云
            MsgVo msgVo = QiniuUtils.uploadAvatar(carousel.getFile(), accessKey, secretKey, bucketName);
            String imageUrl = (String) msgVo.getData();
            carousel.setImageUrl(imageUrl);

            // 添加轮播图
            Boolean isSuccess = advertService.addCarousel(carousel);

            if (isSuccess) {
                // 返回成功消息
                return ResponseEntity.ok(new MsgVo(200, "添加成功", true));
            } else {
                // 添加失败
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "添加失败", false));
            }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "服务器错误", false));
        }
    }

    @ApiOperation(value = "轮播图查询")
    @UserLoginToken
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @GetMapping("/getAllCarousel")
    public ResponseEntity<MsgVo> getAllCarousel(String keyword, Integer pageSize, Integer pageNumber) {
        try {
            if (pageSize == null || pageSize <= 0) {
                pageSize = 10; // 默认每页显示10条数据
            }
            if (pageNumber == null || pageNumber <= 0) {
                pageNumber = 1; // 默认显示第一页
            }

            PageHelper.startPage(pageNumber, pageSize);
            List<CarouselVo> allConouselVo = advertService.getAllConouselVo(keyword);
            PageInfo<CarouselVo> pageInfo = new PageInfo<>(allConouselVo);

            // 请求成功，返回分页结果
            return ResponseEntity.ok(new MsgVo(200, "请求成功", pageInfo));
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常，返回错误消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "服务器错误", null));
        }
    }


    @ApiOperation(value = "查询指定轮播图")
    @UserLoginToken
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @GetMapping("/ipCarousel")
    public ResponseEntity<MsgVo> ipCarousel(Integer id) {
        try {
            CarouselVo carouselVo = advertService.isCaouselVo(id);
            // 返回请求成功消息和轮播信息
            return ResponseEntity.ok(new MsgVo(200, "请求成功", carouselVo));
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "服务器错误", null));
        }
    }

    @ApiOperation(value = "修改指定轮播图")
    @UserLoginToken
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @PutMapping("/updCarousel")
    public ResponseEntity<MsgVo> updCarousel(Carousel carousel,BindingResult result) {
        try {
            if (result.hasErrors()) {
                String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
                MsgVo response = new MsgVo(403, errorMessage, false);
                return ResponseEntity.ok(response);
            }
            Carousel carById = advertService.getCarById(carousel.getId());
            Carousel numberById = advertService.getNumberById(carousel.getOrderNumber());
            if (carById.getOrderNumber()!=numberById.getOrderNumber()){
                Boolean aBoolean = advertService.updCarnumber(numberById.getId(), carById.getOrderNumber());
                System.out.println(aBoolean);
            }
            MsgVo msgVo = QiniuUtils.uploadAvatar(carousel.getFile(), accessKey, secretKey, bucketName);
            carousel.setImageUrl((String) msgVo.getData());
            Boolean aBoolean = advertService.updCaouselVo(carousel);
            // 根据返回结果判断是否更新成功
            if (aBoolean) {
                // 更新成功
                return ResponseEntity.ok(new MsgVo(200, "更新成功", true));
            } else {
                // 更新失败
                return ResponseEntity.ok(new MsgVo(500, "更新失败", false));
            }
        } catch (Exception e) {
            // 出现异常，返回错误消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgVo(500, "服务器错误", false));
        }
    }

    @ApiOperation(value = "修改状态轮播图")
    @UserLoginToken
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @PutMapping("/updstatusCarousel")
    public ResponseEntity<MsgVo> updstatusCarousel(Integer status, Integer id) {
        boolean isSuccess = advertService.updCaouselStatus(id, status);
        if (isSuccess) {
            MsgVo message = new MsgVo(200,"修改成功",isSuccess);
            return ResponseEntity.ok(message);
        } else {
            MsgVo message = new MsgVo(500,"修改失败",isSuccess);
            return ResponseEntity.badRequest().body(message);
        }
    }
}
