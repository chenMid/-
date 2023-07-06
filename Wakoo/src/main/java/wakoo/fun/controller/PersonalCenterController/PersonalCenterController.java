package wakoo.fun.controller.PersonalCenterController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wakoo.fun.dto.MsgVo;
import wakoo.fun.utils.QiniuUtils;
import wakoo.fun.service.PersonalCenterService;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "Centre")
public class PersonalCenterController {
    @Autowired
    private PersonalCenterService personalCenterService;
    @Value("${qiniu.access-key}")
    private String accessKey;
    @Value("${qiniu.secret-key}")
    private String secretKey;
    @Value("${qiniu.bucket-name}")
    private String bucketName;

    @ApiOperation(value = "个人简介")
    @ApiResponses({
            @ApiResponse(responseCode = "500",description = "请联系管理员"),
            @ApiResponse(responseCode = "1000",description = "响应成功")
    })
    @PostMapping("/uploadAvatar")
    public MsgVo uploadAvatar(@RequestPart MultipartFile file,@RequestParam Integer id) throws IOException {

        MsgVo msgVo = QiniuUtils.uploadAvatar(file, accessKey, secretKey, bucketName);
        if (msgVo.getCode()==1000){
            personalCenterService.avatar(id, (String) msgVo.getData());
        }
        return msgVo;
    }
}
