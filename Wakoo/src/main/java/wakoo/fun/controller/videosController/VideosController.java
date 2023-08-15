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
import wakoo.fun.vo.MsgVo;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.pojo.Videos;
import wakoo.fun.service.VideosService.VideosService;
import wakoo.fun.vo.SubclassVo;
import wakoo.fun.vo.VideosVo;
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
    public ResponseEntity<MsgVo> getAllVideos(String keyword, Integer pageSize, Integer pageNumber) {
        // 默认每页显示10条数据
        pageSize = (pageSize == null || pageSize <= 0) ? 10 : pageSize;
        // 默认显示第一页
        pageNumber = (pageNumber == null || pageNumber <= 0) ? 1 : pageNumber;

        PageHelper.startPage(pageNumber, pageSize);
        List<Videos> allVideos = videosService.getAllVideos(keyword);
        PageInfo<Videos> pageInfo = new PageInfo<>(allVideos);
        return ResponseEntity.ok(new MsgVo(200, "请求成功", pageInfo));
    }

    @ApiOperation(value = "查询父类")
    @UserLoginToken
    @GetMapping("/getFtype")
    public ResponseEntity<MsgVo> getFtype() {
        Map<Object, Object> map = new HashMap<>(50);
        map.put("fType", videosService.getfTypeDto());
        return ResponseEntity.ok(new MsgVo(200, "请求成功", map));
    }

    @ApiOperation(value = "查询子类")
    @UserLoginToken
    @GetMapping("/getSontype")
    public ResponseEntity<MsgVo> getSontype(Integer fId) {
        Map<String, Object> map = new HashMap<>(50);
        List<SubclassVo> subclassVos = videosService.getAllSubclass(fId);
        if (subclassVos.size()!=0){
            map.put("result",subclassVos);
        }else {
            map.put("result","无查询结果");
        }
        return ResponseEntity.ok(new MsgVo(200, "请求成功",map));
    }

    @ApiOperation(value = "添加课程")
    @UserLoginToken
    @PostMapping("/addCourse")
    public ResponseEntity<MsgVo> addCourse(@RequestBody VideosVo videosVo) {
        try {
            videosVo.setVideoLength(BigDecimal.valueOf(new MultimediaObject(new URL(videosVo.getVideoUrl())).getInfo().getDuration()));
            String className = videosVo.getSubclassName().split("-")[0];
            int subClassNumber = Integer.parseInt(videosVo.getSubclassName().split("-")[1]);
            Integer isId = videosService.queryParentLevel(videosVo.getId(), className, subClassNumber);
            videosVo.setId(isId);
            boolean addVideo = videosService.addVideo(videosVo);
            return ResponseEntity.ok(new MsgVo(200, "请求成功", addVideo));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new MsgVo(200, "请填写正确的视频路径", null));
        }
    }

    @ApiOperation(value = "修改回显")
    @UserLoginToken
    @GetMapping("/modifyTheEcho")
    public ResponseEntity<MsgVo> modifyTheEcho(Integer specifiedEcho) {
        return ResponseEntity.ok(new MsgVo(200, "请求成功", videosService.exampleModifyTheCommandOutput(specifiedEcho)));
    }

    @ApiOperation(value = "修改课程视频")
    @UserLoginToken
    @PutMapping("/modifyTheCourseVideo")
    public ResponseEntity<MsgVo> modifyTheCourseVideo(@Validated @RequestBody VideosVo videosVo, Integer parentClassID, BindingResult result) {
        //分割videosVo.getSubclassName()并一次赋给变量
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.ok(new MsgVo(403, errorMessage, false));
        }
        try {
            // 获取视频时长
            videosVo.setVideoLength(BigDecimal.valueOf(new MultimediaObject(new URL(videosVo.getVideoUrl())).getInfo().getDuration()));
            // 解析子类名称和编号
            String[] subClassInfo = videosVo.getSubclassName().split("-");
            String className = subClassInfo[0];
            Integer subClassNumber = Integer.parseInt(subClassInfo[1]);

            // 查询父级课程ID
            videosVo.setId(videosService.queryParentLevel(videosVo.getId(), className, subClassNumber));
            // 修改课程视频
            Boolean aSuccessMessageIsDisplayed = videosService.modifyTheCourseVideo(videosVo, parentClassID);
            return ResponseEntity.ok(new MsgVo(200, "请求成功", aSuccessMessageIsDisplayed));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.ok(new MsgVo(200, "请填写正确的视频路径", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new MsgVo(500, "服务器内部错误", null));
        }
    }
}
