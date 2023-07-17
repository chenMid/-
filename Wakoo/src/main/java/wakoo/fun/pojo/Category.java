package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private int id;
    private String typeName;
    private String parentImage;
    private String status;
    private String createTime;
    private String updateTime;
    private MultipartFile file;
}
