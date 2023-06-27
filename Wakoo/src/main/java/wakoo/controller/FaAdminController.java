package wakoo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wakoo.config.JwtUtils;
import wakoo.config.MsgUtils;
import wakoo.dto.MsgVo;
import wakoo.pojo.FaAdmin;
import wakoo.service.FaAdminService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/Login")
@Api(tags = "Admin")
public class FaAdminController {

    @Resource
    private FaAdminService faAdminService;
    @ApiOperation(value = "登录")
    @ApiResponses({
            @ApiResponse(responseCode = "500",description = "请联系管理员"),
            @ApiResponse(responseCode = "1000",description = "响应成功")
    })
    @PostMapping("/login")
    public MsgVo login(String userName, String password, String Token) throws Exception {
        MsgVo msg=null;
        if (null==Token){
            List<FaAdmin> faAdmins=faAdminService.faAdmin(userName);
            if (faAdmins.size()!=0){
                if (!faAdmins.get(0).getPassword().equals(password)){
                    msg=new MsgVo(400,"密码错误",null);
                }else {
                    String s = JwtUtils.generateToken(faAdmins.get(0).getId());
                    Boolean aBoolean = faAdminService.UpdToken(s, faAdmins.get(0).getId());
                    if (aBoolean){
                        msg=new MsgVo(MsgUtils.SUCCESS,faAdminService.faAdmin(userName));
                        /*登录成功*/
                    }else {
                        /*更新失败*/
                        msg=new MsgVo(MsgUtils.CONTACT_ADMINISTRATOR);
                    }
                }
            }else {
                msg=new MsgVo(MsgUtils.FAILED);
            }
        }else {
            Long lwtToken = JwtUtils.verifyAndParseToken(Token);
            if (null==lwtToken){
                msg=new MsgVo(MsgUtils.TOKEN_FAILURE);
            }else {
                msg=new MsgVo(MsgUtils.SUCCESS,faAdminService.faAdmin(userName));
            }
        }
        return msg;
    }


    @ApiOperation(value = "个人简介")
    @ApiResponses({
            @ApiResponse(responseCode = "500",description = "请联系管理员"),
            @ApiResponse(responseCode = "1000",description = "响应成功")
    })
    @PostMapping("/PersonalCenter")
    public ResponseEntity<String> PersonalCenter(@RequestParam String name,
                                                 @RequestParam int age,
                                                 @RequestPart MultipartFile avatar) {
        return null;
    }

}
