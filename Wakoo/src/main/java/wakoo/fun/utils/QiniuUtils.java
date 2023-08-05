package wakoo.fun.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;
import wakoo.fun.vo.MsgVo;

import java.io.IOException;

public class QiniuUtils {

    public static MsgVo uploadAvatar(MultipartFile file,String accessKey, String secretKey,String bucketName, String folderPath) throws IOException {
        MsgVo msg = null;
        if (file.isEmpty()) {
            msg = new MsgVo(MsgUtils.VALIDATE_ERROR); // 如果文件为空，则返回提示信息
        } else {
            try {
                // 创建七牛云配置对象
                Configuration cfg = new Configuration();
                // 创建七牛云授权对象
                Auth auth = Auth.create(accessKey, secretKey);
                // 创建七牛云上传管理器对象
                UploadManager uploadManager = new UploadManager(cfg);

                // 获取上传文件的原始文件名作为七牛云的文件名
                String key = file.getOriginalFilename();
                // 指定上传文件目录，如果未指定则默认为空字符串，即上传到根目录

                if (!folderPath.isEmpty()) {
                    // 在文件名前拼接指定文件夹路径和斜杠，形成完整的文件路径
                    key = folderPath + "/" + key;
                }

                // 获取上传文件的字节数组
                byte[] fileBytes = file.getBytes();

                // 生成上传凭证
                String upToken = auth.uploadToken(bucketName);
                // 调用上传方法进行文件上传
                Response response = uploadManager.put(fileBytes, key, upToken);

                // 处理上传响应结果
                if (response.isOK()) {
                    // 文件上传成功，拼接文件的URL
                    String fileUrl = "cdn.wakoohome.com/" + key;
                    msg = new MsgVo(MsgUtils.SUCCESS, fileUrl);
                } else {
                    msg = new MsgVo(MsgUtils.FAILED); // 文件上传失败
                }
            } catch (Exception ex) {
                msg = new MsgVo(MsgUtils.FAILED); // 其他异常情况，文件上传失败
            }
        }
        return msg;
    }
}