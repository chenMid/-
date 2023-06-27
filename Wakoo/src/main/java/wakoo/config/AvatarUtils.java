package wakoo.config;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class AvatarUtils {

    public static void saveAvatar(MultipartFile avatar, String path) throws IOException {
        File file = new File(path);

        // 创建目录
        File directory = new File(file.getParent());
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory: " + directory);
        }
        // 保存文件
        avatar.transferTo(file);
    }
}
