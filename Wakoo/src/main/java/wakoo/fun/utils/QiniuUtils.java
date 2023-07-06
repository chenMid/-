package wakoo.fun.utils;


import com.qiniu.common.QiniuException;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;
import wakoo.fun.dto.MsgVo;

import java.io.IOException;

public class QiniuUtils {

    public static MsgVo uploadAvatar(MultipartFile file,String accessKey, String secretKey,String bucketName) throws IOException {
        MsgVo msg=null;
        if (file.isEmpty()) {
            msg=new MsgVo(MsgUtils.VALIDATE_ERROR); // 如果文件为空，则返回提示信息
        }
        try {
            Configuration cfg = new Configuration(); // 创建七牛云配置对象
            Auth auth = Auth.create(accessKey, secretKey); // 创建七牛云授权对象
            UploadManager uploadManager = new UploadManager(cfg); // 创建七牛云上传管理器对象

            String key = file.getOriginalFilename(); // 获取上传文件的原始文件名作为七牛云的文件名
            System.out.println(key);
            byte[] fileBytes = file.getBytes(); // 获取上传文件的字节数组

            String upToken = auth.uploadToken(bucketName); // 生成上传凭证
            uploadManager.put(fileBytes, key, upToken); // 调用上传方法进行文件上传
            String fileUrl ="cdn.wakoohome.com/"+key;

            msg=new MsgVo(MsgUtils.SUCCESS,fileUrl);
        } catch (QiniuException ex) {
            msg=new MsgVo(MsgUtils.FAILED);
        } catch (Exception ex) {
            msg=new MsgVo(MsgUtils.FAILED);
        }
        return msg;
    }
}