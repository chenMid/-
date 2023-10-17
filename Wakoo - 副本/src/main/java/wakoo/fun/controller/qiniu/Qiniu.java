package wakoo.fun.controller.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wakoo.fun.common.Log;
import wakoo.fun.config.UserLoginToken;
import wakoo.fun.dto.FileInformationDto;
import wakoo.fun.log.Constants;
import wakoo.fun.utils.MsgUtils;
import wakoo.fun.utils.QiniuUtils;
import wakoo.fun.vo.MsgVo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@EnableTransactionManagement//数据库事务管理
@CrossOrigin
@RestController
@RequestMapping("/api")
@Api(tags = "Role")
public class Qiniu {
    @Value("${qiniu.access-key}")// 七牛云存储的访问密钥
    private String accessKey;
    @Value("${qiniu.secret-key}")// 七牛云存储的秘钥
    private String secretKey;
    @Value("${qiniu.bucket-name}")// 存储空间名称
    private String bucketName;
    @Value("${qiniu.domain}")// 访问七牛资源的域名
    private String domain;

    @ApiOperation(value = "七牛")
    @UserLoginToken
    @GetMapping("/qiniu")
    public MsgVo qiniu(String folderPath, Integer pageSize, Integer pageNumber) {
        Auth auth = Auth.create(accessKey, secretKey);
        Configuration configuration = new Configuration();
        BucketManager bucketManager = new BucketManager(auth, configuration);

        List<FileInformationDto> fileInformationMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(50);
        // 计算起始位置
        int start = (pageNumber - 1) * pageSize;

        // 设置每次获取文件列表的数量
        int limit = (pageSize < 1000) ? pageSize : 1000;

        // 创建文件列表迭代器
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketName, folderPath, limit, null);

        // 跳过起始位置之前的文件
        for (int i = 0; i < start; i++) {
            if (!fileListIterator.hasNext()) {
                // 超过文件总数，直接返回空结果
                map.put("fileInformation", fileInformationMap);
                return new MsgVo(MsgUtils.SUCCESS, map);
            }
            fileListIterator.next();
        }

        // 获取指定数量的文件信息
        int count = 0;
        while (fileListIterator.hasNext() && count < pageSize) {
            FileInfo[] items = fileListIterator.next();
            for (FileInfo fileInfo : items) {
                if (!isImageOrVideoFile(fileInfo.key)) {
                    // 跳过非图片和非视频文件
                    continue;
                }

                String fileUrl = domain + "/" + fileInfo.key;
                FileInformationDto fileInformation = new FileInformationDto();
                fileInformation.setFileName(fileInfo.key);
                fileInformation.setFileUrl(fileUrl);

                double fileSize = fileInfo.fsize;
                String fileSizeString = (fileSize < 1024 * 1024) ? String.format("%.2fKB", fileSize / 1024) : String.format("%.2fMB", fileSize / (1024 * 1024));
                fileInformation.setFileSize(fileSizeString);

                String uploadTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(fileInfo.putTime / 10000));
                fileInformation.setUploadTime(uploadTime);

                try {
                    String mimeType = bucketManager.stat(bucketName, fileInfo.key).mimeType;
                    fileInformation.setMimeType(mimeType);
                } catch (QiniuException e) {
                    e.printStackTrace();
                    // 如果无法获取MimeType，则设置为未知
                    fileInformation.setMimeType("Unknown");
                }

                fileInformationMap.add(fileInformation);
                count++;

                if (fileInformationMap.size() >= pageSize) {
                    break;
                }
            }
        }

        map.put("fileInformation", fileInformationMap);
        return new MsgVo(MsgUtils.SUCCESS, map);
    }
    /**
     * 判断文件是否为图片文件
     */
    public boolean isImageOrVideoFile(String fileName) {
        // 支持的图片和视频文件扩展名
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif"};
        String[] videoExtensions = {"mp4", "avi", "mkv", "mov"};
        // 获取文件扩展名
        String extension = getExtension(fileName);
        // 判断扩展名是否在支持的图片或视频文件扩展名列表中
        for (String imageExtension : imageExtensions) {
            if (imageExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }

        for (String videoExtension : videoExtensions) {
            if (videoExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }

        return false;
    }

    private String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex + 1).toLowerCase();
    }


    @ApiOperation(value = "七牛添加")
    @UserLoginToken
    @Log(modul = "资源页面-资源添加", type = Constants.INSERT, desc = "操作添加按钮")
    @GetMapping("/qiniuManagement")
    public MsgVo qiniuManagement(@RequestPart MultipartFile file, String folderPath) throws IOException {
        return QiniuUtils.uploadAvatar(file, accessKey, secretKey, bucketName, folderPath);
    }

    @ApiOperation(value = "七牛分类")
    @UserLoginToken
    @Log(modul = "资源页面-资源分类", type = Constants.UPDATE, desc = "操作分配按钮")
    @GetMapping("/sevenOxClassification")
    public MsgVo sevenOxClassification(){
        Configuration cfg = new Configuration(Region.autoRegion());
        // 实例化一个BucketManager对象
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);

        // 指定源文件的bucket和key
        String bucket = "2023.6.21编程分类视频及图片/";
        // 需要移动的文件名，可以传入多个
        String[] keys = {"1-1.png", "已读.png"};

        // 指定目标bucket和key
        String targetBucket = "2023.6.21编程分类视频及图片/test/test1";
        // 目标文件名，与源文件名一一对应
        String[] targetKeys = {"1-1.png", "已读.png"};


        try {
            // 执行移动文件操作
            for (int i = 0; i < keys.length; i++) {
                bucketManager.move(bucket, keys[i], targetBucket, targetKeys[i]);
            }

            System.out.println("文件移动成功！");
        } catch (QiniuException e) {
            System.out.println("文件移动失败，错误信息：" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
