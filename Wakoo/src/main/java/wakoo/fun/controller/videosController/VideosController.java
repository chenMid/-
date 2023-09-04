package wakoo.fun.controller.videosController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wakoo.fun.common.Log;
import wakoo.fun.log.Constants;
import wakoo.fun.vo.MsgVo;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.pojo.Videos;
import wakoo.fun.service.VideosService.VideosService;
import wakoo.fun.vo.SubclassVo;
import wakoo.fun.vo.VideosVo;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库事务管理
 *
 * @author HASEE
 */
@EnableTransactionManagement
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "视频管理")
public class VideosController {
    @Value("${qiniu.access-key}")
    private String accessKey;
    @Value("${qiniu.secret-key}")
    private String secretKey;
    @Value("${qiniu.bucket-name}")
    private String bucketName;
    @Resource
    private VideosService videosService;

    @ApiOperation(value = "查询所有")
    @UserLoginToken
    @GetMapping("/getAllVideos")
    public MsgVo getAllVideos(String keyword, Integer pageSize, Integer pageNumber) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Videos> allVideos = videosService.getAllVideos(keyword);
        PageInfo<Videos> pageInfo = new PageInfo<>(allVideos);
        pageInfo.setPageSize(pageSize);
        return new MsgVo(200, "请求成功", pageInfo);
    }

    @ApiOperation(value = "多条件查询视频")
    @UserLoginToken
    @GetMapping("/queryVideosBasedOnMultipleCriteria")
    public MsgVo queryVideosBasedOnMultipleCriteria(Integer pageSize, Integer pageNumber,String typeName,String classTypeName,String title,String videoLength,Integer which){
        PageHelper.startPage(pageNumber, pageSize);
        List<Videos> allVideos = videosService.queryVideosBasedOnMultipleCriteria(typeName, classTypeName, title, videoLength, which);
        PageInfo<Videos> pageInfo = new PageInfo<>(allVideos);
        pageInfo.setPageSize(pageSize);
        return new MsgVo(200, "请求成功", pageInfo);
    }

    @ApiOperation(value = "查询父类")
    @UserLoginToken
    @GetMapping("/getFtype")
    public MsgVo getFtype() {
        Map<Object, Object> map = new HashMap<>(50);
        map.put("fType", videosService.getfTypeDto());
        return new MsgVo(200, "请求成功", map);
    }

    @ApiOperation(value = "查询子类")
    @UserLoginToken
    @GetMapping("/getSontype")
    public MsgVo getSontype(Integer fId) {
        Map<String, Object> map = new HashMap<>(50);
        List<SubclassVo> subclassVos = videosService.getAllSubclass(fId);
        if (subclassVos.size()!=0){
            map.put("result",subclassVos);
        }
        return new MsgVo(200, "请求成功",map);
    }

    @ApiOperation(value = "添加课程")
    @UserLoginToken
    @Log(modul = "视频课程页面-添加课程", type = Constants.INSERT, desc = "操作添加按钮")
    @PostMapping("/addCourse")
    public MsgVo addCourse(@RequestBody VideosVo videosVo) throws MalformedURLException, EncoderException {
            videosVo.setVideoLength(BigDecimal.valueOf(new MultimediaObject(new URL(videosVo.getVideoUrl())).getInfo().getDuration()));
            boolean addVideo = videosService.addVideo(videosVo);
            if (addVideo){
                return new MsgVo(200, "添加成功", true);
            }
            return new MsgVo(403, "添加失败", false);
    }

    @ApiOperation(value = "修改回显")
    @UserLoginToken
    @GetMapping("/modifyTheEcho")
    public MsgVo modifyTheEcho(Integer specifiedEcho) {
        return new MsgVo(200, "请求成功", videosService.exampleModifyTheCommandOutput(specifiedEcho));
    }

    @ApiOperation(value = "修改课程视频")
    @UserLoginToken
    @Log(modul = "视频课程页面-修改课程", type = Constants.UPDATE, desc = "操作修改按钮")
    @PutMapping("/modifyTheCourseVideo")
    public MsgVo modifyTheCourseVideo(@Validated @RequestBody VideosVo videosVo, BindingResult result) throws MalformedURLException, EncoderException {
        //分割videosVo.getSubclassName()并一次赋给变量
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            return new MsgVo(403, errorMessage, false);
        }
            // 获取视频时长
            videosVo.setVideoLength(BigDecimal.valueOf(new MultimediaObject(new URL(videosVo.getVideoUrl())).getInfo().getDuration()));
            // 修改课程视频
            Boolean aSuccessMessageIsDisplayed = videosService.modifyTheCourseVideo(videosVo);
            if (aSuccessMessageIsDisplayed) {
                return new MsgVo(200, "修改成功", true);
            }
            return new MsgVo(403, "修改失败", false);
    }

    @ApiOperation(value = "删除视频")
    @UserLoginToken
    @Log(modul = "视频课程页面-删除课程", type = Constants.UPDATE, desc = "操作删除按钮")
    @DeleteMapping ("/deleteVideo")
    public MsgVo deleteVideo(@RequestBody Map<String, Integer[]> requestBody) {
        Integer[] ids = requestBody.get("ids");
        Boolean aBoolean = videosService.deleteVideo(ids);
        if (aBoolean){
            return new MsgVo(200, "删除成功",true);
        }
        return new MsgVo(200, "删除失败",false);
    }
}
