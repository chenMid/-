package wakoo.fun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarouselVo {
    private Integer id,isActive;
    private List<Integer> orderNumber;
    private String imageUrl,title,description,linkUrl;
    private MultipartFile file;
}
