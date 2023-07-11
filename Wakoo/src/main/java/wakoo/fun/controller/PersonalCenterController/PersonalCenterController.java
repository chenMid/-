package wakoo.fun.controller.PersonalCenterController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wakoo.fun.Vo.AdvertDtoVo;
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
    public MsgVo uploadAvatar(@RequestPart MultipartFile file, @RequestParam Integer id) throws IOException {

        MsgVo msgVo = QiniuUtils.uploadAvatar(file, accessKey, secretKey, bucketName);
        if (msgVo.getCode() == 200) {
            personalCenterService.avatar(id, (String) msgVo.getData());
        }
        return msgVo;
    }

    @ApiOperation(value = "首页广告添加")
    @UserLoginToken
    @Transactional
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @PostMapping("/addAvatar")
    public MsgVo addAvatar(@ModelAttribute Advert avatar) throws IOException {MsgVo msgVo = QiniuUtils.uploadAvatar(avatar.getFile(), accessKey, secretKey, bucketName);
           if (msgVo.getCode()==200){
               String data =(String)msgVo.getData();
               avatar.setImg(data);
               advertService.addAdver(avatar);
           }else {
               msgVo=new MsgVo(500,"添加失败",false);
           }
        return msgVo;
    }

    @ApiOperation(value = "首页广告查询")
    @UserLoginToken
    @Transactional
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @GetMapping("/selectAvatar")
    public MsgVo selectAvatar(String keyword, Integer pageSize, Integer pageNumber){
        PageHelper.startPage(pageNumber, pageSize);
        List<AdvertDto> avate = advertService.getAvate(keyword);
        PageInfo<AdvertDto> pageInfo = new PageInfo<>(avate);
        return new MsgVo(200, "请求成功", pageInfo);
    }


    @ApiOperation(value = "指定广告查询")
    @UserLoginToken
    @Transactional
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @GetMapping("/getIpAdvert")
    public MsgVo getIpAdvert(Integer id){
        AdvertDtoVo isAvate = advertService.getIsAvate(id);
        return new MsgVo(200, "请求成功",isAvate);
    }

    @ApiOperation(value = "首页广告修改")
    @UserLoginToken
    @Transactional
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @PutMapping("/updAdvert")
    public MsgVo updAdvert(@RequestBody AdvertDtoVo advertDtoVo){
        Boolean aBoolean = advertService.updAvate(advertDtoVo);
        System.out.println(aBoolean);
        return new MsgVo(200,"修改成功",aBoolean);
    }


    @ApiOperation(value = "广告状态修改")
    @UserLoginToken
    @ApiResponses({@ApiResponse(responseCode = "500", description = "请联系管理员"), @ApiResponse(responseCode = "200", description = "响应成功")})
    @PutMapping("/updAdvertStatus")
    public MsgVo updAdvertStatus(Integer id,Integer status){
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
    public MsgVo addSlideshow(Carousel carousel) throws IOException {
        Integer orderNumber = advertService.getOrderNumber(carousel.getOrderNumber());
        if (orderNumber==0){
            Boolean aBoolean = advertService.addCarousel(carousel);
            MsgVo msgVo = QiniuUtils.uploadAvatar(carousel.getFile(), accessKey, secretKey, bucketName);
            carousel.setImageUrl((String) msgVo.getData());
            return new MsgVo(200,"添加成功",true);
        }else {
            return new MsgVo(500,"顺序已存在,请重新添加",false);
        }
    }
}
