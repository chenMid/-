package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carousel {
    private Integer id,orderNumber,isActive;
    private String imageUrl,title,description,linkUrl;
    private MultipartFile file;
}
