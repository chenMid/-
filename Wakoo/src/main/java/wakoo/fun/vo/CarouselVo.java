package wakoo.fun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author HASEE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarouselVo {
    private Integer id;
    private Integer orderNumber;
    private String imageUrl,title,description;
    private String createdAt;
    private String updatedAt;
}
