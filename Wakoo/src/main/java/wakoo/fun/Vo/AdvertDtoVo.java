package wakoo.fun.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertDtoVo {
    private Integer id,sort;
    private String advertName;
    private String content,img;
}
