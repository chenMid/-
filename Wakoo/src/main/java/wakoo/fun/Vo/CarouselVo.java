package wakoo.fun.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import wakoo.fun.dto.CarouselVoDto;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarouselVo {
    private Integer id,isActive;
    private List<Integer> orderNumber;
    private String imageUrl,title,description,linkUrl;
    private MultipartFile file;
}
