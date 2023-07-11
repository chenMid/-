package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Advert {
    private Integer id,status,sort;
    private String advertName;
    private String content,img,createtime,updatetime;
    private MultipartFile file;
}
